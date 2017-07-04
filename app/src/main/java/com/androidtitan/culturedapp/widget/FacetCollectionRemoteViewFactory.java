package com.androidtitan.culturedapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * Serves its purpose as the collection widget's adapter.  It connects the collection items with the data set.
 *
 * Created by Adrian Mohnacs on 7/1/17.
 */

public class FacetCollectionRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = FacetCollectionRemoteViewFactory.class.getSimpleName();

    private Context context;
    private int appWidgetId;

    public FacetCollectionRemoteViewFactory(Context context, Intent intent) {
        this.context = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);


    }

    @Override
    public void onCreate() {
        /*
        In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        for example downloading or creating content etc, should be deferred to onDataSetChanged()
        or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.

        for (int i = 0; i < mCount; i++) {
            mWidgetItems.add(new WidgetItem(i + "!"));
        }
        */
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
