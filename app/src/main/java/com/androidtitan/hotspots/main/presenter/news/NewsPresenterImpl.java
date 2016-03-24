package com.androidtitan.hotspots.main.presenter.news;

import android.content.Context;
import android.util.Log;

import com.androidtitan.hotspots.main.domain.retrofit.DaggerNewsRetrofitComponent;
import com.androidtitan.hotspots.main.domain.retrofit.NewsEndpointInterface;
import com.androidtitan.hotspots.main.model.newyorktimes.Article;
import com.androidtitan.hotspots.main.model.newyorktimes.NewsResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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

    private Context context;
    private NewsView newsView;

    @Inject //todo:we are going to make this switch to an RSS Feed Version
    public NewsPresenterImpl(Context context, NewsView newsview) {

        DaggerNewsRetrofitComponent.create()
                .inject(this);

        this.context = context;
        this.newsView = newsview;
    }

    @Override
    public List<Article> queryNews(String section, int limit) {

        final ArrayList<Article> itemList = new ArrayList<Article>();

        NewsEndpointInterface newsService = retrofit.create(NewsEndpointInterface.class);
        final Call<NewsResponse> call = newsService.articles(section, limit,
                "043b20a6a48cee1dddf92ee2257cfd73:10:74775241");

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if(response.isSuccessful()) {
                    NewsResponse resp = response.body();
                    Log.d(TAG, "response received: " + resp.getStatus() + " : "
                            + resp.getArticles().size() + " articles received");

                    for(Article article : resp.getArticles()) {

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

        return itemList;
    }
}
