package com.androidtitan.culturedapp.main.toparticle;

import com.androidtitan.culturedapp.model.newyorktimes.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amohnacs on 9/19/16.
 */

public interface TopArticleMvp {

    interface Provider {

        void fetchArticles(CallbackListener listener);

        interface CallbackListener {

            void onConstructionComplete(ArrayList<Article> articleArrayList);

            void cursorDataNotAvailable();
            void cursorDataEmpty();
        }

    }

    interface Presenter {

        void loadArticles();
    }

    interface View {

        void updateArticles(List<Article> articleList);

        void setLoading();
        void displayDataNotAvailable();
        void displayDataEmpty();
    }
}
