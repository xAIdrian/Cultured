package com.androidtitan.culturedapp.main.toparticle;

import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;
import com.androidtitan.culturedapp.model.newyorktimes.FacetType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by amohnacs on 9/19/16.
 */

public interface TopArticleMvp {

    interface Provider {

        void fetchArticles(CallbackListener listener);
        void fetchFacets(CallbackListener listener);

        interface CallbackListener {

            void onArticleConstructionComplete(ArrayList<Article> articleArrayList);
            void onFacetConstructionComplete(HashMap<FacetType, HashMap<Integer, List<Facet>>> facetArrayList);

            void cursorDataNotAvailable();
            void cursorDataEmpty();
        }

    }

    interface Presenter {

        void loadArticles(boolean isTopArticles);
    }

    interface View {

        void updateArticles(List<Article> articleList);

        void setLoading();
        void displayDataNotAvailable();
        void displayDataEmpty();
    }
}
