package com.androidtitan.culturedapp.main.newsfeed;

import android.content.Context;
import android.util.Log;

import com.androidtitan.culturedapp.common.structure.BasePresenter;
import com.androidtitan.culturedapp.main.toparticle.TopArticleMvp;
import com.androidtitan.culturedapp.main.toparticle.TopArticleProvider;
import com.androidtitan.culturedapp.model.ApiError;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;
import com.androidtitan.culturedapp.model.newyorktimes.FacetType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * Created by amohnacs on 3/21/16.
 */
public class NewsFeedPresenter extends BasePresenter<NewsFeedMvp.View> implements NewsFeedMvp.Presenter,
        TopArticleMvp.Provider.CallbackListener, NewsFeedMvp.Provider.CallbackListener {
    private final String TAG = getClass().getSimpleName();

    private Context context;

    NewsFeedProvider newsFeedProvider;
    TopArticleProvider topArticleProvider;
    
    //todo:we are going to make this switch to a live Feed Version
    public NewsFeedPresenter(Context context) {
        this.context = context;
        this.newsFeedProvider = new NewsFeedProvider(context);
        this.topArticleProvider = TopArticleProvider.getInstance(context);
    }


    @Override
    public List<Article> loadArticles(int limit) {

        return newsFeedProvider.fetchArticles("world", limit, this);
    }

    @Override
    public void loadOffsetArticles(int limit, int offset) {

        newsFeedProvider.fetchAdditionalArticles("world", limit, offset, this);
    }

    @Override
    public void newsArticlesRefresh(List<Article> articles, int limit) {

        newsFeedProvider.refreshForAdditionalArticlesToInsert("world", articles, this);

    }

    @Override
    public void checkTopArticlesPresent() {
        topArticleProvider.fetchArticles(this);
    }


    //callback methods
    @Override
    public void appendArticleToAdapter(Article article) {
        if (isViewAttached()) {
            getMvpView().appendAdapterItem(article);
        }
    }

    @Override
    public void insertArticleIntoAdapter(int index, Article article) {
        if (isViewAttached()) {
            getMvpView().insertAdapterItem(index, article);
        }
    }

    @Override
    public void insertArticlesIntoAdapter(int index, ArrayList<Article> articles) {
        if (isViewAttached()) {
            getMvpView().insertAdapterItems(index, articles);
        }
    }

    @Override
    public void onCompleted() {
        if(isViewAttached()) {
            getMvpView().onLoadComplete();
        }
    }

    @Override
    public void responseFailed(ApiError apiError) {
        Log.d(TAG, apiError.getMessage());
        if (isViewAttached()) {
            getMvpView().displayError(apiError.getMessage(), apiError.getAdditionalProperties());
        }

    }

    @Override
    public void onArticleConstructionComplete(ArrayList<Article> articleArrayList) {
        boolean doArticlesExist = !articleArrayList.isEmpty();
        if(isViewAttached()) {
            getMvpView().doTopArticlesExist(doArticlesExist);
        }
    }

    @Override
    public void onFacetConstructionComplete(HashMap<FacetType, HashMap<Integer, List<Facet>>> facetArrayList) {
        //
    }

    @Override
    public void cursorDataNotAvailable() {

    }

    @Override
    public void cursorDataEmpty() {

    }
}

