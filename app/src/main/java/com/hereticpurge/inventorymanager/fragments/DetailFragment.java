package com.hereticpurge.inventorymanager.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hereticpurge.inventorymanager.R;
import com.hereticpurge.inventorymanager.model.ProductItem;
import com.hereticpurge.inventorymanager.model.ProductViewModel;
import com.hereticpurge.inventorymanager.utils.ImageUtils;

import java.util.List;

public class DetailFragment extends Fragment {

    private static final String TAG = "DetailFragment";
    protected static int sCurrentPosition;

    private ImageView mToolbarImageView;

    private FloatingActionButton mFloatingActionButton;

    private DetailPagerAdapter mDetailPagerAdapter;

    private ProductViewModel mViewModel;

    private ViewPager mViewPager;

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

        android.support.v7.widget.Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);

        mViewPager = view.findViewById(R.id.detail_viewpager);
        mDetailPagerAdapter = new DetailPagerAdapter(getChildFragmentManager(), mViewPager);
        mViewPager.setAdapter(mDetailPagerAdapter);

        mToolbarImageView = view.findViewById(R.id.toolbar_image_container);

        mFloatingActionButton = view.findViewById(R.id.main_fab);
        mFloatingActionButton.setOnClickListener(v -> {
            ProductItem productItem =
                    ((DetailDisplayFragment) mDetailPagerAdapter.getItem(mViewPager.getCurrentItem()))
                            .getDisplayProduct();
            mDetailEditButtonCallback.editButtonPressed(productItem);
        });


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                // do nothing
            }

            @Override
            public void onPageSelected(int i) {

                try {
                    String productName = ((DetailDisplayFragment) mDetailPagerAdapter.getItem(i))
                            .getDisplayProduct()
                            .getName();
                    ImageUtils.loadImage(getContext(), productName, mToolbarImageView);
                } catch (NullPointerException npe) {
                    Log.e(TAG, "onPageSelected: Null Product Reference");
                }

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
        private ViewPager mParentViewPager;

        DetailPagerAdapter(FragmentManager fragmentManager, ViewPager parentViewPager) {
            super(fragmentManager);
            mParentViewPager = parentViewPager;
        }

        @Override
        public Fragment getItem(int i) {
            return DetailDisplayFragment.createInstance(mProductItemList.get(i));
        }

        @Override
        public int getCount() {
            return mProductItemList == null ? 0 : mProductItemList.size();
        }

        void updateList(List<ProductItem> productItemList) {
            this.mProductItemList = productItemList;
            this.notifyDataSetChanged();

            mParentViewPager.setCurrentItem(sCurrentPosition);
        }
    }

    public static class DetailDisplayFragment extends Fragment {

        private ProductItem mProductItem;

        private TextView mProductName;
        private TextView mProductBarcode;
        private TextView mProductCustomId;
        private TextView mProductCost;
        private TextView mProductRetail;
        private TextView mProductCurrentStock;
        private TextView mProductTargetStock;

        private TextView mProductTracked;

        public static DetailDisplayFragment createInstance(ProductItem productItem) {
            DetailDisplayFragment detailDisplayFragment = new DetailDisplayFragment();
            detailDisplayFragment.mProductItem = productItem;
            return detailDisplayFragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.detail_fragment_pager_item_layout, container, false);

            mProductName = view.findViewById(R.id.detail_product_name_text);
            mProductName.setText(mProductItem.getName());

            mProductBarcode = view.findViewById(R.id.detail_barcode_text);
            mProductBarcode.setText(mProductItem.getBarcode());

            mProductCustomId = view.findViewById(R.id.detail_custom_id_text);
            mProductCustomId.setText(mProductItem.getCustomId());

            mProductCost = view.findViewById(R.id.detail_cost_text);
            mProductCost.setText(mProductItem.getCost());

            mProductRetail = view.findViewById(R.id.detail_retail_text);
            mProductRetail.setText(mProductItem.getRetail());

            mProductCurrentStock = view.findViewById(R.id.detail_current_stock_text);
            mProductCurrentStock.setText(String.valueOf(mProductItem.getCurrentStock()));

            mProductTargetStock = view.findViewById(R.id.detail_target_stock_text);
            mProductTargetStock.setText(String.valueOf(mProductItem.getTargetStock()));

            mProductTracked = view.findViewById(R.id.detail_track_text);
            mProductTracked.setText(String.valueOf(mProductItem.isTracked()));

            return view;
        }

        public ProductItem getDisplayProduct() {
            return mProductItem;
        }
    }

    public interface DetailEditButtonCallback {
        void editButtonPressed(ProductItem productItem);
    }
}
