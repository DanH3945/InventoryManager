package com.hereticpurge.inventorymanager.database;

public class DebugProduct {

    public static ProductItem getDebugProduct(){
        long barcode = Double.valueOf(Math.random() * 10000).longValue();
        long customId = Double.valueOf(Math.random() * 10000).longValue();
        String debugItem = "Debug Item";
        String cost = "4.00";
        String retail = "9.99";
        int currentStock = Double.valueOf(Math.random() * 1000).intValue();
        int targetStock = Double.valueOf(Math.random() * 1000).intValue();
        boolean tracked = Math.random() * 10 < 5;


        return new ProductItem(barcode, customId, debugItem, cost, retail, currentStock, targetStock, tracked);
    }
}
