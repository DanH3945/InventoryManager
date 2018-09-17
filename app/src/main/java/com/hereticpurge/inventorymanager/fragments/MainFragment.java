package com.hereticpurge.inventorymanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.hereticpurge.inventorymanager.AnalyticsApplication;
import com.hereticpurge.inventorymanager.R;

public class MainFragment extends Fragment implements View.OnClickListener {

    private NumberPicker mNumberPicker;
    private MainFragmentButtonListener mMainFragmentButtonListener;

    private Tracker mTracker;

    public static final String TAG = "MainFragment";

    public static MainFragment createInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_layout, container, false);

        android.support.v7.widget.Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
        collapsingToolbarLayout.setLayoutParams(params);


        mNumberPicker = view.findViewById(R.id.main_fragment_number_picker);
        mNumberPicker.setMinValue(0);
        mNumberPicker.setMaxValue(999);
        mNumberPicker.setValue(10);

        view.findViewById(R.id.main_fragment_btn_browse_all).setOnClickListener(this);
        view.findViewById(R.id.main_fragment_btn_browse_barcode).setOnClickListener(this);
        view.findViewById(R.id.main_fragment_btn_new_product).setOnClickListener(this);
        view.findViewById(R.id.main_fragment_btn_quick_change).setOnClickListener(this);

        mTracker = ((AnalyticsApplication) getActivity().getApplication()).getDefaultTracker();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainFragmentButtonListener = (MainFragmentButtonListener) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMainFragmentButtonListener = null;
    }

    @Override
    public void onResume() {
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.main_fragment_btn_browse_all:
                mMainFragmentButtonListener.onBrowseAllPressed();
                break;

            case R.id.main_fragment_btn_browse_barcode:
                mMainFragmentButtonListener.onBarcodeSearch();
                break;

            case R.id.main_fragment_btn_new_product:
                mMainFragmentButtonListener.onNewItemPressed();
                break;

            case R.id.main_fragment_btn_quick_change:
                mMainFragmentButtonListener.onQuickChangePressed();
        }
    }

    public int getNumberPickerValue() {
        return mNumberPicker.getValue();
    }

    public interface MainFragmentButtonListener {
        void onBrowseAllPressed();

        void onNewItemPressed();

        void onBarcodeSearch();

        void onQuickChangePressed();
    }
}
