package com.hereticpurge.inventorymanager.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageSaver {

    public static final int SAVE_FAILED = -1;
    public static final int SAVE_SUCCESS = 1;

    private static final String TAG = "ImageSaver";

    public static int saveImage(@Nullable Context context, Bitmap bitmap, String fileName){
        File target;
        try {
            target = new File(context.getExternalFilesDir(null), fileName);
        } catch (NullPointerException npe){
            return SAVE_FAILED; // Save failed. Context null.  Probably the context was destroyed
                                // before this function was called as part of an activity result.
        }

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(target);
            bitmap.compress(Bitmap.CompressFormat.PNG, 75, fileOutputStream);
        } catch (FileNotFoundException fnfe){
            Log.e(TAG, "saveImage: FileNotFoundException thrown on " + fileName);
            return SAVE_FAILED; // Save failed
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException | NullPointerException e){
                e.printStackTrace();
            }
        }
        return SAVE_SUCCESS; // Save success
    }
}
