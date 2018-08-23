package com.hereticpurge.inventorymanager.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.hereticpurge.inventorymanager.R;

import javax.annotation.Nullable;

public class BarcodeReader {

    private static final String TAG = "BarcodeReader";

    private static final String INTENT_DATA_TAG = "data";

    private static @Nullable
    Result decodeBitmap(Bitmap image) throws NotFoundException {

        int[] pixelArray = new int[image.getWidth() * image.getHeight()];
        image.getPixels(pixelArray, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

        RGBLuminanceSource luminanceSource = new RGBLuminanceSource(image.getWidth(), image.getHeight(), pixelArray);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
        return new MultiFormatReader().decode(binaryBitmap);
    }

    public static @Nullable String getBarcode(Context context, Bitmap bitmap) {
        String returnString = null;

        try {
            returnString = decodeBitmap(bitmap).getText();
        } catch (NotFoundException nfe){
            Toast.makeText(context, R.string.image_resolve_error, Toast.LENGTH_LONG).show();
        }
        return returnString;
    }
}
