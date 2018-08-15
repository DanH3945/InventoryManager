package com.hereticpurge.inventorymanager.model;

public class DebugProductItemFactory {

    public static ProductItem getDebugProduct() {
        long barcode = Double.valueOf(Math.random() * 10000).longValue();
        long customId = Double.valueOf(Math.random() * 10000).longValue();
        String imageLocation = "null"; // Placeholder
        String name = "Debug Item";
        String cost = "4.00";
        String retail = "9.99";
        int currentStock = Double.valueOf(Math.random() * 1000).intValue();
        int targetStock = Double.valueOf(Math.random() * 1000).intValue();
        boolean tracked = Math.random() * 10 < 5;


        return new ProductItem(barcode, customId, imageLocation, name, cost, retail, currentStock, targetStock, tracked);
    }
}
