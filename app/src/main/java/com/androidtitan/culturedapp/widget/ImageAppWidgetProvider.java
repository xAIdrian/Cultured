package com.androidtitan.culturedapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.main.CulturedApp;
import com.androidtitan.culturedapp.main.toparticle.TopArticleMvp;
import com.androidtitan.culturedapp.main.toparticle.TopArticleProvider;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Multimedium;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link ImageAppWidgetProviderConfigureActivity ImageAppWidgetProviderConfigureActivity}
 */
public class ImageAppWidgetProvider extends AppWidgetProvider implements TopArticleMvp.Provider.CallbackListener {
    private final static String TAG = ImageAppWidgetProvider.class.getCanonicalName();

    private Context context;
    private AppWidgetManager appWidgetManager;
    private int[] appWidgetIds;

    private TopArticleProvider provider;
    private Article providerArticle;

    SimpleTarget mediaGlideTarget;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    //Enter relevant functionality for when the first widget is created
    //Think of this as your onCreate()
    @Override
    public void onEnabled(Context context) {
        this.context = context;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e(TAG, "onUpdate()");
        provider = TopArticleProvider.getInstance(CulturedApp.getAppContext());
        if(providerArticle == null) {
            provider.fetchArticles(this);
        }

        this.appWidgetManager = appWidgetManager;
        this.appWidgetIds = appWidgetIds;

        if (appWidgetIds != null && appWidgetIds.length > 0) {
            // There may be multiple widgets active, so update all of them
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(appWidgetId, providerArticle);
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

    private void updateAppWidget(int appWidgetId, Article providerArticle) {

        if (providerArticle == null) {
            return;
        }

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(CulturedApp.getAppContext().getPackageName(), R.layout.image_app_widget_provider);

        views.setTextViewText(R.id.titleTextView, providerArticle.getTitle());
        if (providerArticle.getGeoFacet() != null && providerArticle.getGeoFacet().size() > 0) {
            views.setTextViewText(R.id.facetTitleTextView, providerArticle.getGeoFacet().get(0).getFacetText());
        }

        if (providerArticle.getMultimedia() != null && providerArticle.getMultimedia().size() > 0) {
            Multimedium imageMedia = providerArticle.getMultimedia().get(0);

            if(imageMedia != null) {

                mediaGlideTarget = new SimpleTarget<Bitmap>(imageMedia.getWidth(), imageMedia.getHeight()) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        views.setImageViewBitmap(R.id.articleImageView, resource);
                        appWidgetManager.updateAppWidget(appWidgetId, views); //todo: maybe we want to block the update until this process is finished
                    }
                };

                Glide.with(CulturedApp.getAppContext())
                        .load(imageMedia.getUrl())
                        .asBitmap()
                        .into(mediaGlideTarget);
            }
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onConstructionComplete(ArrayList<Article> articleArrayList) {

        if (articleArrayList.size() > 0) {
            providerArticle = articleArrayList.get(3);
            this.onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void cursorDataNotAvailable() {
        //no-op
    }

    @Override
    public void cursorDataEmpty() {
        //no-op
    }

    /*
    private void getArticleBitmap(Multimedium cachingMedia) {
        final Bitmap[] bitmap = new Bitmap[1];

        if(cachingMedia != null) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        bitmap[0] = Glide.with(CulturedApp.getAppContext())
                                .load(cachingMedia.getUrl())
                                .asBitmap()
                                .into(cachingMedia.getWidth(), cachingMedia.getHeight())
                                .get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return bitmap[0];
    }
    */
}

