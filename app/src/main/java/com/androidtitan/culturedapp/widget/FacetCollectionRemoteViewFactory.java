package com.androidtitan.culturedapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.main.toparticle.TopArticleMvp;
import com.androidtitan.culturedapp.main.toparticle.TopArticleProvider;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;
import com.androidtitan.culturedapp.model.newyorktimes.FacetType;
import com.androidtitan.culturedapp.widget.ui.FacetCollectionWidgetProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Serves its purpose as the collection widget's adapter.  It connects the collection items with the data set.
 * <p>
 * Created by Adrian Mohnacs on 7/1/17.
 */

public class FacetCollectionRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory, TopArticleMvp.Provider.CallbackListener {
    private static final String TAG = FacetCollectionRemoteViewFactory.class.getSimpleName();

    private Context context;
    private int appWidgetId;

    private TopArticleProvider provider;

    private List<Facet> facetList = new ArrayList<>();


    public FacetCollectionRemoteViewFactory(Context context, Intent intent) {
        this.context = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        this.provider = provider.getInstance(context);
    }

    /*
     In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
     for example downloading or creating content etc, should be deferred to onDataSetChanged()
     or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
     */
    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate()");

        provider.fetchFacets(this);
    }

    @Override
    public void onDataSetChanged() {
        Log.e(TAG, "onDataSetChanged()");
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy()");
        provider = null;
    }

    @Override
    public int getCount() {
        return facetList.size();
    }

    /*
     Given the position (index) of a WidgetItem in the array, use the item's text value in
     combination with the app widget item XML file to construct a RemoteViews object.
     */
    @Override
    public RemoteViews getViewAt(int position) {
        Log.e(TAG, "getViewAt()");

        //todo : how do we know which remote view to load when it comes to light/dark
        /*
         Construct a RemoteViews item based on the app widget item XML file, and set the
         text based on the position.
         */
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.facet_collection_widget_light_item);

        remoteViews.setTextViewText(R.id.widget_facet_title, (CharSequence) facetList.get(position).getFacetText());
        remoteViews.setTextViewText(R.id.widget_facet_date, (CharSequence) facetList.get(position).getCreatedDate().toString());

        /*
         Next, set a fill-intent, which will be used to fill in the pending intent template
         that is set on the collection view in FacetCollectionWidgetProvider
         */
        /*
        Bundle extras = new Bundle();
        extras.putInt(FacetCollectionWidgetProvider.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        */
        /*
         Make it possible to distinguish the individual on-click action of a given item
         */
        //remoteViews.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

        return remoteViews;
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
        if (facetList.size() > 0) {
            return facetList.get(position).getStoryId();
        }
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void onArticleConstructionComplete(ArrayList<Article> articleArrayList) {
        //
    }

    @Override
    public void onFacetConstructionComplete(HashMap<FacetType, HashMap<Integer, List<Facet>>> facetMap) {
        Log.e(TAG, "onFacetConstructionComplete()");

        this.facetList = transformFacets(facetMap);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, FacetCollectionWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.facet_collection_listview);

    }

    @Override
    public void cursorDataNotAvailable() {
        //todo: display empty page
    }

    @Override
    public void cursorDataEmpty() {
        //todo: display empty page
    }

    private List<Facet> transformFacets(HashMap<FacetType, HashMap<Integer, List<Facet>>> facetMap) {
        for (FacetType facetType : facetMap.keySet()) {
            for (List<Facet> collection : facetMap.get(facetType).values()) {
                facetList.addAll(collection);
            }
        }
        Collections.shuffle(facetList);
        return facetList;
    }
}
