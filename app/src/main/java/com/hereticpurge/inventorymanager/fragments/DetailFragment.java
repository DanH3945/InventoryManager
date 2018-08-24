package com.hereticpurge.inventorymanager.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hereticpurge.inventorymanager.R;

public class DetailFragment extends Fragment {

    int initialId;

    public static DetailFragment createInstance(int id){
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.initialId = id;
        return detailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment_pager_layout, container, false);

        ViewPager viewPager= view.findViewById(R.id.detail_viewpager);
        viewPager.setAdapter(new DetailPagerAdapter(initialId, getContext()));

        return view;
    }
}
