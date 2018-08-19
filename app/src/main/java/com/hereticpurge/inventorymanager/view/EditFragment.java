package com.hereticpurge.inventorymanager.view;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.hereticpurge.inventorymanager.model.ProductItem;

public class EditFragment extends Fragment {

    ProductItem productItem;

    public static EditFragment createInstance(@Nullable ProductItem productItem){
        EditFragment editFragment = new EditFragment();
        editFragment.productItem = productItem;
        return editFragment;
    }
}
