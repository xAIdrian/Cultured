package com.androidtitan.culturedapp.widget.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.main.CulturedApp;
import com.androidtitan.culturedapp.main.newsfeed.ui.NewsDetailActivity;
import com.androidtitan.culturedapp.main.toparticle.TopArticleMvp;
import com.androidtitan.culturedapp.main.toparticle.TopArticleProvider;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;
import com.androidtitan.culturedapp.model.newyorktimes.Multimedium;
import com.androidtitan.culturedapp.widget.AppWidgetProviderConfigureActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import static com.androidtitan.culturedapp.main.newsfeed.ui.NewsFeedActivity.ARTICLE_EXTRA;
import static com.androidtitan.culturedapp.main.newsfeed.ui.NewsFeedActivity.ARTICLE_GEO_FACETS;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link AppWidgetProviderConfigureActivity AppWidgetProviderConfigureActivity}
 */
public class ImageWidgetProvider extends AppWidgetProvider implements TopArticleMvp.Provider.CallbackListener {
    private final static String TAG = ImageWidgetProvider.class.getCanonicalName();

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

        RemoteViews views = new RemoteViews(CulturedApp.getAppContext().getPackageName(), R.layout.image_widget_provider);

        if (appWidgetIds != null && appWidgetIds.length > 0) {
            // There may be multiple widgets active, so update all of them
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(views, appWidgetId, providerArticle);
            }
        }

        if(providerArticle != null) {
            PendingIntent detailArticlePendingIntent = buildPendingIntent();
            views.setOnClickPendingIntent(R.id.articleImageView, detailArticlePendingIntent);
        }

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.e(TAG, "onDeleted()");

        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            //AppWidgetProviderConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onDisabled(Context context) {
        Log.e(TAG, "onDisabled()");
        // Enter relevant functionality for when the last widget is disabled
    }

    private void updateAppWidget(RemoteViews views, int appWidgetId, Article providerArticle) {

        if (providerArticle == null) {
            return;
        }

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
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    private PendingIntent buildPendingIntent() {
        Intent intent = new Intent(CulturedApp.getAppContext(), NewsDetailActivity.class);
        intent.putExtra(ARTICLE_EXTRA, providerArticle);
        intent.putStringArrayListExtra(ARTICLE_GEO_FACETS, getGeoFacetArrayList(providerArticle));

        return PendingIntent.getActivity(CulturedApp.getAppContext(), 200, intent, 0);
    }

    @Override
    public void onConstructionComplete(ArrayList<Article> articleArrayList) {

        if (articleArrayList.size() > 0) {
            providerArticle = articleArrayList.get(0);
            for(Article article : articleArrayList) {
                if(!providerArticle.getMultimedia().isEmpty()) {
                    providerArticle = article;
                    this.onUpdate(context, appWidgetManager, appWidgetIds);
                    return;
                }
            }
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

    private ArrayList<String> getGeoFacetArrayList(@NonNull Article article) {
        ArrayList<String> facets = new ArrayList<>();
        for (Facet facet : article.getGeoFacet()) {
            facets.add(facet.getFacetText());
        }
        return facets;
    }
}

