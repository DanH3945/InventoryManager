package com.hereticpurge.inventorymanager.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hereticpurge.inventorymanager.R;
import com.hereticpurge.inventorymanager.model.ProductItem;
import com.hereticpurge.inventorymanager.model.ProductViewModel;
import com.hereticpurge.inventorymanager.utils.DebugAssistant;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DetailFragment extends Fragment {

    static int initialId;

    DetailPagerAdapter mDetailPagerAdapter;
    FragmentManager fragmentManager;

    public static DetailFragment createInstance(int id, FragmentManager fragmentManager){
        initialId = id;
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.fragmentManager = fragmentManager;
        return detailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment_pager_layout, container, false);

        ViewPager viewPager = view.findViewById(R.id.detail_viewpager);
        mDetailPagerAdapter = new DetailPagerAdapter(fragmentManager);
        viewPager.setAdapter(mDetailPagerAdapter);
        viewPager.setLayoutDirection(ViewPager.LAYOUT_DIRECTION_LTR);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ViewModelProviders.of(this)
                .get(ProductViewModel.class)
                .getProductList()
                .observe(this, productItemList -> mDetailPagerAdapter.updateList(productItemList));
    }

    private static class DetailPagerAdapter extends FragmentStatePagerAdapter {

        List<ProductItem> productItemList;

        DetailPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int i) {
            DebugAssistant.callCheck("getItemCalled with param: " + i);
            return DetailDisplayFragment.createInstance(productItemList.get(i));
        }

        @Override
        public int getCount() {
            return productItemList == null ? 0 : productItemList.size();
        }

        void updateList(List<ProductItem> productItemList){
            this.productItemList = productItemList;
            this.notifyDataSetChanged();
        }
    }

    public static class DetailDisplayFragment extends Fragment {

        private ProductItem mProductItem;

        TextView mProductName;
        TextView mProductBarcode;
        TextView mProductCurrentStock;

        public static DetailDisplayFragment createInstance(ProductItem productItem){
            DetailDisplayFragment detailDisplayFragment = new DetailDisplayFragment();
            detailDisplayFragment.mProductItem = productItem;
            return detailDisplayFragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.detail_fragment_pager_item_layout, container, false);

            mProductName = view.findViewById(R.id.detail_product_name);
            mProductBarcode = view.findViewById(R.id.detail_product_barcode);
            mProductCurrentStock = view.findViewById(R.id.detail_product_current_stock);

            mProductName.setText(mProductItem.getName());
            mProductBarcode.setText(mProductItem.getBarcode());
            mProductCurrentStock.setText(Integer.toString(mProductItem.getCurrentStock()));

            return view;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
        }
    }
}
