package com.hereticpurge.inventorymanager.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.hereticpurge.inventorymanager.AnalyticsApplication;
import com.hereticpurge.inventorymanager.MainActivity;
import com.hereticpurge.inventorymanager.R;
import com.hereticpurge.inventorymanager.model.ProductViewModel;

public class RecyclerFragment extends Fragment {

    private static final String LAYOUT_STATE_KEY = "layoutStateKey";
    private static final int RESTORE_DELAY = 200;

    private ProductViewModel mViewModel;

    private RecyclerFragmentAdapter.RecyclerCallback mRecyclerCallback;

    private Tracker mTracker;

    private RecyclerView mRecyclerView;

    public static final String TAG = "RecyclerFragment";

    public static RecyclerFragment createInstance() {
        return new RecyclerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_fragment_layout, container, false);

        if (!MainActivity.isTablet) {
            showToolbar(view);
        }


        mRecyclerView = view.findViewById(R.id.recycler_view);
        RecyclerFragmentAdapter recyclerFragmentAdapter = new RecyclerFragmentAdapter(mRecyclerCallback, mViewModel);
        mRecyclerView.setAdapter(recyclerFragmentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SearchView searchView = view.findViewById(R.id.recycler_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                recyclerFragmentAdapter.filterProducts(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")) {
                    recyclerFragmentAdapter.filterProducts(null);
                    return true;
                }
                return false;
            }
        });

        mViewModel.getProductList()
                .observe(this, productItems -> recyclerFragmentAdapter.updateList(productItems));

        if (getActivity() != null) {
            // Google Analytics tracker.
            mTracker = ((AnalyticsApplication) getActivity().getApplication()).getDefaultTracker();
        }

        return view;
    }

    private void showToolbar(View view) {
        // This toolbar had to be done differently than the rest of the app.  Coordinator layouts
        // appear to disable the scrollToPosition method in recycler views so it was reseting the
        // recycler list when the view was destroyed and recreated without allowing me to reset the
        // correct list position.  As a result this is a simple average toolbar added specifically
        // to this fragment's class and layout files.
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setTitle(null);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(LAYOUT_STATE_KEY, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        // Puts a delay on setting the scroll position after orientation change to allow the LiveData
        // sources to update.
        if (savedInstanceState != null) {
            Parcelable state = savedInstanceState.getParcelable(LAYOUT_STATE_KEY);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mRecyclerView != null) {
                        mRecyclerView.getLayoutManager().onRestoreInstanceState(state);
                    }
                }
            }, RESTORE_DELAY);
        }
    }

    @Override
    public void onResume() {
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Set the callback for clicks to the main activity for handling navigation.
        mRecyclerCallback = (RecyclerFragmentAdapter.RecyclerCallback) getActivity();

        // Using get activity instead of the fragment itself to maintain observers.
        mViewModel = ViewModelProviders.of(getActivity())
                .get(ProductViewModel.class);

    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Destroy the reference to the main activity so we're not trying to callback on a destroyed
        // activity instance.
        mRecyclerCallback = null;
    }
}
