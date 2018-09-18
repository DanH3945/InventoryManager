package com.hereticpurge.inventorymanager.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

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

    public static void saveImage(@Nullable Context context, Bitmap bitmap, String fileName) {
        // This will be the final target location for the image
        File target = null;

        // Set the default image quality
        int prefImageQuality = IMAGE_QUALITY_DEFAULT;

        try {
            // Initialize the target location
            target = new File(context.getExternalFilesDir(null), fileName);

            // Try to set the image quality from user selected preferences
            String key = context.getString(R.string.pref_image_quality_key);
            String stringRatio = PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
            if (stringRatio != null) {
                prefImageQuality = Integer.parseInt(stringRatio);
            }

        } catch (NullPointerException e) {
            Toast.makeText(context, R.string.external_files_error, Toast.LENGTH_LONG).show();
            return;
        } catch (ClassCastException | NumberFormatException e){
            // Failed to get the user entered preference so just let the default fall through
        }

        if (target != null) {
            // Do the actual saving of the image off thread.
            new SaveImageTask(bitmap, target, prefImageQuality).execute();
        }
    }

    public static void loadImage(@Nullable Context context, String filename, ImageView imageView) {
        // Loading an image.  Picasso handles the Async work here.
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

    public static class SaveImageTask extends AsyncTask<Void, Void, Void> {

        Bitmap mBitmap;
        File mTargetFile;
        int mImageQuality;


        SaveImageTask(Bitmap bitmap, File targetFile, int imageQuality) {
            mBitmap = bitmap;
            mTargetFile = targetFile;
            mImageQuality = imageQuality;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = new FileOutputStream(mTargetFile);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, mImageQuality, fileOutputStream);
            } catch (FileNotFoundException e) {
                return null;
            } finally {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }
}
