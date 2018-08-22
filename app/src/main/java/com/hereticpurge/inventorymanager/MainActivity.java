package com.hereticpurge.inventorymanager;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hereticpurge.inventorymanager.database.ProductDatabase;
import com.hereticpurge.inventorymanager.model.ProductItem;
import com.hereticpurge.inventorymanager.model.ProductViewModel;
import com.hereticpurge.inventorymanager.utils.BarcodeReader;
import com.hereticpurge.inventorymanager.fragments.DetailFragment;
import com.hereticpurge.inventorymanager.fragments.EditFragment;
import com.hereticpurge.inventorymanager.fragments.MainFragment;
import com.hereticpurge.inventorymanager.fragments.RecyclerFragment;
import com.hereticpurge.inventorymanager.fragments.RecyclerFragmentAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerFragment mRecyclerFragment;
    private MainFragment mMainFragment;

    private ProductViewModel viewModel;

    private static final int BARCODE_SEARCH = 100;
    private static final int BARCODE_QUICK_CHANGE = 200;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        viewModel = ViewModelProviders.of(this).get(ProductViewModel.class);

        if (savedInstanceState == null) {
            loadFragment(getMainFragment());
        }

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.overflow_items);
    }

    @Override
    protected void onDestroy() {
        ProductDatabase.destroyInstance();
        super.onDestroy();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private DetailFragment getDetailFragment(ProductItem productItem){
        return DetailFragment.createInstance(productItem);
    }

    private EditFragment getEditFragment(ProductItem productItem){
        return EditFragment.createInstance(productItem);
    }

    private RecyclerFragment getRecyclerFragment() {
        if (mRecyclerFragment == null) {
            mRecyclerFragment = RecyclerFragment.createFragment(new RecyclerFragmentAdapter.RecyclerCallback() {
                @Override
                public void onItemSelected(int id) {
                    onProductSelected(id);
                }
            });
        }
        return mRecyclerFragment;
    }

    private MainFragment getMainFragment() {
        if (mMainFragment == null) {
            mMainFragment = MainFragment.createFragment(new MainFragment.MainFragmentButtonListener() {

                @Override
                public void onBrowseAllPressed() {
                    loadFragment(getRecyclerFragment());
                }

                @Override
                public void onNewItemPressed() {
                    loadFragment(getEditFragment(null));
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

    public void onSearch(View view) {
        String query;

        if (view instanceof EditText) {
            query = ((EditText) view).getText().toString();
        } else {
            Log.d(TAG, "onSearch: Couldn't recognize view type.  view is not a type of EditText");
            query = null;
        }
        // TODO text based search functionality;
    }

    public void barcodeSearch(@Nullable String barcode) {
        if (barcode != null){
            LiveData<ProductItem> productItemLiveData = viewModel.getProductByBarcode(barcode);
            ProductItem productItem = productItemLiveData.getValue();
            loadFragment(getDetailFragment(productItem));
        }
    }

    public void onProductSelected(int id) {
        LiveData<ProductItem> productItemLiveData = viewModel.getProductById(id);
        ProductItem productItem = productItemLiveData.getValue();
        loadFragment(getDetailFragment(productItem));
    }

    private void startCameraForResult(int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, requestCode);
        } else {
            Toast.makeText(MainActivity.this,
                    R.string.no_camera_app_error,
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode) {

            // TODO Move most of this crap into BarcodeReader

            case BARCODE_SEARCH:
                if (resultCode == Activity.RESULT_OK) {
                    barcodeSearch(BarcodeReader.getBarcode(this, data));
                }
                break;

            case BARCODE_QUICK_CHANGE:
                if (resultCode == Activity.RESULT_OK) {
                    quickStockIncrement(BarcodeReader.getBarcode(this, data),
                            getMainFragment().getNumberPickerValue());
                }
                break;

            default:
                Toast.makeText(this, R.string.activity_result_error, Toast.LENGTH_LONG).show();
        }
    }

    void quickStockIncrement(String barcode, int value) {
        try {
            if (barcode != null) {
                ProductItem productItem = viewModel.getProductByBarcode(barcode).getValue();
                productItem.setCurrentStock(productItem.getCurrentStock() + value);
                viewModel.addProduct(productItem);
            }
        } catch (NullPointerException e) {
            Toast.makeText(this, R.string.product_not_found_error, Toast.LENGTH_LONG).show();
        }
    }
}
