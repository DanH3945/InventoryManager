package com.hereticpurge.inventorymanager.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.hereticpurge.inventorymanager.AnalyticsApplication;
import com.hereticpurge.inventorymanager.R;

public class PreferenceFragment extends PreferenceFragmentCompat {

    private Tracker mTracker;

    public static final String TAG = "PreferenceFragment";

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.preferences, s);
        mTracker = ((AnalyticsApplication) getActivity().getApplication()).getDefaultTracker();
    }

    @Override
    public void onResume() {
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
    }
}
