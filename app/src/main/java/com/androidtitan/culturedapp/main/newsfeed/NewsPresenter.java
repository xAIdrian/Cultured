package com.androidtitan.culturedapp.main.newsfeed;

import android.content.Context;
import android.util.Log;

import com.androidtitan.culturedapp.common.BasePresenter;
import com.androidtitan.culturedapp.main.CulturedApp;
import com.androidtitan.culturedapp.model.ApiError;
import com.androidtitan.culturedapp.model.newyorktimes.Article;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by amohnacs on 3/21/16.
 */
public class NewsPresenter extends BasePresenter<NewsMvp.View> implements NewsMvp.Presenter,
        NewsMvp.Provider.CallbackListener {
    private final String TAG = getClass().getSimpleName();

    private Context context;

    @Inject
    NewsProvider newsProvider;
    
    //todo:we are going to make this switch to a live Feed Version
    public NewsPresenter(Context context) {
        this.context = context;

        CulturedApp.getAppComponent().inject(this);
    }


    @Override
    public List<Article> loadArticles(int limit) {

        return newsProvider.fetchArticles("world", limit, this);
    }

    @Override
    public void loadOffsetArticles(int limit, int offset) {

        newsProvider.fetchAdditionalArticles("world", limit, offset, this);
    }

    @Override
    public void newArticleRefresh() {

        newsProvider.fetchAdditionalArticlesToInsert("world", getMvpView().getArticles(), this);

    }


    //callback methods

    @Override
    public void appendArticleToAdapter(Article article) {
        getMvpView().appendAdapterItem(article);
        getMvpView().updateNewsAdapter();
    }

    @Override
    public void insertArticleInAdapter(int index, Article article) {
        getMvpView().insertAdapterItem(index, article);
    }

    @Override
    public void onCompleted() {
        getMvpView().onLoadComplete();
    }

    @Override
    public void responseFailed(ApiError apiError) {
        Log.d(TAG, apiError.getMessage());
        getMvpView().displayError(apiError.getMessage(), apiError.getAdditionalProperties());

    }
}

