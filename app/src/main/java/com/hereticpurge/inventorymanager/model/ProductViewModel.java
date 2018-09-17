package com.hereticpurge.inventorymanager.model;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.hereticpurge.inventorymanager.MainActivity;
import com.hereticpurge.inventorymanager.R;
import com.hereticpurge.inventorymanager.database.ProductDatabase;
import com.hereticpurge.inventorymanager.widget.MainAppWidgetProvider;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private ProductDatabase productDatabase;

    public ProductViewModel(@NonNull Application application) {
        super(application);

        productDatabase = ProductDatabase.getDatabase(this.getApplication());
    }

    public LiveData<List<ProductItem>> getProductList() {
        return productDatabase.productDao().getProductList();
    }

    public LiveData<ProductItem> getProductById(int id) {
        return productDatabase.productDao().getProductById(id);
    }

    public LiveData<ProductItem> getProductByBarcode(String barcode) {
        return productDatabase.productDao().getProductByBarcode(barcode);
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
        // Async Task to delete a single item from the database.
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
        // Async Task to wipe the database.
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
        // Async Task to add a product to the database.
        ProductDatabase db;

        addProductItemAsyncTask(ProductDatabase productDatabase) {
            db = productDatabase;
        }

        @Override
        protected Void doInBackground(ProductItem... productItems) {
            db.productDao().insertProductItem(productItems[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // Notifying the app widget that some information has changed and it should update.
            Context context = MainActivity.getWeakContext().get();
            AppWidgetManager appWidgetManager =
                    AppWidgetManager.getInstance(context);

            ComponentName appWidget = new ComponentName(context, MainAppWidgetProvider.class.getName());

            int[] widgetIds = appWidgetManager.getAppWidgetIds(appWidget);

            appWidgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.widget_list_view);
        }
    }
}
