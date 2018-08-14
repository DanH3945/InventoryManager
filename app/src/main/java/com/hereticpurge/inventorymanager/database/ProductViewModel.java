package com.hereticpurge.inventorymanager.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private ProductDatabase productDatabase;
    private final LiveData<List<ProductItem>> productList;

    public ProductViewModel(@NonNull Application application) {
        super(application);

        productDatabase = ProductDatabase.getDatabase(this.getApplication());

        productList = productDatabase.productDao().getProductList();
    }

    public LiveData<List<ProductItem>> getProductList() {
        return productList;
    }

    public LiveData<ProductItem> getSingleProduct(String id) {
        return productDatabase.productDao().getProduct(id);
    }

    public void addProduct(ProductItem productItem) {
        new addProductItemAsyncTask(productDatabase).execute(productItem);
    }

    public void deleteSingleProduct(ProductItem productItem) {
        new deleteSingleProductAsyncTask(productDatabase).execute(productItem);
    }

    public void deleteAllProducts() {
        new deleteAllProductsAsyncTask(productDatabase).execute();

    }

    private static class deleteSingleProductAsyncTask extends AsyncTask<ProductItem, Void, Void> {

        private ProductDatabase db;

        deleteSingleProductAsyncTask(ProductDatabase productDatabase) {
            db = productDatabase;
        }

        @Override
        protected Void doInBackground(ProductItem... productItems) {
            db.productDao().deleteSingleProduct(productItems[0]);
            return null;
        }
    }

    private static class deleteAllProductsAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProductDatabase db;

        deleteAllProductsAsyncTask(ProductDatabase productDatabase) {
            db = productDatabase;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db.productDao().deleteAllProducts();
            return null;
        }
    }

    private static class addProductItemAsyncTask extends AsyncTask<ProductItem, Void, Void> {

        ProductDatabase db;

        addProductItemAsyncTask(ProductDatabase productDatabase) {
            db = productDatabase;
        }

        @Override
        protected Void doInBackground(ProductItem... productItems) {
            db.productDao().insertProductItem(productItems[0]);
            return null;
        }
    }
}
