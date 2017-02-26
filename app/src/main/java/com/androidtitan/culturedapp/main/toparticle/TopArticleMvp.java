package com.androidtitan.culturedapp.main.toparticle;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.androidtitan.culturedapp.common.structure.MvpView;
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
    /*
    if(getMvpView() != null) {
                    sendDownArticlesToView(articles);
                }
     */

    interface Presenter {

        void loadArticles();
    }

    interface View extends MvpView {

        void updateArticles(List<Article> articleList);

        void setLoading();
        void displayDataNotAvailable();
        void displayDataEmpty();
    }
}
