package com.hereticpurge.inventorymanager.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

@Entity
public class ProductItem implements Parcelable {

    // Parcelable room data object.

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "barcode")
    private String barcode;

    @ColumnInfo(name = "custom_id")
    private String customId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "cost")
    private String cost;

    @ColumnInfo(name = "retail")
    private String retail;

    @ColumnInfo(name = "current_stock")
    public int currentStock;

    @ColumnInfo(name = "target_stock")
    public int targetStock;

    @ColumnInfo(name = "tracked")
    private boolean tracked;

    public ProductItem() {
        this("0000",
                "0000",
                "Default Product Name",
                "0.00",
                "0.00",
                0,
                0,
                false);
    }

    public ProductItem(String barcode,
                       String customId,
                       @NonNull String name,
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

    @Ignore
    protected ProductItem(Parcel in) {
        id = in.readInt();
        barcode = in.readString();
        customId = in.readString();
        name = in.readString();
        cost = in.readString();
        retail = in.readString();
        currentStock = in.readInt();
        targetStock = in.readInt();
        tracked = in.readByte() != 0;
    }

    @Ignore
    public ArrayList<String> getSearchTerms() {
        ArrayList<String> searchTerms = new ArrayList<>();
        searchTerms.add(getBarcode());
        searchTerms.add(customId);
        searchTerms.add(getName());
        return searchTerms;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
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

    @Ignore
    public static final Creator<ProductItem> CREATOR = new Creator<ProductItem>() {
        @Override
        public ProductItem createFromParcel(Parcel in) {
            return new ProductItem(in);
        }

        @Override
        public ProductItem[] newArray(int size) {
            return new ProductItem[size];
        }
    };

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(barcode);
        dest.writeString(customId);
        dest.writeString(name);
        dest.writeString(cost);
        dest.writeString(retail);
        dest.writeInt(currentStock);
        dest.writeInt(targetStock);
        dest.writeByte((byte) (tracked ? 1 : 0));
    }
}
