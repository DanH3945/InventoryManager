package com.hereticpurge.inventorymanager;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hereticpurge.inventorymanager.database.ProductDatabase;
import com.hereticpurge.inventorymanager.database.ProductViewModel;

public class MainActivity extends AppCompatActivity implements RecyclerCallback {

    ProductViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
    }

    @Override
    protected void onDestroy() {
        ProductDatabase.destroyInstance();
        super.onDestroy();
    }

    @Override
    public void onItemSelected(int position) {
        // RecyclerView callback method to display the selected item.
    }
}
