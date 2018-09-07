package com.hereticpurge.inventorymanager.utils;

import android.content.Context;

import com.hereticpurge.inventorymanager.R;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.License;
import de.psdev.licensesdialog.model.Notice;

public final class LicenseUtils {

    private LicenseUtils() {}

    public static void showLicenseDialogLicense(Context context){
        final String name = context.getResources().getString(R.string.license_license_dialog_name);
        final String url = context.getResources().getString(R.string.license_license_dialog_url);
        final String copyright = context.getResources().getString(R.string.license_license_dialog_copyright);
        final License license = new ApacheSoftwareLicense20();
        final Notice notice = new Notice(name, url, copyright, license);
        new LicensesDialog.Builder(context).setNotices(notice).build().showAppCompat();
    }

    public static void showZxingLicense(Context context){
        final String name = context.getResources().getString(R.string.license_zxing_name);
        final String url = context.getResources().getString(R.string.license_zxing_url);
        final String copyright = context.getResources().getString(R.string.license_zxing_copyright);
        final License license = new ApacheSoftwareLicense20();
        final Notice notice = new Notice(name, url, copyright, license);
        new LicensesDialog.Builder(context).setNotices(notice).build().showAppCompat();
    }

    public static void showPicassoLicense(Context context){
        final String name = context.getResources().getString(R.string.license_picasso_name);
        final String url = context.getResources().getString(R.string.license_picasso_url);
        final String copyright = context.getResources().getString(R.string.license_picasso_copyright);
        final License license = new ApacheSoftwareLicense20();
        final Notice notice = new Notice(name, url, copyright, license);
        new LicensesDialog.Builder(context).setNotices(notice).build().showAppCompat();
    }

}
