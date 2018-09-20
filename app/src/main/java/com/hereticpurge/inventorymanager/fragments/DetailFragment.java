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
    private static int sCurrentPosition;

    private ImageView mToolbarImageView;

    private DetailPagerAdapter mDetailPagerAdapter;

    private ProductViewModel mViewModel;

    private ViewPager mViewPager;

    private DetailEditButtonCallback mDetailEditButtonCallback;

    @NonNull
    public static DetailFragment createInstance(int position) {
        // The ViewPager position to bring into view when the fragment is loaded.
        sCurrentPosition = position;
        return new DetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment_pager_layout, container, false);

        // We don't load the toolbar if the app is displayed on a tablet or is in landscape.
        if (!MainActivity.isTablet) {
            initAppBar(view);
        }

        mViewPager = view.findViewById(R.id.detail_viewpager);
        mDetailPagerAdapter = new DetailPagerAdapter(getChildFragmentManager(), mViewPager);
        mViewPager.setAdapter(mDetailPagerAdapter);

        // FAB button for this screen grabs the currently displayed product item and sends it to the
        // callback.
        FloatingActionButton mFloatingActionButton = view.findViewById(R.id.main_fab);
        mFloatingActionButton.setOnClickListener(v -> {
            ProductItem productItem = ((DetailDisplayFragment) mDetailPagerAdapter
                    .getItem(mViewPager.getCurrentItem()))
                    .getDisplayProduct();
            mDetailEditButtonCallback.editButtonPressed(productItem);
        });

        // When the user changes the page we load the new image into the toolbar if it exists and
        // set the initial position variable to the new position. If the view
        // is destroyed and reloaded it will grab the initial position to set it's current location
        // so even when switching between fragments and landscape / portrait the state is maintained
        // as long as the app isn't fully destroyed.
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                // do nothing
            }

            @Override
            public void onPageSelected(int i) {

                try {

                    // Load the product image into the toolbar parallax display if necessary.
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
        // Helper method to initialize the app bar if it's needed.
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

        if (!MainActivity.isLandscape) {
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
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Set the callback to the main activity for use when an item selected for edit.
        mDetailEditButtonCallback = (DetailEditButtonCallback) getActivity();
        mViewModel = ViewModelProviders.of(this)
                .get(ProductViewModel.class);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Destroy the reference to the callback so we're not trying to callback to a dead activity.
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
            // creates instances of the display fragment views for individual items based on
            // list position.
            return DetailDisplayFragment.createInstance(mProductItemList.get(i));
        }

        @Override
        public int getCount() {
            return mProductItemList == null ? 0 : mProductItemList.size();
        }

        void updateList(List<ProductItem> productItemList) {
            // Called by room when the observed dataset changes.  Updates this classes internal
            // List of products and calls notifyDataSetChanged so the list of possible views will be
            // re-loaded.
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

        static DetailDisplayFragment createInstance(ProductItem productItem) {
            // Creation method that creates and instance of the fragment and pre-loads the given
            // product item's information into its views and returns it to be displayed in the view
            // pager.
            DetailDisplayFragment detailDisplayFragment = new DetailDisplayFragment();
            detailDisplayFragment.mProductItem = productItem;
            return detailDisplayFragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.detail_fragment_pager_item_layout, container, false);

            try {
                // if this is a reloaded fragment (after saveInstanceState is called for any reason) we
                // reload the saved data.
                mProductId = (int) savedInstanceState.get(PRODUCT_ID);
                mProductItem = mViewModel.getProductById(mProductId).getValue();
            } catch (NullPointerException npe) {
                // If it's a new view we set the id to the given product's id.
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
                // Google analytics tracker.
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
            // setup the analytics tracker
            mTracker.setScreenName(TAG);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            super.onResume();
        }

        ProductItem getDisplayProduct() {
            // Return the currently displayed product.
            return mProductItem;
        }

        private void updateProductItem(ProductItem productItem) {
            // Replace the current product with a new product and repopulate the display fields.
            mProductItem = productItem;
            mProductId = mProductItem.getId();
            populateFields();
        }

        private void populateFields() {

            mProductName.setText(mProductItem.getName());
            mProductName.setContentDescription(mProductItem.getName());

            mProductBarcode.setText(mProductItem.getBarcode());
            mProductBarcode.setContentDescription(mProductItem.getBarcode());

            mProductCustomId.setText(mProductItem.getCustomId());
            mProductCustomId.setContentDescription(mProductItem.getCustomId());

            mProductCost.setText(CurrencyUtils.addLocalCurrencySymbol(mProductItem.getCost()));
            mProductCost.setContentDescription(mProductItem.getCost());

            mProductRetail.setText(CurrencyUtils.addLocalCurrencySymbol(mProductItem.getRetail()));
            mProductRetail.setContentDescription(mProductItem.getRetail());

            mProductCurrentStock.setText(String.valueOf(mProductItem.getCurrentStock()));
            mProductCurrentStock.setContentDescription(String.valueOf(mProductItem.getCurrentStock()));

            mProductTargetStock.setText(String.valueOf(mProductItem.getTargetStock()));
            mProductTargetStock.setContentDescription(String.valueOf(mProductItem.getTargetStock()));

            if (getActivity() != null) {
                // setting the tracked switch to the correct state based on whether it's tracked
                // in the product item object.

                boolean isTracked = mProductItem.isTracked();

                String tracked = isTracked ?
                        getActivity().getResources().getString(R.string.detail_tracked_yes) :
                        getActivity().getResources().getString(R.string.detail_tracked_no);

                mProductTracked.setText(tracked);

                String trackedContentDesc = isTracked ?
                        getResources().getString(R.string.tracking_yes) :
                        getResources().getString(R.string.tracking_no);

                mProductTracked.setContentDescription(trackedContentDesc);
            }

            // load the image for the product into the small image view.
            CustomImageUtils.loadImage(getContext(), mProductItem.getName(), mProductImageViewSmall);

        }
    }

    public interface DetailEditButtonCallback {
        // Simple callback interface used by this fragment.
        void editButtonPressed(ProductItem productItem);
    }
}
