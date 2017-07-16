package com.androidtitan.culturedapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.main.CulturedApp;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Multimedium;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by Adrian Mohnacs on 7/14/17.
 */

public class WidgetSharedUpdater {

    public static void updateAppWidget(RemoteViews views, AppWidgetManager appWidgetManager, int appWidgetId,  Article providerArticle) {
        updateAppWidget(views, appWidgetManager, appWidgetId, null, providerArticle);
    }

    public static void updateAppWidget(RemoteViews views, AppWidgetManager appWidgetManager, ComponentName appWidgetComponent,  Article providerArticle) {
        updateAppWidget(views, appWidgetManager, -1, appWidgetComponent, providerArticle);
    }

    private static void updateAppWidget(RemoteViews views, AppWidgetManager appWidgetManager, int appWidgetId, ComponentName appWidgetComponent, Article providerArticle) {

        SimpleTarget mediaGlideTarget;

        if (providerArticle == null) {
            return;
        }

        views.setTextViewText(R.id.titleTextView, providerArticle.getTitle());
        if (providerArticle.getGeoFacet() != null && providerArticle.getGeoFacet().size() > 0) {
            views.setTextViewText(R.id.facetTitleTextView, providerArticle.getGeoFacet().get(0).getFacetText());
        }

        if (providerArticle.getMultimedia() != null && providerArticle.getMultimedia().size() > 0) {
            Multimedium imageMedia = providerArticle.getMultimedia().get(0);

            if(imageMedia != null && appWidgetId >= 0) {

                mediaGlideTarget = new SimpleTarget<Bitmap>(imageMedia.getWidth(), imageMedia.getHeight()) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        views.setImageViewBitmap(R.id.articleImageView, resource);
                        appWidgetManager.updateAppWidget(appWidgetId, views);
                    }
                };

                Glide.with(CulturedApp.getAppContext())
                        .load(imageMedia.getUrl())
                        .asBitmap()
                        .centerCrop()
                        .into(mediaGlideTarget);
            }
        }
        if(appWidgetId >= 0) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
        } else {
            appWidgetManager.updateAppWidget(appWidgetComponent, views);
        }
    }
}
