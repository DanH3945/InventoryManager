package com.hereticpurge.inventorymanager.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.hereticpurge.inventorymanager.R;

public class MainFragment extends Fragment{

    NumberPicker numberPicker;

    public static MainFragment createFragment(){
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_layout, container, false);

        numberPicker = view.findViewById(R.id.main_fragment_number_picker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(999);
        numberPicker.setValue(10);

        return view;
    }
}
