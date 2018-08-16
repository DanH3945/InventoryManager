package com.hereticpurge.inventorymanager;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hereticpurge.inventorymanager.database.ProductDatabase;
import com.hereticpurge.inventorymanager.model.ProductViewModel;
import com.hereticpurge.inventorymanager.view.MainFragment;
import com.hereticpurge.inventorymanager.view.RecyclerFragment;

public class MainActivity extends AppCompatActivity {

    private ProductViewModel mViewModel;

    private RecyclerFragment mRecyclerFragment;
    private MainFragment mMainFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);

        if (savedInstanceState == null){
            loadFragment(getMainFragment());
        }

    }

    @Override
    protected void onDestroy() {
        ProductDatabase.destroyInstance();
        super.onDestroy();
    }

    public void onProductSelected(int position) {
        // RecyclerView callback method to display the selected item.
    }

    private void initUI(){

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
            mRecyclerFragment = RecyclerFragment.createFragment(new RecyclerFragment.RecyclerCallback() {
                @Override
                public void onItemSelected(int position) {
                    onProductSelected(position);
                }
            });
        }
        return mRecyclerFragment;
    }

    private Fragment getMainFragment(){
        if (mMainFragment == null){
            mMainFragment = MainFragment.createFragment();
        }
        return mMainFragment;
    }
}
