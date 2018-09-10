package com.hereticpurge.inventorymanager.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.hereticpurge.inventorymanager.R;
import com.hereticpurge.inventorymanager.model.ProductItem;

import java.util.List;

public class MainWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new MainWidgetRemoteViewsFactory(getApplicationContext(), intent);
    }

    public class MainWidgetRemoteViewsFactory implements RemoteViewsFactory {

        private Context mContext;
        private List<ProductItem> mTrackedProducts;
        private int mAppWidgetId;

        MainWidgetRemoteViewsFactory(Context context, Intent intent) {

            this.mContext = context;

            this.mTrackedProducts = intent
                    .getParcelableArrayListExtra(MainAppWidgetProvider.TRACKED_LIST_TAG);

            this.mAppWidgetId = intent
                    .getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mTrackedProducts.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.widget_layout_item);
            return view;
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
