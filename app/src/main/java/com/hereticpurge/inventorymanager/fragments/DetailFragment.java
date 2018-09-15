package com.hereticpurge.inventorymanager.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.hereticpurge.inventorymanager.AnalyticsApplication;
import com.hereticpurge.inventorymanager.MainActivity;
import com.hereticpurge.inventorymanager.R;
import com.hereticpurge.inventorymanager.model.ProductItem;
import com.hereticpurge.inventorymanager.model.ProductViewModel;
import com.hereticpurge.inventorymanager.utils.AppbarStateChangeListener;
import com.hereticpurge.inventorymanager.utils.CurrencyUtils;
import com.hereticpurge.inventorymanager.utils.CustomImageUtils;

import java.util.List;

public class DetailFragment extends Fragment {

    public static final String TAG = "DetailFragment";
    protected static int sCurrentPosition;

    private ImageView mToolbarImageView;

    private FloatingActionButton mFloatingActionButton;

    private DetailPagerAdapter mDetailPagerAdapter;

    private ProductViewModel mViewModel;

    private ViewPager mViewPager;

    private DetailEditButtonCallback mDetailEditButtonCallback;

    public static DetailFragment createInstance(int position) {
        // The ViewPager position to bring into view when the fragment is loaded.
        sCurrentPosition = position;


        DetailFragment detailFragment = new DetailFragment();
        ;
        return detailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment_pager_layout, container, false);

        if (!MainActivity.isTablet | !MainActivity.isLandscape) {
            initAppBar(view);
        }

        mViewPager = view.findViewById(R.id.detail_viewpager);
        mDetailPagerAdapter = new DetailPagerAdapter(getChildFragmentManager(), mViewPager);
        mViewPager.setAdapter(mDetailPagerAdapter);

        mFloatingActionButton = view.findViewById(R.id.main_fab);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductItem productItem = ((DetailDisplayFragment) mDetailPagerAdapter
                        .getItem(mViewPager.getCurrentItem()))
                        .getDisplayProduct();
                mDetailEditButtonCallback.editButtonPressed(productItem);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                // do nothing
            }

            @Override
            public void onPageSelected(int i) {

                try {

                    if (!MainActivity.isTablet | !MainActivity.isLandscape) {
                        String productName = ((DetailDisplayFragment) mDetailPagerAdapter.getItem(i))
                                .getDisplayProduct()
                                .getName();
                        CustomImageUtils.loadImage(getContext(), productName, mToolbarImageView);
                    }

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

    private void initAppBar(View view) {
        mToolbarImageView = view.findViewById(R.id.toolbar_image_container);

        android.support.v7.widget.Toolbar toolbar = view.findViewById(R.id.toolbar);
        try {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (NullPointerException npe) {
            Log.e(TAG, "onCreateView: Failed to Load AppBar");
        }

        AppBarLayout appBarLayout = view.findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppbarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                TextView textView = view.findViewById(R.id.toolbar_text_view);
                if (state == State.COLLAPSED) {
                    textView.setVisibility(View.VISIBLE);
                } else if (state == State.EXPANDED) {
                    textView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDetailEditButtonCallback = (DetailEditButtonCallback) getActivity();
        mViewModel = ViewModelProviders.of(this)
                .get(ProductViewModel.class);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDetailEditButtonCallback = null;
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

        private static final String TAG = "DetailDisplayFragment";
        private static final String PRODUCT_ID = "productId";

        private ProductItem mProductItem;

        private int mProductId;

        private TextView mProductName;
        private TextView mProductBarcode;
        private TextView mProductCustomId;
        private TextView mProductCost;
        private TextView mProductRetail;
        private TextView mProductCurrentStock;
        private TextView mProductTargetStock;

        private TextView mProductTracked;

        ImageView mProductImageViewSmall;

        private Tracker mTracker;

        private ProductViewModel mViewModel;

        public static DetailDisplayFragment createInstance(ProductItem productItem) {
            DetailDisplayFragment detailDisplayFragment = new DetailDisplayFragment();
            detailDisplayFragment.mProductItem = productItem;
            return detailDisplayFragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.detail_fragment_pager_item_layout, container, false);

            if (savedInstanceState != null && savedInstanceState.get(PRODUCT_ID) != null) {
                mProductId = (int) savedInstanceState.get(PRODUCT_ID);
                mProductItem = mViewModel.getProductById(mProductId).getValue();
            } else {
                mProductId = mProductItem.getId();
            }

            mProductName = view.findViewById(R.id.detail_product_name_text);
            mProductBarcode = view.findViewById(R.id.detail_barcode_text);
            mProductCustomId = view.findViewById(R.id.detail_custom_id_text);
            mProductCost = view.findViewById(R.id.detail_cost_text);
            mProductRetail = view.findViewById(R.id.detail_retail_text);
            mProductCurrentStock = view.findViewById(R.id.detail_current_stock_text);
            mProductTargetStock = view.findViewById(R.id.detail_target_stock_text);
            mProductTracked = view.findViewById(R.id.detail_track_text);
            mProductImageViewSmall = view.findViewById(R.id.detail_image_small);

            if (getActivity() != null) {
                mTracker = ((AnalyticsApplication) getActivity().getApplication()).getDefaultTracker();
            }

            mViewModel.getProductById(mProductId).observe(this, productItem -> updateProductItem(productItem));

            return view;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);

            mViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        }

        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
            outState.putInt(PRODUCT_ID, mProductItem.getId());
            super.onSaveInstanceState(outState);
        }

        @Override
        public void onResume() {
            mTracker.setScreenName(TAG);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            super.onResume();
        }

        public ProductItem getDisplayProduct() {
            return mProductItem;
        }

        private void updateProductItem(ProductItem productItem) {
            mProductItem = productItem;
            populateFields();
        }

        private void populateFields() {

            mProductName.setText(mProductItem.getName());
            mProductBarcode.setText(mProductItem.getBarcode());
            mProductCustomId.setText(mProductItem.getCustomId());
            mProductCost.setText(CurrencyUtils.addLocalCurrencySymbol(mProductItem.getCost()));
            mProductRetail.setText(CurrencyUtils.addLocalCurrencySymbol(mProductItem.getRetail()));
            mProductCurrentStock.setText(String.valueOf(mProductItem.getCurrentStock()));
            mProductTargetStock.setText(String.valueOf(mProductItem.getTargetStock()));

            if (getActivity() != null) {
                mProductTracked.setText(String.valueOf(mProductItem.isTracked() ?
                        getActivity().getResources().getString(R.string.detail_tracked_yes) :
                        getActivity().getResources().getString(R.string.detail_tracked_no)));
            }

            CustomImageUtils.loadImage(getContext(), mProductItem.getName(), mProductImageViewSmall);

        }
    }

    public interface DetailEditButtonCallback {
        void editButtonPressed(ProductItem productItem);
    }
}
