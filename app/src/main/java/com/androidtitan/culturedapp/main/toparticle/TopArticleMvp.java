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

        Loader<Cursor> createBasicCursorLoader(int loaderId);


        interface CallbackListener {
            //void sampleImageLoadComplete(Bitmap bitmap);
        }

    }

    interface Presenter {

        void loadArticles();
    }

    interface View extends MvpView {

        void updateArticles(List<Article> articleList);

        void cursorDataNotAvailable();
        void cursorDataEmpty();

        void setLoading();
    }
}
