package com.hereticpurge.inventorymanager.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.hereticpurge.inventorymanager.R;
import com.hereticpurge.inventorymanager.model.ProductItem;
import com.hereticpurge.inventorymanager.model.ProductViewModel;

public class EditFragment extends Fragment {

    ProductViewModel mViewModel;

    ProductItem mProductItem;

    Button mSaveButton;

    EditText mProductName;

    public static EditFragment createInstance(@Nullable ProductItem productItem){
        EditFragment editFragment = new EditFragment();
        editFragment.mProductItem = productItem;
        return editFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_fragment_layout, container, false);

        mSaveButton = view.findViewById(R.id.edit_save_button);
        mSaveButton.setOnClickListener(v -> doSave());

        mProductName = view.findViewById(R.id.edit_product_name_et);

        if (mProductItem != null) {
            mProductName.setText(mProductItem.getName());
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mViewModel = ViewModelProviders
                .of(this)
                .get(ProductViewModel.class);
    }

    private void doSave(){
        if (mProductItem == null){
            mProductItem = new ProductItem();
        }
        mProductItem.setName(mProductName.getText().toString());

        mViewModel.addProduct(mProductItem);

        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        getActivity().onBackPressed();
    }
}
