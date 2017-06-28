package com.androidtitan.culturedapp.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.CacheDockingStation;
import com.androidtitan.culturedapp.model.newyorktimes.Article;

import static com.androidtitan.culturedapp.common.Constants.CULTURED_PREFERENCES;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link ImageAppWidgetProviderConfigureActivity ImageAppWidgetProviderConfigureActivity}
 */
public class ImageAppWidgetProvider extends AppWidgetProvider {
    private final static String TAG = ImageAppWidgetProvider.class.getCanonicalName();

    private Context context;
    private AppWidgetManager appWidgetManager;
    private int[] appWidgetIds;

    private CacheDockingStation<String, String, Bitmap> cacheDockingStation;
    private Article providerArticle;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    //Enter relevant functionality for when the first widget is created
    //Think of this as your onCreate()
    @Override
    public void onEnabled(Context context) {
        this.context = context;
        providerArticle = updateViewWithCache();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e(TAG, "onUpdate()");

        this.appWidgetManager = appWidgetManager;
        this.appWidgetIds = appWidgetIds;

        if (appWidgetIds != null && appWidgetIds.length > 0) {
            // There may be multiple widgets active, so update all of them
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.e(TAG, "onDeleted()");

        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            //ImageAppWidgetProviderConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onDisabled(Context context) {
        Log.e(TAG, "onDisabled()");
        // Enter relevant functionality for when the last widget is disabled
    }

    private Article updateViewWithCache() {
        SharedPreferences preferences = context.getSharedPreferences(CULTURED_PREFERENCES, Context.MODE_PRIVATE);
        cacheDockingStation = new CacheDockingStation(context, CacheDockingStation.BITMAP_STATION_SIZE.PLANETARY);

        String articleTitle = preferences.getString(CacheDockingStation.TOP_ARTICLE_TITLE_CACHE, "");
        String articleFacet = preferences.getString(CacheDockingStation.TOP_ARTICLE_FACET_CACHE, "");
        Bitmap articleBitmap = cacheDockingStation.getBitmapFromMemCache(CacheDockingStation.TOP_ARTICLE_BITMAP_CACHE);

        providerArticle = new Article(articleTitle, articleFacet, articleBitmap);

        this.onUpdate(context, appWidgetManager, appWidgetIds);
        return providerArticle;
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        if (providerArticle == null) {
            return;
        }

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.image_app_widget_provider);
        //views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setTextViewText(R.id.titleTextView, providerArticle.getTitle());
        views.setTextViewText(R.id.facetTitleTextView, providerArticle.getGeoFacet().get(0).getFacetText());
        views.setImageViewBitmap(R.id.articleImageView, providerArticle.getImageBitmap());

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


}

