package com.androidtitan.culturedapp.main.toparticle;

import android.content.Context;
import android.util.Log;

import com.androidtitan.culturedapp.common.structure.BasePresenter;
import com.androidtitan.culturedapp.model.newyorktimes.Article;

import java.util.ArrayList;

/**
 * Created by amohnacs on 9/19/16.
 */

//The functionality that the PRESENTER contains should be driven by the Business Requirements.

public class TopArticlePresenter extends BasePresenter<TopArticleMvp.View> implements TopArticleMvp.Presenter,
        TopArticleMvp.Provider.CallbackListener {

    private final String TAG = getClass().getSimpleName();

    private Context context;

    TopArticleProvider topArticleProvider;

    public TopArticlePresenter(Context context) {
        this.context = context;
        this.topArticleProvider = new TopArticleProvider(context);

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
