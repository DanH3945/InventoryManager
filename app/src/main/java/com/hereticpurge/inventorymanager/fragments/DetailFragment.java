package com.hereticpurge.inventorymanager.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hereticpurge.inventorymanager.R;
import com.hereticpurge.inventorymanager.model.ProductItem;
import com.hereticpurge.inventorymanager.model.ProductViewModel;
import com.hereticpurge.inventorymanager.utils.DebugAssistant;

import java.util.List;

public class DetailFragment extends Fragment {

    private DetailPagerAdapter mDetailPagerAdapter;

    private ProductViewModel mViewModel;

    private ViewPager mViewPager;

    private static int sInitialId;

    public static DetailFragment createInstance(int id) {
        sInitialId = id;
        DetailFragment detailFragment = new DetailFragment();
        return detailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment_pager_layout, container, false);

        mViewPager = view.findViewById(R.id.detail_viewpager);
        mDetailPagerAdapter = new DetailPagerAdapter(getChildFragmentManager(), sInitialId, mViewPager);
        mViewPager.setAdapter(mDetailPagerAdapter);

        mViewModel.getProductList()
                .observe(this, productItemList -> mDetailPagerAdapter.updateList(productItemList));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mViewModel = ViewModelProviders.of(this)
                .get(ProductViewModel.class);
    }

    private static class DetailPagerAdapter extends FragmentPagerAdapter {

        List<ProductItem> mProductItemList;
        ViewPager mParentViewPager;
        int mStartPosition;

        DetailPagerAdapter(FragmentManager fragmentManager, int startPosition, ViewPager parentViewPager) {
            super(fragmentManager);
            this.mStartPosition = startPosition;
            this.mParentViewPager = parentViewPager;
        }

        @Override
        public Fragment getItem(int i) {
            return DetailDisplayFragment.createInstance(mProductItemList.get(i));
        }

        @Override
        public int getCount() {
            return mProductItemList == null ? 0 : mProductItemList.size();
        }

        void updateList(List<ProductItem> productItemList){
            this.mProductItemList = productItemList;
            this.notifyDataSetChanged();

            if (mStartPosition != -1 && mProductItemList.size() > 0){
                mParentViewPager.setCurrentItem(mStartPosition);
                mStartPosition = -1;
            }
        }
    }

    public static class DetailDisplayFragment extends Fragment {

        private ProductItem mProductItem;

        TextView mProductName;
        TextView mProductBarcode;
        TextView mProductCurrentStock;

        public static DetailDisplayFragment createInstance(ProductItem productItem) {
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
