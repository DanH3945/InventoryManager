package com.hereticpurge.inventorymanager.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import com.hereticpurge.inventorymanager.R;
import com.hereticpurge.inventorymanager.database.ProductDatabase;
import com.hereticpurge.inventorymanager.model.ProductItem;

import java.util.ArrayList;
import java.util.List;

public class MainAppWidgetProvider extends AppWidgetProvider {

    public static final String TRACKED_LIST_TAG = "trackedProductsKey";

    public void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int[] AppWidgetId,
                                ArrayList<ProductItem> trackedProducts) {

        for (int appWidgetId : AppWidgetId) {

            Intent serviceIntent = new Intent(context, MainWidgetRemoteViewsService.class);
            serviceIntent.putParcelableArrayListExtra(TRACKED_LIST_TAG, trackedProducts);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            remoteViews.setRemoteAdapter(R.id.widget_list_view, serviceIntent);
            remoteViews.setEmptyView(R.id.widget_list_view, R.id.empty_view);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        new DatabaseQueryAsyncTask(ProductDatabase.getDatabase(context), new WidgetCallback() {
            // Could be a lambda but I thought this was actually easier to read than the lambda notation.
            // This could also be moved into the remote view adapter.  Not sure what the best practice
            // is here...
            @Override
            public void setProductList(ArrayList<ProductItem> trackedProducts) {
                updateAppWidget(context, appWidgetManager, appWidgetIds, trackedProducts);
            }
        }).execute();
    }

    public static class DatabaseQueryAsyncTask extends AsyncTask<Void, Void, ArrayList<ProductItem>> {

        ProductDatabase mProductDatabase;
        WidgetCallback mWidgetCallback;

        DatabaseQueryAsyncTask(ProductDatabase productDatabase, WidgetCallback widgetCallback) {
            this.mProductDatabase = productDatabase;
            this.mWidgetCallback = widgetCallback;
        }

        @Override
        protected ArrayList<ProductItem> doInBackground(Void... voids) {
            List<ProductItem> productItems = mProductDatabase.productDao().getProductListNonLive();
            ArrayList<ProductItem> trackedProducts = new ArrayList<>();

            for (ProductItem productItem : productItems) {
                if (productItem.isTracked()) {
                    trackedProducts.add(productItem);
                }
            }

            return trackedProducts;
        }

        @Override
        protected void onPostExecute(ArrayList<ProductItem> productItems) {
            super.onPostExecute(productItems);
            mWidgetCallback.setProductList(productItems);
        }
    }

    public interface WidgetCallback {
        void setProductList(ArrayList<ProductItem> trackedProducts);
    }
}
