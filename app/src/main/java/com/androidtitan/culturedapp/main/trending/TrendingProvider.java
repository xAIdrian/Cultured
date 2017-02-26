package com.androidtitan.culturedapp.main.trending;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.util.Log;

import com.androidtitan.culturedapp.common.Constants;
import com.androidtitan.culturedapp.common.FacetGenerator;
import com.androidtitan.culturedapp.main.provider.LoaderHelper;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;
import com.androidtitan.culturedapp.model.newyorktimes.FacetType;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import static com.androidtitan.culturedapp.common.Constants.ARTICLE_LOADER_ID;
import static com.androidtitan.culturedapp.common.Constants.TOP_ARTICLE_FACET_LOADER_ID;
import static com.androidtitan.culturedapp.common.Constants.TRENDING_FACET_LOADER_ID;
import static com.androidtitan.culturedapp.model.newyorktimes.FacetType.DES;
import static com.androidtitan.culturedapp.model.newyorktimes.FacetType.GEO;
import static com.androidtitan.culturedapp.model.newyorktimes.FacetType.ORG;
import static com.androidtitan.culturedapp.model.newyorktimes.FacetType.PER;


/**
 * Created by Adrian Mohnacs on 1/22/17.
 */

/**


 */

public class TrendingProvider implements TrendingMvp.Provider, Loader.OnLoadCompleteListener<Cursor> {
    private final String TAG = getClass().getSimpleName();

    private final CursorLoader facetCursorLoader;

    private FacetGenerator facetGenerator;

    private Context context;
    private CallbackListener reservedCallback;

    private LoaderHelper loaderHelepr;


    @Inject
    public TrendingProvider(Context context) {
        this.context = context;

        loaderHelepr = new LoaderHelper();

        facetCursorLoader = loaderHelepr.createBasicCursorLoader(TRENDING_FACET_LOADER_ID);
        facetCursorLoader.registerListener(TRENDING_FACET_LOADER_ID, this);
    }

    @Override
    public void fetchInitialFacets(CallbackListener listener) {
        reservedCallback = listener;
        facetCursorLoader.startLoading();
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {

        // now you have a hold of [facetType, List of Facets]
        facetGenerator = new FacetGenerator();
        HashMap<FacetType, List<Facet>> trendingFacetMap;

        if (loader.getId() == TRENDING_FACET_LOADER_ID) {

            trendingFacetMap = facetGenerator.generateFacetMapFromCursor(cursor,
                    new HashMap<FacetType, List<Facet>>(), false);
            processFacets(trendingFacetMap, TRENDING_FACET_LOADER_ID, false);

        } else if (loader.getId() == TOP_ARTICLE_FACET_LOADER_ID) {

            trendingFacetMap = facetGenerator.generateFacetMapFromCursor(cursor,
                    new HashMap<FacetType, List<Facet>>(), true);
            processFacets(trendingFacetMap, ARTICLE_LOADER_ID, true);
        }

    }

    private void processFacets(HashMap<FacetType, List<Facet>> localTrendingFacetMap,
                               int loaderId, boolean isStoryId) {

        if (isStoryId) {
            //the load failed and we did not receive any facets not associated with a story
            if (localTrendingFacetMap.get(DES) != null && localTrendingFacetMap.get(GEO) != null
                    && localTrendingFacetMap.get(PER) != null && localTrendingFacetMap.get(ORG) != null) {
                // we have results
                if (localTrendingFacetMap.get(DES).size() == 0 || localTrendingFacetMap.get(GEO).size() == 0
                        || localTrendingFacetMap.get(PER).size() == 0 || localTrendingFacetMap.get(ORG).size() == 0) {
                    loaderHelepr.createBasicCursorLoader(loaderId);
                } else {
                    //load successful

                    reservedCallback.onFacetLoadComplete(localTrendingFacetMap);
                }
            } else {
                Log.e(TAG, "We have received NULL trendingFacetMap");

                CursorLoader topArticleFacetCursorLoader = loaderHelepr.createBasicCursorLoader(TOP_ARTICLE_FACET_LOADER_ID);
                topArticleFacetCursorLoader.registerListener(TOP_ARTICLE_FACET_LOADER_ID, this);
                topArticleFacetCursorLoader.startLoading();

            }
        }
    }
}
