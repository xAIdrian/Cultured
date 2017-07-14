package com.androidtitan.culturedapp.widget.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.widget.FacetCollectionWidgetService;

/**
 * Created by Adrian Mohnacs on 7/1/17.
 */

public class FacetCollectionWidgetProvider extends AppWidgetProvider {
    private static final String TAG = FacetCollectionWidgetProvider.class.getSimpleName();

    public static final String TOAST_ACTION = "com.example.android.stackwidget.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM";

    public FacetCollectionWidgetProvider() {
        super();
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();

        if(action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            //refresh all of your widgets
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            manager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.facet_collection_listview);
        }
        // todo : this will be replaced at some point with actual functionality
        else if (action.equals(TOAST_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
        }

        super.onReceive(context, intent);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // update each of the app widgets with the remote adapter
        for (int i = 0; i < appWidgetIds.length; ++i) {
            /*
             Sets up the intent that points to the FacetCollectionWidgetService that will
             provide the views for this collection.
             */
            Intent intent = new Intent(context, FacetCollectionWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

            /*
             When intents are compared, the extras are ignored, so we need to embed the extras
             into the data so that the extras will not be ignored.
             */
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.facet_collection_widget_provider);
            rv.setRemoteAdapter(R.id.facet_collection_listview, intent);

            /*
             The empty view is displayed when the collection has no items. It should be a sibling
             of the collection view.
             */
            rv.setEmptyView(R.id.facet_collection_listview, R.id.empty_view);

            /*
             This section makes it possible for items to have individualized behavior.
             It does this by setting up a pending intent template. Individual items of a collection
             cannot set up their own pending intents. Instead, the collection as a whole sets
             up a pending intent template, and the individual items set a fillInIntent
             to create unique behavior on an item-by-item basis.
             */
            Intent intentAction = new Intent(context, FacetCollectionWidgetProvider.class);

            /*
             Set the action for the intent.
             When the user touches a particular view, it will have the effect of
             broadcasting TOAST_ACTION.
             */
            intentAction.setAction(FacetCollectionWidgetProvider.TOAST_ACTION);
            intentAction.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentAction,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            rv.setPendingIntentTemplate(R.id.facet_collection_listview, pendingIntent);


            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}
