package com.hereticpurge.inventorymanager.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.hereticpurge.inventorymanager.AnalyticsApplication;

public class PreferenceFragment extends Fragment {

    private Tracker mTracker;

    public static final String TAG = "PreferenceFragment";

    public static PreferenceFragment createInstance(){
        return new PreferenceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        mTracker = ((AnalyticsApplication) getActivity().getApplication()).getDefaultTracker();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
    }
}
