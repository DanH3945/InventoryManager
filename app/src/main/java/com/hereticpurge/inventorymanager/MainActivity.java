package com.hereticpurge.inventorymanager;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.hereticpurge.inventorymanager.database.ProductDatabase;
import com.hereticpurge.inventorymanager.fragments.AboutDialog;
import com.hereticpurge.inventorymanager.fragments.DetailFragment;
import com.hereticpurge.inventorymanager.fragments.EditFragment;
import com.hereticpurge.inventorymanager.fragments.MainFragment;
import com.hereticpurge.inventorymanager.fragments.RecyclerFragment;
import com.hereticpurge.inventorymanager.model.DebugProductItemFactory;
import com.hereticpurge.inventorymanager.model.ProductItem;
import com.hereticpurge.inventorymanager.model.ProductViewModel;
import com.hereticpurge.inventorymanager.utils.BarcodeReader;

public class MainActivity extends AppCompatActivity {

    private RecyclerFragment mRecyclerFragment;
    private MainFragment mMainFragment;

    private ProductViewModel mViewModel;

    private AdView mAdView;

    private static final int BARCODE_SEARCH = 100;
    private static final int BARCODE_QUICK_CHANGE = 101;
    private static final int PLAY_SERVICES_DIALOG_RESULT_CODE = 200;

    private static final int BARCODE_DEBUG = 1000;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);

        if (savedInstanceState == null) {
            loadFragment(getMainFragment(), false, null);
        }

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mAdView = findViewById(R.id.included_ad_view);
        mAdView.loadAd(adRequest);

        // Fix for ad view dropping the frame rate of the app. Credit to Martin on stackoverflow.com
        // for giving me the idea to switch the layer type.  My solution is somewhat simpler.  Just
        // change the layer type for the entire view since i'm only trying to load test ads on a demo.
        // https://stackoverflow.com/questions/9366365/android-admob-admob-ad-refresh-destroys-frame-rate
        mAdView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

    }

    @Override
    protected void onResume() {
        int playServicesResultCode = GoogleApiAvailability
                .getInstance()
                .isGooglePlayServicesAvailable(this);

        if (playServicesResultCode == ConnectionResult.SUCCESS) {
            super.onResume();
        } else {
            GoogleApiAvailability
                    .getInstance()
                    .getErrorDialog(this,
                            playServicesResultCode,
                            PLAY_SERVICES_DIALOG_RESULT_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.overflow_items, menu);

        if (BuildConfig.DEBUG) {
            menu.findItem(R.id.menu_debug_generic_btn).setVisible(true);
            menu.findItem(R.id.menu_debug_scan_btn).setVisible(true);
            menu.findItem(R.id.menu_debug_clear_database).setVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_about_btn:
                Log.e(TAG, "onOptionsItemSelected: About Pressed");
                new AboutDialog().show(getSupportFragmentManager(), null);
                break;

            case R.id.menu_pref_btn:
                Log.e(TAG, "onOptionsItemSelected: Preferences Pressed");
                break;

            case R.id.menu_debug_generic_btn:
                Log.e(TAG, "onOptionsItemSelected: DEBUG Generic Pressed");
                mViewModel.addProduct(DebugProductItemFactory.getDebugProduct());
                break;

            case R.id.menu_debug_scan_btn:
                startCameraForResult(BARCODE_DEBUG);
                Log.e(TAG, "onOptionsItemSelected: DEBUG Scan Pressed");
                break;

            case R.id.menu_debug_clear_database:
                Log.e(TAG, "onOptionsItemSelected: Wipe Database Pressed");
                mViewModel.deleteAllProducts();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof EditFragment) {
            ((EditFragment) fragment).confirmNavigateAwaySave();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        ProductDatabase.destroyInstance();
        super.onDestroy();
    }

    private void loadFragment(Fragment fragment, boolean addToBackStack, @Nullable String s) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(s);
        }
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private DetailFragment getNewDetailFragment(int id) {
        return DetailFragment.createInstance(id, productItem -> onEditButtonPressed(productItem));
    }

    private EditFragment getEditFragment(ProductItem productItem) {
        return EditFragment.createInstance(productItem);
    }

    private RecyclerFragment getRecyclerFragment() {
        if (mRecyclerFragment == null) {
            mRecyclerFragment = RecyclerFragment.createInstance(id -> onProductSelected(id));
        }
        return mRecyclerFragment;
    }

    private MainFragment getMainFragment() {
        if (mMainFragment == null) {
            mMainFragment = MainFragment.createFragment(new MainFragment.MainFragmentButtonListener() {

                @Override
                public void onBrowseAllPressed() {
                    loadFragment(getRecyclerFragment(),
                            true,
                            null);
                }

                @Override
                public void onNewItemPressed() {
                    loadFragment(
                            getEditFragment(null),
                            true,
                            null);
                }

                @Override
                public void onBarcodeSearch() {
                    startCameraForResult(BARCODE_SEARCH);
                }

                @Override
                public void onQuickChangePressed() {
                    startCameraForResult(BARCODE_QUICK_CHANGE);
                }
            });
        }
        return mMainFragment;
    }

    public void barcodeSearch(@Nullable String barcode) {
        if (barcode != null) {
            LiveData<ProductItem> productItemLiveData = mViewModel.getProductByBarcode(barcode);
            ProductItem productItem = productItemLiveData.getValue();
            try {
                loadFragment(getNewDetailFragment(
                        productItem.getId()),
                        true,
                        null);
            } catch (NullPointerException npe) {
                productNotFoundErrorToast();
            }
        }
    }

    public void onProductSelected(int id) {
        loadFragment(getNewDetailFragment(id),
                true,
                null);
    }

    private void startCameraForResult(int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, requestCode);
        } else {
            Toast.makeText(this,
                    R.string.no_camera_app_error,
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Object dataObject = null;

        if (data != null && data.hasExtra("data") && data.getExtras().containsKey("data")) {
            dataObject = data.getExtras().get("data");
        }

        switch (requestCode) {

            case BARCODE_SEARCH:
                if (dataObject instanceof Bitmap) {
                    String barcode = BarcodeReader.getBarcode(this, (Bitmap) dataObject);
                    if (barcode != null) {
                        barcodeSearch(barcode);
                    }
                }
                break;

            case BARCODE_QUICK_CHANGE:
                if (dataObject instanceof Bitmap) {
                    String barcode = BarcodeReader.getBarcode(this, (Bitmap) dataObject);
                    if (barcode != null) {
                        quickStockIncrement(barcode, getMainFragment().getNumberPickerValue());
                    }
                }
                break;

            case BARCODE_DEBUG:
                if (dataObject instanceof Bitmap) {
                    String barcode = BarcodeReader.getBarcode(this, (Bitmap) dataObject);
                    if (barcode != null) {
                        ProductItem productItem = DebugProductItemFactory.getDebugProduct(barcode);
                        mViewModel.addProduct(productItem);
                    }

                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void quickStockIncrement(@NonNull String barcode, int value) {
        try {
            ProductItem productItem = mViewModel.getProductByBarcode(barcode).getValue();
            productItem.setCurrentStock(productItem.getCurrentStock() + value);
            mViewModel.addProduct(productItem);
        } catch (NullPointerException e) {
            productNotFoundErrorToast();
        }
    }

    private void productNotFoundErrorToast() {
        Toast.makeText(this, R.string.product_not_found_error, Toast.LENGTH_LONG).show();
    }

    public void onEditButtonPressed(ProductItem productItem) {
        loadFragment(getEditFragment(productItem), true, null);
    }
}
