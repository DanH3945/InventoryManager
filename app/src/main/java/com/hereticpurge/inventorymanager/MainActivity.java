package com.hereticpurge.inventorymanager;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hereticpurge.inventorymanager.database.ProductDatabase;
import com.hereticpurge.inventorymanager.model.ProductViewModel;
import com.hereticpurge.inventorymanager.view.RecyclerFragment;

public class MainActivity extends AppCompatActivity {

    ProductViewModel mViewModel;

    RecyclerFragment mRecyclerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);

        mRecyclerFragment = RecyclerFragment.createFragment(new RecyclerFragment.RecyclerCallback() {
            @Override
            public void onItemSelected(int position) {
                MainActivity.this.onItemSelected(position);
            }
        });

    }

    @Override
    protected void onDestroy() {
        ProductDatabase.destroyInstance();
        super.onDestroy();
    }

    public void onItemSelected(int position) {
        // RecyclerView callback method to display the selected item.
    }
}
