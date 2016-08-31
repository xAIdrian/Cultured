package com.androidtitan.hotspots.main.newsfeed;

import android.content.Context;
import android.util.Log;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.common.BasePresenter;
import com.androidtitan.hotspots.main.CulturedApp;
import com.androidtitan.hotspots.main.domain.retrofit.NewsEndpointInterface;
import com.androidtitan.hotspots.main.model.newyorktimes.Article;
import com.androidtitan.hotspots.main.model.newyorktimes.NewsResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by amohnacs on 3/21/16.
 */
public class NewsPresenter extends BasePresenter<NewsMvp.View> implements NewsMvp.Presenter,
        NewsMvp.Provider.CallbackListener {
    private final String TAG = getClass().getSimpleName();

    private Context context;

    @Inject
    NewsProvider newsProvider;

    private List<Article> articleList;

    //todo:we are going to make this switch to a live Feed Version
    public NewsPresenter(Context context) {
        this.context = context;

        CulturedApp.getAppComponent().inject(this);

        articleList = new ArrayList<>();
    }


    @Override
    public void initalArticleStore(int limit) {

        articleList = newsProvider.fetchArticles("world", limit);
    }

    @Override
    public void loadNextNewsArticles(int limit, int offset) {

        newsProvider.fetchAdditionalArticles("world", limit, offset, this);
    }

    @Override
    public void newArticleRefresh() {

        newsProvider.fetchAdditionalArticlesToInsert("world", articleList, this);

    }

    @Override
    public List<Article> getArticles() {
        return articleList;
    }

    //callback methods

    @Override
    public void appendArticleToAdapter(Article article) {
        articleList.add(article);

        getMvpView().appendAdapterItem(article);
        getMvpView().updateNewsAdapter();
    }

    @Override
    public void insertArticleInAdapter(int index, Article article) {
        articleList.add(index, article);

        getMvpView().insertAdapterItem(index, article);
    }

    @Override
    public void onCompleted() {
        getMvpView().refreshCompleted();
    }
}

