package com.androidtitan.culturedapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.main.toparticle.TopArticleMvp;
import com.androidtitan.culturedapp.main.toparticle.TopArticleProvider;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;
import com.androidtitan.culturedapp.model.newyorktimes.FacetType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Serves its purpose as the collection widget's adapter.  It connects the collection items with the data set.
 *
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
    }

    /**
     * Called when your factory is first constructed. The same factory may be shared across multiple
     * RemoteViewAdapters depending on the intent passed.
     * <p>
     * In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
     * for example downloading or creating content etc, should be deferred to onDataSetChanged()
     * or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
     */
    @Override
    public void onCreate() {

        provider.getInstance(context);
        provider.fetchFacets(this);
    }

    @Override
    public void onDataSetChanged() {
        //todo :
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return facetList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        //todo : how do we know which remote view to load when it comes to light/dark
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.facet_collection_widget_light_item);

        remoteViews.setTextViewText(R.id.widget_facet_title, (CharSequence) facetList.get(position));
        remoteViews.setTextViewText(R.id.widget_facet_date, (CharSequence) facetList.get(position));

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
        return facetList.get(position).getStoryId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onArticleConstructionComplete(ArrayList<Article> articleArrayList) {
        //
    }

    @Override
    public void onFacetConstructionComplete(HashMap<FacetType, HashMap<Integer, List<Facet>>> facetMap) {

        for(FacetType facetType : facetMap.keySet()) {
            for(List<Facet> collection : facetMap.get(facetType).values()) {
                facetList.addAll(collection);
            }
        }
        Collections.shuffle(facetList);
    }

    @Override
    public void cursorDataNotAvailable() {
        //todo: display empty page
    }

    @Override
    public void cursorDataEmpty() {
        //todo: display empty page
    }
}
