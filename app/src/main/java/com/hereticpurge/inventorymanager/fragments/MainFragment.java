package com.hereticpurge.inventorymanager.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.hereticpurge.inventorymanager.R;

public class MainFragment extends Fragment implements View.OnClickListener{

    private NumberPicker mNumberPicker;
    private MainFragmentButtonListener mMainFragmentButtonListener;

    public static MainFragment createFragment(MainFragmentButtonListener mainFragmentButtonListener){
        MainFragment mainFragment = new MainFragment();
        mainFragment.mMainFragmentButtonListener = mainFragmentButtonListener;
        return mainFragment;
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

        mNumberPicker = view.findViewById(R.id.main_fragment_number_picker);
        mNumberPicker.setMinValue(0);
        mNumberPicker.setMaxValue(999);
        mNumberPicker.setValue(10);

        view.findViewById(R.id.main_fragment_btn_browse_all).setOnClickListener(this);
        view.findViewById(R.id.main_fragment_btn_browse_barcode).setOnClickListener(this);
        view.findViewById(R.id.main_fragment_btn_new_product).setOnClickListener(this);
        view.findViewById(R.id.main_fragment_btn_quick_change).setOnClickListener(this);

        return view;
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

    public int getNumberPickerValue(){
        return mNumberPicker.getValue();
    }

    public interface MainFragmentButtonListener{
        void onBrowseAllPressed();
        void onNewItemPressed();
        void onBarcodeSearch();
        void onQuickChangePressed();
    }
}
