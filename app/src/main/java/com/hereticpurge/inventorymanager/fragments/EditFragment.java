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
import android.widget.Switch;
import android.widget.Toast;

import com.hereticpurge.inventorymanager.R;
import com.hereticpurge.inventorymanager.model.ProductItem;
import com.hereticpurge.inventorymanager.model.ProductViewModel;

public class EditFragment extends Fragment {

    private ProductViewModel mViewModel;

    private ProductItem mProductItem;

    private EditText mName;
    private EditText mBarcode;
    private EditText mCustomId;
    private EditText mCost;
    private EditText mRetail;
    private EditText mCurrentStock;
    private EditText mTargetStock;

    private Switch mTrackSwitch;

    private Button mSaveButton;

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

        mName = view.findViewById(R.id.edit_product_name_et);
        mBarcode = view.findViewById(R.id.edit_product_barcode_et);
        mCustomId = view.findViewById(R.id.edit_product_customid_et);
        mCost = view.findViewById(R.id.edit_fragment_cost_et);
        mRetail = view.findViewById(R.id.edit_fragment_retail_et);
        mCurrentStock = view.findViewById(R.id.edit_fragment_stock_current_et);
        mTargetStock = view.findViewById(R.id.edit_fragment_stock_target_et);

        mTrackSwitch = view.findViewById(R.id.edit_fragment_track_switch_button);

        if (mProductItem != null) {
            mName.setText(mProductItem.getName());
            mBarcode.setText(mProductItem.getBarcode());
            mCustomId.setText(mProductItem.getCustomId());
            mCost.setText(mProductItem.getCost());
            mRetail.setText(mProductItem.getRetail());
            mCurrentStock.setText(String.valueOf(mProductItem.getCurrentStock()));
            mTargetStock.setText(String.valueOf(mProductItem.getTargetStock()));
            mTrackSwitch.setChecked(mProductItem.isTracked());
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

        try {
            mProductItem.setName(mName.getText().toString());
            mProductItem.setBarcode(mBarcode.getText().toString());
            mProductItem.setCustomId(mCustomId.getText().toString());
            mProductItem.setCost(mCost.getText().toString());
            mProductItem.setRetail(mRetail.getText().toString());
            mProductItem.setCurrentStock(Integer.parseInt(mCurrentStock.getText().toString()));
            mProductItem.setTargetStock(Integer.parseInt(mTargetStock.getText().toString()));
            mProductItem.setTracked(mTrackSwitch.isChecked());

            mViewModel.addProduct(mProductItem);

            try {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            getActivity().onBackPressed();
        } catch (NumberFormatException nfe){
            Toast.makeText(getContext(), R.string.number_error, Toast.LENGTH_LONG).show();
        }
    }
}
