package com.hereticpurge.inventorymanager.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.hereticpurge.inventorymanager.R;
import com.hereticpurge.inventorymanager.model.ProductItem;
import com.hereticpurge.inventorymanager.model.ProductViewModel;
import com.hereticpurge.inventorymanager.utils.BarcodeReader;
import com.hereticpurge.inventorymanager.utils.ImageSaver;
import com.squareup.picasso.Picasso;

import java.io.File;

public class EditFragment extends Fragment {

    private static final String TAG = "EditFragment";

    private ProductViewModel mViewModel;

    private ProductItem mProductItem;

    ImageButton mMainImageButton;
    ImageButton mBarcodeImageButton;

    ImageView mMainImageView;

    Bitmap mTempImage;

    private EditText mName;
    private EditText mBarcode;
    private EditText mCustomId;
    private EditText mCost;
    private EditText mRetail;
    private EditText mCurrentStock;
    private EditText mTargetStock;

    private Switch mTrackSwitch;

    private Button mSaveButton;

    private static final int MAIN_IMAGE_RESULT = 200;
    private static final int BARCODE_RESULT = 201;

    public static EditFragment createInstance(@Nullable ProductItem productItem){
        EditFragment editFragment = new EditFragment();
        editFragment.mProductItem = productItem;
        return editFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_fragment_layout, container, false);

        mMainImageButton = view.findViewById(R.id.edit_product_main_image_camera_button);
        mMainImageButton.setOnClickListener(v -> startCameraForResult(MAIN_IMAGE_RESULT));

        mBarcodeImageButton = view.findViewById(R.id.edit_product_barcode_camera_button);
        mBarcodeImageButton.setOnClickListener(v -> startCameraForResult(BARCODE_RESULT));

        mSaveButton = view.findViewById(R.id.edit_save_button);
        mSaveButton.setOnClickListener(v -> doSave());

        mMainImageView = view.findViewById(R.id.edit_product_image_iv);

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
            loadImage(mProductItem.getName());
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

    private void startCameraForResult(int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, requestCode);
        } else {
            Toast.makeText(getContext(),
                    R.string.no_camera_app_error,
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Object dataObject = null;

        if (data != null && data.hasExtra("data") && data.getExtras().containsKey("data")) {
            dataObject = data.getExtras().get("data");
        }

        switch (requestCode){

            case (MAIN_IMAGE_RESULT):
                if (dataObject instanceof Bitmap){
                    mTempImage = (Bitmap) dataObject;
                    mMainImageView.setImageBitmap(mTempImage);
                    break;
                }

            case (BARCODE_RESULT):
                if (dataObject instanceof Bitmap){
                    String barcode = BarcodeReader.getBarcode(getContext(), (Bitmap) dataObject);
                    checkProductNull();
                    mBarcode.setText(barcode);
                    break;
                }

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void loadImage(String fileName){
        try {
            Picasso.get().load(new File(getContext().getExternalFilesDir(null),
                    fileName)).into(mMainImageView);
        } catch (NullPointerException npe){
            Log.e(TAG, "loadImage: getExternalFilesDir() returned null");
        }
    }

    private void checkProductNull(){
        if (mProductItem == null){
            mProductItem = new ProductItem();
        }
    }

    private void doSave(){
        checkProductNull();
        try {
            mProductItem.setName(mName.getText().toString());
            mProductItem.setBarcode(mBarcode.getText().toString());
            mProductItem.setCustomId(mCustomId.getText().toString());
            mProductItem.setCost(mCost.getText().toString());
            mProductItem.setRetail(mRetail.getText().toString());
            mProductItem.setCurrentStock(Integer.parseInt(mCurrentStock.getText().toString()));
            mProductItem.setTargetStock(Integer.parseInt(mTargetStock.getText().toString()));
            mProductItem.setTracked(mTrackSwitch.isChecked());

            if (mTempImage != null){
                ImageSaver.saveImage(getContext(), mTempImage, mProductItem.getName());
            }

            mViewModel.addProduct(mProductItem);

            try {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            } catch (NullPointerException e) {
                Log.e(TAG, "doSave: Error Removing Soft Keyboard");
            }

            getActivity().onBackPressed();
        } catch (NumberFormatException nfe){
            Toast.makeText(getContext(), R.string.number_error, Toast.LENGTH_LONG).show();
        }
    }
}
