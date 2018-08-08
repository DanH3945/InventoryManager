package com.hereticpurge.inventorymanager.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM ProductItem")
    LiveData<List<ProductItem>> getProductList();

    @Query("SELECT * FROM ProductItem WHERE id = :id")
    LiveData<ProductItem> getProduct(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProductItem(ProductItem productItem);

    @Delete
    void deleteSingleProduct(ProductItem productItem);

    void deleteAllProducts();
}
