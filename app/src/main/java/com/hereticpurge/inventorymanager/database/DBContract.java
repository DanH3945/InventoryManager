package com.hereticpurge.inventorymanager.database;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DBContract {

    private static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.hereticpurge.inventorymanager";
    private static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    public static final String PATH_INVENTORY = "inventory";

    private DBContract(){

    }

    public static final class InventoryTable implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_INVENTORY)
                .build();

        public static final String TABLE_NAME = "inventory";
        public static final String PRODUCT_NAME = "name";
        public static final String PRODUCT_DESCRIPTION_SHORT = "descriptionshort";
        public static final String PRODUCT_DESCRIPTION_LONG = "descriptionlong";
        public static final String PRODUCT_COST = "cost";
        public static final String PRODUCT_RETAIL_PRICE = "retailprice";
        public static final String CURRENT_STOCK = "currentstock";
        public static final String TARGET_STOCK = "targetstock";
        public static final String BARCODE_NUMBER = "barcodenumber";
        public static final String TIMESTAMP = "timestamp";

    }
}
