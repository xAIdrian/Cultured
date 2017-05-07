package com.androidtitan.culturedapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.main.CulturedApp;
import com.androidtitan.culturedapp.main.toparticle.TopArticleMvp;
import com.androidtitan.culturedapp.main.toparticle.TopArticlePresenter;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link ImageAppWidgetProviderConfigureActivity ImageAppWidgetProviderConfigureActivity}
 */
public class ImageAppWidgetProvider extends AppWidgetProvider implements TopArticleMvp.View {
    private final static String TAG = ImageAppWidgetProvider.class.getCanonicalName();

    @Inject
    TopArticlePresenter presenter;

    private Bitmap articleBitmap;

    private Article providerTopArticle;

    private ImageView backgroundImage;
    private TextView titleTextView;
    private TextView facetTextView;

    private Context context;
    private AppWidgetManager appWidgetManager;
    private int[] appWidgetIds;

    @Override
    public void onEnabled(Context context) { //think of this as your onCreate()
        Log.e(TAG, "onEnabled()");

        CulturedApp.getAppComponent().inject(this);
        this.context = context;

        presenter.bindView(this);

        presenter.loadArticles();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e(TAG, "onUpdate()");

        this.appWidgetManager = appWidgetManager;
        this.appWidgetIds = appWidgetIds;

        // Pull data for the top article from our Content Provider

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.e(TAG, "onDeleted()");
        //Null out all that we have used
        presenter.unbindView();
        this.context = null;

        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            //ImageAppWidgetProviderConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onDisabled(Context context) { //think of this as your onCreate()
        Log.e(TAG, "onDisabled()");

        presenter.unbindView();
    }

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        if (providerTopArticle == null) {
            return;
        }

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.image_app_widget_provider);
        //views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setTextViewText(R.id.titleTextView, providerTopArticle.getTitle());
        views.setTextViewText(R.id.facetTitleTextView, providerTopArticle.getGeoFacet().get(0).getFacetText());
        views.setImageViewBitmap(R.id.articleImageView, articleBitmap);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * An instance of Glide is spun up and fetches the image formatted as a bitmap
     *
     * @param url Multimedium url
     * @return Bitmap associated with Article formatted by Glide
     */
    private Bitmap fetchImage(Context context, String url) {
        final Bitmap[] theBitmap = new Bitmap[1];

        new Thread(() -> {
            try {
                theBitmap[0] = Glide.
                        with(context).
                        load(url).
                        asBitmap().
                        into(110, 180)
                        .get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        return theBitmap[0];
    }

    @Override
    public void updateArticles(List<Article> articleList) {

        Log.e(TAG, "updateArticles(List<Article>)");

        if (articleList.size() > 0) {
            for (Article article : articleList) {
                if (article.getMultimedia().size() > 0) {
                    providerTopArticle = article;
                }
            }
        }

        articleBitmap = fetchImage(context, providerTopArticle.getMultimedia().get(0).getUrl());
        this.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void setLoading() {

    }

    @Override
    public void displayDataNotAvailable() {

    }

    @Override
    public void displayDataEmpty() {

    }
}

