package com.hereticpurge.inventorymanager.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.hereticpurge.inventorymanager.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public final class CustomImageUtils {

    // Utility class for saving and loading image files from the external files directory.

    public static final int SAVE_FAILED = -1;
    public static final int SAVE_SUCCESS = 1;
    private static final int IMAGE_QUALITY_DEFAULT = 75;

    private static final String TAG = "CustomImageUtils";

    private CustomImageUtils() {
    }

    public static int saveImage(@Nullable Context context, Bitmap bitmap, String fileName) {
        File target;
        try {
            target = new File(context.getExternalFilesDir(null), fileName);
        } catch (NullPointerException npe) {
            Log.e(TAG, "saveImage: Save Failed.  Null Context. ");
            return SAVE_FAILED;
        }

        FileOutputStream fileOutputStream = null;

        // set the default quality
        int prefImageQuality = IMAGE_QUALITY_DEFAULT;

        // try and get the user entered image compression ratio from shared preferences
        try {
            String key = context.getString(R.string.pref_image_quality_key);
            String stringRatio = PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
            if (stringRatio != null) {
                prefImageQuality = Integer.parseInt(stringRatio);
            }
        } catch (ClassCastException | NumberFormatException e) {
            // Failed to get the user entered preference so just let the default fall through
        }

        DebugAssistant.callCheck("Saving image with ratio " + Integer.toString(prefImageQuality));
        try {
            fileOutputStream = new FileOutputStream(target);
            bitmap.compress(Bitmap.CompressFormat.JPEG, prefImageQuality, fileOutputStream);
        } catch (FileNotFoundException fnfe) {
            Log.e(TAG, "saveImage: FileNotFoundException thrown on " + fileName);
            return SAVE_FAILED; // Save failed
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return SAVE_SUCCESS; // Save success
    }

    public static void loadImage(@Nullable Context context, String filename, ImageView imageView) {

        try {
            File file = new File(context.getExternalFilesDir(null), filename);
            Picasso.get().invalidate(file);
            Picasso.get().load(file).error(R.mipmap.error_24px).into(imageView);
        } catch (NullPointerException | IllegalArgumentException e) {
            // Most likely the user rotated the screen while picasso was attempting to load an image
            // into an image view that no long exists.
            Log.e(TAG, "loadImage: Load Failed.  Values went null before load finished.");
        }
    }
}
