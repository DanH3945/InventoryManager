package com.hereticpurge.inventorymanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hereticpurge.inventorymanager.database.ProductDatabase;
import com.hereticpurge.inventorymanager.view.MainFragment;
import com.hereticpurge.inventorymanager.view.RecyclerFragment;
import com.hereticpurge.inventorymanager.view.RecyclerFragmentAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerFragment mRecyclerFragment;
    private MainFragment mMainFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (savedInstanceState == null){
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

    private void loadFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private Fragment getRecyclerFragment(){
        if (mRecyclerFragment == null){
            mRecyclerFragment = RecyclerFragment.createFragment(new RecyclerFragmentAdapter.RecyclerCallback() {
                @Override
                public void onItemSelected(int id) {
                    onProductSelected(id);
                }
            });
        }
        return mRecyclerFragment;
    }

    private Fragment getMainFragment(){
        if (mMainFragment == null){
            mMainFragment = MainFragment.createFragment(new MainFragment.MainFragmentButtonListener() {

                @Override
                public void onBrowseAllPressed() {
                    loadFragment(getRecyclerFragment());
                }

                @Override
                public void onNewItemPressed() {

                }

                @Override
                public void onBarcodeSearch(long barcode) {

                }

                @Override
                public void onQuickStockPressed(long barcode, int value) {

                }
            });
        }
        return mMainFragment;
    }

    public void onSearch(View view) {

    }
}
