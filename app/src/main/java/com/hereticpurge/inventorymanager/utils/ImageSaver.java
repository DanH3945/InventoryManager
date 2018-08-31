package com.hereticpurge.inventorymanager.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageSaver {

    private static final String TAG = "ImageSaver";

    public static int saveImage(Context context, Bitmap bitmap, String directoryName, String fileName){
        File directory = context.getDir(directoryName, Context.MODE_PRIVATE);
        File path = new File(directory, fileName);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 75, fileOutputStream);
        } catch (FileNotFoundException fnfe){
            Log.e(TAG, "saveImage: FileNotFoundException thrown on " + fileName);
            return -1; // Save failed
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException | NullPointerException e){
                e.printStackTrace();
            }
        }
        return 1; // Save success
    }
}
