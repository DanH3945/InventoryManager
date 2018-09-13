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

    TextView mVersionText;
    TextView mLicenseDialogNameText;
    TextView mZxingNameText;
    TextView mPicassoNameText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog;
        if (getContext() != null) {
            ContextThemeWrapper themeWrapper = new ContextThemeWrapper(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
            dialog = new Dialog(themeWrapper);
        } else {
            return super.onCreateDialog(savedInstanceState);
        }

        dialog.setContentView(R.layout.about_dialog_layout);
        dialog.setTitle(R.string.about_title);

        mVersionText = dialog.findViewById(R.id.about_version_tv);
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

        mLicenseDialogNameText = dialog.findViewById(R.id.about_license_dialog_tv);
        mLicenseDialogNameText.setOnClickListener(v -> showLicenseDialogLicense(getContext()));

        mZxingNameText = dialog.findViewById(R.id.about_zxing_tv);
        mZxingNameText.setOnClickListener(v -> showZxingLicense(getContext()));

        mPicassoNameText = dialog.findViewById(R.id.about_picasso_tv);
        mPicassoNameText.setOnClickListener(v -> showPicassoLicense(getContext()));

        return dialog;
    }

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
