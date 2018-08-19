package com.hereticpurge.inventorymanager.view;

import android.support.v4.app.Fragment;

import com.hereticpurge.inventorymanager.model.ProductItem;

public class DetailFragment extends Fragment {

    ProductItem productItem;

    public static DetailFragment createInstance(ProductItem productItem){
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.productItem = productItem;
        return detailFragment;
    }
}
