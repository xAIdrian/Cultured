package com.androidtitan.culturedapp.main.toparticle;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.util.Log;

import com.androidtitan.culturedapp.common.structure.BasePresenter;
import com.androidtitan.culturedapp.main.CulturedApp;
import com.androidtitan.culturedapp.main.provider.wrappers.FacetCursorWrapper;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;
import com.androidtitan.culturedapp.model.newyorktimes.FacetType;
import com.androidtitan.culturedapp.model.newyorktimes.Multimedium;
import com.androidtitan.culturedapp.main.provider.wrappers.ArticleCursorWrapper;
import com.androidtitan.culturedapp.main.provider.wrappers.MultimediumCursorWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import static com.androidtitan.culturedapp.common.Constants.ARTICLE_LOADER_ID;
import static com.androidtitan.culturedapp.common.Constants.TOP_ARTICLE_MEDIA_LOADER_ID;
import static com.androidtitan.culturedapp.common.Constants.TOP_ARTICLE_FACET_LOADER_ID;
import static com.androidtitan.culturedapp.model.newyorktimes.FacetType.DES;
import static com.androidtitan.culturedapp.model.newyorktimes.FacetType.GEO;
import static com.androidtitan.culturedapp.model.newyorktimes.FacetType.ORG;
import static com.androidtitan.culturedapp.model.newyorktimes.FacetType.PER;

/**
 * Created by amohnacs on 9/19/16.
 */

//The functionality that the PRESENTER contains should be driven by the Business Requirements.

public class TopArticlePresenter extends BasePresenter<TopArticleMvp.View> implements TopArticleMvp.Presenter,
        TopArticleMvp.Provider.CallbackListener {

    private final String TAG = getClass().getSimpleName();

    private Context context;

    @Inject
    TopArticleProvider topArticleProvider;

    public TopArticlePresenter(Context context) {
        this.context = context;

        CulturedApp.getAppComponent().inject(this);

    }

    /**
     * We will always have fresh data from remote, the Loaders handle the local data
     */
    @Override
    public void loadArticles() {

        topArticleProvider.fetchArticles(this);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onConstructionComplete(ArrayList<Article> articleArrayList) {
        sendDownArticlesToView(articleArrayList);
    }

    @Override
    public void cursorDataNotAvailable() {
        Log.e(TAG, "Articles not instantiated");
        if(isViewAttached()) {
            getMvpView().displayDataNotAvailable();
        }
    }

    @Override
    public void cursorDataEmpty() {
        if(isViewAttached()) {
            getMvpView().displayDataEmpty();
        }
        Log.e(TAG, "No articles were returned");
    }

    public void sendDownArticlesToView(ArrayList<Article> articleList) {

        if (articleList != null) {
            if (articleList.size() > 0) {

                articleList = stripArticlesWithoutMedia(articleList);

                if(isViewAttached()) {
                    getMvpView().updateArticles(articleList);
                }
            } else {
                if(isViewAttached()) {
                    getMvpView().displayDataEmpty();
                }
                Log.e(TAG, "No articles were returned");
            }
        } else {
            Log.e(TAG, "Articles not instantiated");
            if(isViewAttached()) {
                getMvpView().displayDataNotAvailable();
            }
        }
    }

    /**
     * We only want to work with Articles that have images to display
     * @param articleListToStrip
     * @return A list of articles that does not contain any articles without media
     */
    private ArrayList<Article> stripArticlesWithoutMedia(ArrayList<Article> articleListToStrip) {
        //we are using an array here because we were getting a ConcurrentModificationException
        //when we just removed the article while we were iterating through the list
        ArrayList<Article> deletes = new ArrayList<>();

        for(int i = 0; i < articleListToStrip.size(); i++) {
            if(articleListToStrip.get(i).getMultimedia().size() <= 0) {
                deletes.add(articleListToStrip.get(i));
            }
        }

        articleListToStrip.removeAll(deletes);
        return articleListToStrip;
    }
}
