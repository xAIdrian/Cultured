package com.androidtitan.culturedapp.main.trending;

import android.content.Context;
import android.util.Log;

import com.androidtitan.culturedapp.common.structure.BasePresenter;
import com.androidtitan.culturedapp.main.CulturedApp;
import com.androidtitan.culturedapp.main.toparticle.TopArticleProvider;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;
import com.androidtitan.culturedapp.model.newyorktimes.FacetType;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Adrian Mohnacs on 1/22/17.
 */

public class TrendingPresenter extends BasePresenter<TrendingMvp.View> implements TrendingMvp.Presenter,
    TrendingMvp.Provider.CallbackListener{
    private final String TAG = getClass().getSimpleName();

    private Context context;

    @Inject
    TrendingProvider trendingProvider;

    public TrendingPresenter(Context context) {
        this.context = context;

        CulturedApp.getAppComponent().inject(this);
    }

    @Override
    public void loadInitialFacets() {
        trendingProvider.fetchInitialFacets(this);
    }

    @Override
    public void onFacetLoadComplete(HashMap<FacetType, List<Facet>> facetMap) {

        if(!facetMap.isEmpty()) {

            Log.e(TAG, "we received our TrendingData");

            if (isViewAttached()) {
                getMvpView().initializeDesFacetSpark(facetMap.get(FacetType.DES));
                getMvpView().initializeGeoFacetSpark(facetMap.get(FacetType.GEO));
                getMvpView().initializeOrgFacetSpark(facetMap.get(FacetType.ORG));
                getMvpView().initializePerFacetSpark(facetMap.get(FacetType.PER));
            }
        }
    }

    @Override
    public void cursorDataNotAvailable() {

    }

    @Override
    public void cursorDataEmpty() {

    }
}
