package com.hereticpurge.inventorymanager.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.hereticpurge.inventorymanager.AnalyticsApplication;
import com.hereticpurge.inventorymanager.R;
import com.hereticpurge.inventorymanager.model.ProductViewModel;

public class RecyclerFragment extends Fragment {

    private ProductViewModel mViewModel;

    private RecyclerFragmentAdapter.RecyclerCallback mRecyclerCallback;

    private Tracker mTracker;

    public static final String TAG = "RecyclerFragment";

    public static RecyclerFragment createInstance() {
        return new RecyclerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_fragment_layout, container, false);

        android.support.v7.widget.Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerFragmentAdapter recyclerFragmentAdapter = new RecyclerFragmentAdapter(mRecyclerCallback, mViewModel);
        recyclerView.setAdapter(recyclerFragmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mViewModel.getProductList()
                .observe(this, productItems -> recyclerFragmentAdapter.updateList(productItems));

        mTracker = ((AnalyticsApplication) getActivity().getApplication()).getDefaultTracker();

        return view;
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

        mRecyclerCallback = (RecyclerFragmentAdapter.RecyclerCallback) getActivity();

        mViewModel = ViewModelProviders.of(this)
                .get(ProductViewModel.class);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRecyclerCallback = null;
    }
}
