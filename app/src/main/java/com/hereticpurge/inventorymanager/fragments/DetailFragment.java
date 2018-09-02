package com.hereticpurge.inventorymanager.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hereticpurge.inventorymanager.R;
import com.hereticpurge.inventorymanager.model.ProductItem;
import com.hereticpurge.inventorymanager.model.ProductViewModel;

import java.util.List;

public class DetailFragment extends Fragment {

    private DetailPagerAdapter mDetailPagerAdapter;

    private ProductViewModel mViewModel;

    private ViewPager mViewPager;

    protected static int sCurrentPosition;

    private DetailEditButtonCallback mDetailEditButtonCallback;

    public static DetailFragment createInstance(int position, DetailEditButtonCallback detailEditButtonCallback) {
        // The ViewPager position to bring into view when the fragment is loaded.
        sCurrentPosition = position;


        DetailFragment detailFragment = new DetailFragment();
        detailFragment.mDetailEditButtonCallback = detailEditButtonCallback;
        return detailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment_pager_layout, container, false);

        mViewPager = view.findViewById(R.id.detail_viewpager);
        mDetailPagerAdapter = new DetailPagerAdapter(getChildFragmentManager(), mDetailEditButtonCallback, mViewPager);
        mViewPager.setAdapter(mDetailPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                // do nothing
            }

            @Override
            public void onPageSelected(int i) {
                // Setting the current position to the currently selected page so that when
                // the fragment is reloaded the last seen page will be brought into view.
                sCurrentPosition = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                // do nothing
            }
        });

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

        private List<ProductItem> mProductItemList;
        private DetailEditButtonCallback mDetailEditButtonCallback;
        private ViewPager mParentViewPager;

        DetailPagerAdapter(FragmentManager fragmentManager, DetailEditButtonCallback detailEditButtonCallback, ViewPager parentViewPager) {
            super(fragmentManager);
            mParentViewPager = parentViewPager;
            mDetailEditButtonCallback = detailEditButtonCallback;
        }

        @Override
        public Fragment getItem(int i) {
            return DetailDisplayFragment.createInstance(mProductItemList.get(i), mDetailEditButtonCallback);
        }

        @Override
        public int getCount() {
            return mProductItemList == null ? 0 : mProductItemList.size();
        }

        void updateList(List<ProductItem> productItemList){
            this.mProductItemList = productItemList;
            this.notifyDataSetChanged();

            mParentViewPager.setCurrentItem(sCurrentPosition);
        }
    }

    public static class DetailDisplayFragment extends Fragment {

        private ProductItem mProductItem;

        private TextView mProductName;
        private TextView mProductBarcode;
        private TextView mProductCurrentStock;
        private Button mEditButton;
        private DetailEditButtonCallback mDetailEditButtonCallback;

        public static DetailDisplayFragment createInstance(ProductItem productItem, DetailEditButtonCallback detailEditButtonCallback) {
            DetailDisplayFragment detailDisplayFragment = new DetailDisplayFragment();
            detailDisplayFragment.mDetailEditButtonCallback = detailEditButtonCallback;
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

            mEditButton = view.findViewById(R.id.detail_button_edit);
            mEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDetailEditButtonCallback.editButtonPressed(mProductItem);
                }
            });

            return view;
        }
    }

    public interface DetailEditButtonCallback {
        void editButtonPressed(ProductItem productItem);
    }
}
