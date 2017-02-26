package com.androidtitan.culturedapp.main.trending;

import com.androidtitan.culturedapp.common.structure.MvpView;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;
import com.androidtitan.culturedapp.model.newyorktimes.FacetType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Adrian Mohnacs on 1/22/17.
 */

public interface TrendingMvp {

    interface Provider {

        void fetchInitialFacets(CallbackListener listener);

        interface CallbackListener {

            void onFacetLoadComplete(HashMap<FacetType,  List<Facet>> facetMap);

            void cursorDataNotAvailable();
            void cursorDataEmpty();
        }
    }

    interface Presenter  {

        void loadInitialFacets();
    }

    interface View extends MvpView {

        void initializeGeoFacetSpark(List<Facet> geoFacetList);
        void initializeOrgFacetSpark(List<Facet> orgFacetList);
        void initializeDesFacetSpark(List<Facet> desFacetList);
        void initializePerFacetSpark(List<Facet> perFacetList);

        void setLoading();
        void displayDataNotAvailable();
        void displayDataEmpty();
    }
}
