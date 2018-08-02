package com.hereticpurge.inventorymanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";

    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Lots of TEXT data here.  Converting will need to be done where necessary before using
        // data returned from the cursor.
        final String CREATE_TABLE = "CREATE TABLE " +
                DBContract.InventoryTable.TABLE_NAME + " (" +
                DBContract.InventoryTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBContract.InventoryTable.PRODUCT_NAME + " TEXT NOT NULL, " +
                DBContract.InventoryTable.PRODUCT_DESCRIPTION_SHORT + " TEXT NOT NULL, " +
                DBContract.InventoryTable.PRODUCT_DESCRIPTION_LONG + " TEXT NOT NULL, " +
                DBContract.InventoryTable.PRODUCT_COST + " TEXT NOT NULL, " +
                DBContract.InventoryTable.PRODUCT_RETAIL_PRICE + " TEXT NOT NULL, " +
                DBContract.InventoryTable.CURRENT_STOCK + " TEXT NOT NULL, " +
                DBContract.InventoryTable.TARGET_STOCK + " TEXT NOT NULL, " +
                DBContract.InventoryTable.BARCODE_NUMBER + " TEXT NOT NULL, " +
                DBContract.InventoryTable.TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBContract.InventoryTable.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
