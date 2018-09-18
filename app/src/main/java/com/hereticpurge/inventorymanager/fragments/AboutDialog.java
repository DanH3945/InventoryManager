package com.hereticpurge.inventorymanager.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.widget.TextView;

import com.hereticpurge.inventorymanager.R;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.License;
import de.psdev.licensesdialog.model.Notice;

public class AboutDialog extends AppCompatDialogFragment {

    private static final String TAG = "AboutDialog";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog;
        if (getContext() != null) {
            // Wrapping the context in a theme to display the old style dialog box layout.
            // I just think this looks better for the layout of the app.
            ContextThemeWrapper themeWrapper = new ContextThemeWrapper(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
            dialog = new Dialog(themeWrapper);
        } else {
            return super.onCreateDialog(savedInstanceState);
        }

        dialog.setContentView(R.layout.about_dialog_layout);
        dialog.setTitle(R.string.about_title);

        TextView mVersionText = dialog.findViewById(R.id.about_version_tv);
        try {
            int versionCode = getContext()
                    .getPackageManager()
                    .getPackageInfo(getContext().getPackageName(), 0)
                    .versionCode;

            String string = getResources().getString(R.string.about_version) + versionCode;

            mVersionText.setText(string);
        } catch (PackageManager.NameNotFoundException nnfe) {
            Log.e(TAG, "onCreateDialog: Failed to get package name");
        }

        // Set onClick listeners for each of the 3rd party library names in the dialog
        // When clicked they load a second dialog via License Dialog library for each 3rd party
        // library used in the app.
        TextView mLicenseDialogNameText = dialog.findViewById(R.id.about_license_dialog_tv);
        mLicenseDialogNameText.setOnClickListener(v -> showLicenseDialogLicense(getContext()));

        TextView mZxingNameText = dialog.findViewById(R.id.about_zxing_tv);
        mZxingNameText.setOnClickListener(v -> showZxingLicense(getContext()));

        TextView mPicassoNameText = dialog.findViewById(R.id.about_picasso_tv);
        mPicassoNameText.setOnClickListener(v -> showPicassoLicense(getContext()));

        return dialog;
    }

    // Following 3 methods each load a specific dialog related to the library selected above.
    private void showLicenseDialogLicense(Context context) {
        final String name = context.getResources().getString(R.string.about_license_dialog_name);
        final String url = context.getResources().getString(R.string.about_license_dialog_url);
        final String copyright = context.getResources().getString(R.string.about_license_dialog_copyright);
        final License license = new ApacheSoftwareLicense20();
        final Notice notice = new Notice(name, url, copyright, license);
        new LicensesDialog.Builder(context).setNotices(notice).build().showAppCompat();
    }

    private void showZxingLicense(Context context) {
        final String name = context.getResources().getString(R.string.about_zxing_name);
        final String url = context.getResources().getString(R.string.about_zxing_url);
        final String copyright = context.getResources().getString(R.string.about_zxing_copyright);
        final License license = new ApacheSoftwareLicense20();
        final Notice notice = new Notice(name, url, copyright, license);
        new LicensesDialog.Builder(context).setNotices(notice).build().showAppCompat();
    }

    private void showPicassoLicense(Context context) {
        final String name = context.getResources().getString(R.string.about_picasso_name);
        final String url = context.getResources().getString(R.string.about_picasso_url);
        final String copyright = context.getResources().getString(R.string.about_picasso_copyright);
        final License license = new ApacheSoftwareLicense20();
        final Notice notice = new Notice(name, url, copyright, license);
        new LicensesDialog.Builder(context).setNotices(notice).build().showAppCompat();
    }
}
