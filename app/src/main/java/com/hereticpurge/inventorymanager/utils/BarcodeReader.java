package com.hereticpurge.inventorymanager.utils;

import android.graphics.Bitmap;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import javax.annotation.Nullable;

public class BarcodeReader {

    public static @Nullable
    Result decodeBitmap(Bitmap image) throws NotFoundException {

        int[] pixelArray = new int[image.getWidth() * image.getHeight()];
        image.getPixels(pixelArray, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

        RGBLuminanceSource luminanceSource = new RGBLuminanceSource(image.getWidth(), image.getHeight(), pixelArray);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
        return new MultiFormatReader().decode(binaryBitmap);
    }
}
