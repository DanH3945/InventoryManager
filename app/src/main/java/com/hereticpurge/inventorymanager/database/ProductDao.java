package com.hereticpurge.inventorymanager.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.hereticpurge.inventorymanager.model.ProductItem;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM ProductItem")
    List<ProductItem> getProductListNonLive();

    @Query("SELECT * FROM ProductItem")
    LiveData<List<ProductItem>> getProductList();

    @Query("SELECT * FROM ProductItem WHERE id = :id")
    LiveData<ProductItem> getProductById(int id);

    @Query("SELECT * FROM ProductItem WHERE barcode = :barcode")
    LiveData<ProductItem> getProductByBarcode(String barcode);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProductItem(ProductItem productItem);

    @Delete
    void deleteSingleProduct(ProductItem productItem);

    @Query("DELETE FROM ProductItem")
    void deleteAllProducts();
}
