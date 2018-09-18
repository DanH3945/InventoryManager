package com.hereticpurge.inventorymanager.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.hereticpurge.inventorymanager.MainActivity;
import com.hereticpurge.inventorymanager.R;

public class MainAppWidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_LIST_POSITION = "extraProductItem";
    public static final String WIDGET_ITEM_SELECTED = "widgetItemSelected";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {

            Intent serviceIntent = new Intent(context, MainWidgetRemoteViewsService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            Intent intent = new Intent(context, MainActivity.class);
            intent.setAction(WIDGET_ITEM_SELECTED);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            remoteViews.setRemoteAdapter(R.id.widget_list_view, serviceIntent);
            remoteViews.setEmptyView(R.id.widget_list_view, R.id.empty_view);
            remoteViews.setPendingIntentTemplate(R.id.widget_list_view, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    public static void notifyWidgets() {
        // Notifying the app widget that some information has changed and it should update.
        Context context = MainActivity.getWeakContext().get();
        AppWidgetManager appWidgetManager =
                AppWidgetManager.getInstance(context);

        ComponentName appWidget = new ComponentName(context, MainAppWidgetProvider.class.getName());

        int[] widgetIds = appWidgetManager.getAppWidgetIds(appWidget);

        appWidgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.widget_list_view);
    }
}
