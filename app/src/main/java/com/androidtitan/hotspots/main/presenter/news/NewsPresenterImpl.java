package com.androidtitan.hotspots.main.presenter.news;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.main.domain.retrofit.DaggerNewsRetrofitComponent;
import com.androidtitan.hotspots.main.domain.retrofit.NewsEndpointInterface;
import com.androidtitan.hotspots.main.model.newyorktimes.Article;
import com.androidtitan.hotspots.main.model.newyorktimes.NewsResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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

    @Inject Retrofit retrofit;
    @Bind(R.id.swipeRefresh) SwipeRefreshLayout refreshLayout;

    private Context context;
    private NewsView newsView;

    private ArrayList<Article> itemList;

    @Inject //todo:we are going to make this switch to an RSS Feed Version
    public NewsPresenterImpl(Context context, NewsView newsview) {

        DaggerNewsRetrofitComponent.create()
                .inject(this);

        this.context = context;
        this.newsView = newsview;
    }

    @Override
    public List<Article> firstQueryNews(String section, int limit) {

        itemList = new ArrayList<Article>();

        NewsEndpointInterface newsService = retrofit.create(NewsEndpointInterface.class);
        final Call<NewsResponse> call = newsService.articles(section, limit,
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
                        newsView.updateNewsAdapter();
                    }

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
    public void refreshQueryNews(String section, int limit) {

        NewsEndpointInterface newsService = retrofit.create(NewsEndpointInterface.class);
        final Call<NewsResponse> call = newsService.articles(section, limit,
                "043b20a6a48cee1dddf92ee2257cfd73:10:74775241");

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse resp = response.body();
                    Log.d(TAG, "refresh response received: " + resp.getStatus() + " : "
                            + resp.getArticles().size() + " articles received");

                    for (int i = 0; i < resp.getArticles().size(); i++) {

                        if(!resp.getArticles().get(i).getTitle().equals(itemList.get(i).getTitle())) {
                            itemList.add(0, resp.getArticles().get(i));
                            newsView.updateSpecificNewsAdapter(0);

                            Log.e(TAG, "actual get :: " + resp.getArticles().get(i).getTitle());
                        }

                        Log.e(TAG, resp.getArticles().get(i).getTitle());
                    }
                    newsView.refreshCompleted();

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
}

