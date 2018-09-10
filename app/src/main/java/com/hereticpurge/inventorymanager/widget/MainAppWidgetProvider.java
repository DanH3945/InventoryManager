package com.hereticpurge.inventorymanager.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.AsyncTask;

import com.hereticpurge.inventorymanager.database.ProductDatabase;
import com.hereticpurge.inventorymanager.model.ProductItem;

import java.util.List;

public class MainAppWidgetProvider extends AppWidgetProvider{



    public void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int[] AppWidgetId,
                                List<ProductItem> productItems){


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        new DatabaseQueryAsyncTask(ProductDatabase.getDatabase(context), new WidgetCallback() {
            // Could be a lambda but I thought this was actually easier to read than the lambda notation.
            @Override
            public void setProductList(List<ProductItem> productItems) {
                updateAppWidget(context, appWidgetManager, appWidgetIds, productItems);
            }
        }).execute();
    }

    public static class DatabaseQueryAsyncTask extends AsyncTask<Void, Void, List<ProductItem>>{

        ProductDatabase mProductDatabase;
        WidgetCallback mWidgetCallback;

        DatabaseQueryAsyncTask(ProductDatabase productDatabase, WidgetCallback widgetCallback){
            this.mProductDatabase = productDatabase;
            this.mWidgetCallback = widgetCallback;
        }

        @Override
        protected List<ProductItem> doInBackground(Void... voids) {
            return mProductDatabase.productDao().getProductList().getValue();
        }

        @Override
        protected void onPostExecute(List<ProductItem> productItems) {
            super.onPostExecute(productItems);
            mWidgetCallback.setProductList(productItems);
        }
    }

    public interface WidgetCallback {
        void setProductList(List<ProductItem> productItems);
    }
}
