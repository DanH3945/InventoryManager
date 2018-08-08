package com.hereticpurge.inventorymanager.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ProductItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "barcode")
    public long barcode;

    @ColumnInfo(name = "custom_id")
    public long customId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "cost")
    public String cost;

    @ColumnInfo(name = "retail")
    public String retail;

    @ColumnInfo(name = "current_stock")
    public int currentStock;

    @ColumnInfo(name = "target_stock")
    public int targetStock;

    @ColumnInfo(name = "tracked")
    public boolean tracked;

    public ProductItem(long barcode,
                       long customId,
                       String name,
                       String cost,
                       String retail,
                       int currentStock,
                       int targetStock,
                       boolean tracked) {

        this.barcode = barcode;
        this.customId = customId;
        this.name = name;
        this.cost = cost;
        this.retail = retail;
        this.currentStock = currentStock;
        this.targetStock = targetStock;
        this.tracked = tracked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    public long getCustomId() {
        return customId;
    }

    public void setCustomId(long customId) {
        this.customId = customId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getRetail() {
        return retail;
    }

    public void setRetail(String retail) {
        this.retail = retail;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public int getTargetStock() {
        return targetStock;
    }

    public void setTargetStock(int targetStock) {
        this.targetStock = targetStock;
    }

    public boolean isTracked() {
        return tracked;
    }

    public void setTracked(boolean tracked) {
        this.tracked = tracked;
    }
}
