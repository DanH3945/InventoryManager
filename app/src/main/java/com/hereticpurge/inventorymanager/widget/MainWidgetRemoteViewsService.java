package com.hereticpurge.inventorymanager.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.hereticpurge.inventorymanager.R;
import com.hereticpurge.inventorymanager.database.ProductDatabase;
import com.hereticpurge.inventorymanager.model.ProductItem;

import java.util.ArrayList;
import java.util.List;

public class MainWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new MainWidgetRemoteViewsFactory(getApplicationContext(), intent);
    }

    public class MainWidgetRemoteViewsFactory implements RemoteViewsFactory {

        private Context mContext;
        private ArrayList<Integer> mProductsNeedRestock;
        private int mAppWidgetId;
        private ProductDatabase mProductDatabase;
        private List<ProductItem> mProductItemList;

        MainWidgetRemoteViewsFactory(Context context, Intent intent) {
            this.mContext = context;
            this.mProductsNeedRestock = new ArrayList<>();
            this.mAppWidgetId = intent
                    .getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            this.mProductDatabase = ProductDatabase.getDatabase(mContext);
        }

        @Override
        public void onDataSetChanged() {
            mProductItemList = mProductDatabase.productDao().getProductListNonLive();
            mProductsNeedRestock.clear();
            for (int i = 0; i < mProductItemList.size(); i++){
                if (mProductItemList.get(i).isTracked() && mProductItemList.get(i).currentStock < mProductItemList.get(i).targetStock){
                    mProductsNeedRestock.add(i);
                }
            }
        }

        @Override
        public void onDestroy() {
            mProductDatabase.close();
        }

        @Override
        public int getCount() {
            return mProductsNeedRestock.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            int listPosition = mProductsNeedRestock.get(position);
            ProductItem productItem = mProductItemList.get(listPosition);

            RemoteViews baseView =
                    new RemoteViews(mContext.getPackageName(), R.layout.widget_layout_item);

            baseView.setTextViewText(R.id.widget_item_name_text, productItem.getName());

            baseView.setTextViewText(R.id.widget_item_current_stock_text,
                    Integer.toString(productItem.getCurrentStock()));

            baseView.setTextViewText(R.id.widget_item_target_stock_text,
                    Integer.toString(productItem.getTargetStock()));

            Intent intent = new Intent();
            intent.putExtra(MainAppWidgetProvider.EXTRA_LIST_POSITION, listPosition);

            baseView.setOnClickFillInIntent(R.id.widget_item_base, intent);

            return baseView;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
