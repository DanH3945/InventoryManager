package com.hereticpurge.inventorymanager.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;

public class DetailPagerAdapter extends PagerAdapter {

    DetailPagerAdapter(int initialId, Context context){

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return false;
    }

    public interface DetailEditClickedListener {
        void editClicked(int id);
    }
}
