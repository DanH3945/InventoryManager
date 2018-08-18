package com.hereticpurge.inventorymanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.hereticpurge.inventorymanager.database.ProductDatabase;
import com.hereticpurge.inventorymanager.utils.BarcodeReader;
import com.hereticpurge.inventorymanager.view.MainFragment;
import com.hereticpurge.inventorymanager.view.RecyclerFragment;
import com.hereticpurge.inventorymanager.view.RecyclerFragmentAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerFragment mRecyclerFragment;
    private MainFragment mMainFragment;

    private static final int BARCODE_SEARCH = 100;
    private static final int BARCODE_QUICK_CHANGE = 200;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (savedInstanceState == null) {
            loadFragment(getMainFragment());
        }

    }

    @Override
    protected void onDestroy() {
        ProductDatabase.destroyInstance();
        super.onDestroy();
    }

    public void onProductSelected(int id) {
        // RecyclerView callback method to display the selected item.
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private Fragment getRecyclerFragment() {
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

    private Fragment getMainFragment() {
        if (mMainFragment == null) {
            mMainFragment = MainFragment.createFragment(new MainFragment.MainFragmentButtonListener() {

                @Override
                public void onBrowseAllPressed() {
                    loadFragment(getRecyclerFragment());
                }

                @Override
                public void onNewItemPressed() {

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
        onSearch(query);
    }

    public void onSearch(@Nullable String query) {
        // TODO search functionality
        Log.d(TAG, "onSearch: got search params " + query);

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
            case BARCODE_SEARCH:
                if (resultCode == Activity.RESULT_OK) {
                    onSearch(getBarcodeFromIntent(data));
                }
                break;
            case BARCODE_QUICK_CHANGE:
                if (resultCode == Activity.RESULT_OK) {

                }
                break;

            default:
                Toast.makeText(this, R.string.activity_result_error, Toast.LENGTH_LONG).show();
        }
    }

    private String getBarcodeFromIntent(Intent intent) {
        String returnString = "";

        try {
            Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
            Result barcodeResult = BarcodeReader.decodeBitmap(bitmap);
            returnString = barcodeResult.getText();
        } catch (NullPointerException npe) {
            Log.d(TAG, "getBarcodeFromIntent: Intent image unpacking error");
        } catch (NotFoundException nfe) {
            Toast.makeText(this, R.string.image_resolve_error, Toast.LENGTH_LONG).show();
        }

        return returnString;
    }
}
