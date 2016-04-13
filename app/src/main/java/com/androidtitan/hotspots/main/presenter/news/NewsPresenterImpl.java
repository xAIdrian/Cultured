package com.androidtitan.hotspots.main.presenter.news;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.main.domain.retrofit.NewsEndpointInterface;
import com.androidtitan.hotspots.main.domain.retrofit.NewsRetrofit;
import com.androidtitan.hotspots.main.model.newyorktimes.Article;
import com.androidtitan.hotspots.main.model.newyorktimes.NewsResponse;
import com.androidtitan.hotspots.main.ui.activities.NewsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by amohnacs on 3/21/16.
 */
public class NewsPresenterImpl implements NewsPresenter {
    private final String TAG = getClass().getSimpleName();

    Retrofit retrofit;

    private NewsActivity newsActivity;

    //@Bind(R.id.swipeRefresh) SwipeRefreshLayout refreshLayout;

    private List<Article> itemList;

    //todo:we are going to make this switch to a live Feed Version
    public NewsPresenterImpl(Context context) {
        retrofit = new NewsRetrofit().getRetrofit();
    }

    @Override
    public void takeActivity(NewsActivity activity) {
        newsActivity = activity;
    }

    @Override
    public List<Article> initialNewsQuery(String section, int limit) {

        itemList = new ArrayList<>();

        NewsEndpointInterface newsService = retrofit.create(NewsEndpointInterface.class);
        final Call<NewsResponse> call = newsService.articles(section, limit, 0, //our offset
                "043b20a6a48cee1dddf92ee2257cfd73:10:74775241");

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse resp = response.body();
                    Log.d(TAG, "response received: " + resp.getStatus() + " : "
                            + resp.getArticles().size() + " articles received");

                    for (Article article : resp.getArticles()) {

                        itemList.add(article);
                        newsActivity.updateNewsAdapter();
                    }
                    newsActivity.refreshCompleted();
                } else {
                    Log.e(TAG, "response fail");
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        //newsView.refreshCompleted();
        return itemList;
    }

    @Override
    public void appendNewsQuery(String section, int limit, int offset) {

        itemList = new ArrayList<>();

        NewsEndpointInterface newsService = retrofit.create(NewsEndpointInterface.class);
        final Call<NewsResponse> call = newsService.articles(section, limit, offset,
                "043b20a6a48cee1dddf92ee2257cfd73:10:74775241");

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse resp = response.body();
                    Log.d(TAG, "refresh response received: " + resp.getStatus() + " : "
                            + resp.getArticles().size() + " articles received");

                    for (int i = 0; i < resp.getArticles().size(); i++) {

                        newsActivity.appendAdapterItem(resp.getArticles().get(i));

                    }
                    newsActivity.refreshCompleted();

                } else {
                    Log.e(TAG, "response fail");
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void newArticleRefresh(final String section) {

        NewsEndpointInterface newsService = retrofit.create(NewsEndpointInterface.class);
        final Call<NewsResponse> call = newsService.articles(section, 1, 0,
                "043b20a6a48cee1dddf92ee2257cfd73:10:74775241");

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse resp = response.body();
                    Log.d(TAG, "refresh response received: " + resp.getStatus() + " : "
                            + resp.getArticles().size() + " articles received");

                    if(!resp.getArticles().get(0).getAbstract()
                            .equals(newsActivity.articles.get(0).getAbstract())) {

                        newsActivity.insertAdapterItem(0, resp.getArticles().get(0));
                        newArticleRefresh(section);

                        Log.d(TAG, "Item added to the top ...");
                    }

                } else {
                    Log.e(TAG, "response fail");
                }

                newsActivity.refreshCompleted();
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        //todo: do we need to return anything?
    }
}

