package com.androidtitan.culturedapp.main.newsfeed;

import android.content.Context;
import android.util.Log;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.main.domain.retrofit.NewsEndpoint;
import com.androidtitan.culturedapp.main.domain.retrofit.ServiceGenerator;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.NewsResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amohnacs on 8/29/16.
 */

@Singleton
public class NewsProvider implements NewsMvp.Provider {
    private final String TAG = getClass().getSimpleName();

    private Context context;
    private NewsEndpoint newsService;

    private ArrayList<Article> fetchArticleList = new ArrayList<>();
    private ArrayList<Article> loadNextArticleList = new ArrayList<>();

    @Inject
    public NewsProvider(Context context) {
        this.context = context;
        newsService = ServiceGenerator.createService(NewsEndpoint.class);
    }


    @Override
    public List<Article> fetchArticles(String section, int limit, final CallbackListener listener) {


        final Call<NewsResponse> call = newsService.newsWireArticles(section, limit, 0, //our offset
                context.getResources().getString(R.string.nyt_api_key_newswire));

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse resp = response.body();
                    Log.d(TAG, "response received: " + resp.getStatus() + " : "
                            + resp.getArticles().size() + " newsWireArticles received");

                    for (Article article : resp.getArticles()) {

                        fetchArticleList.add(article);
                    }
                    listener.onCompleted();

                } else {
                    Log.e(TAG, "response fail : " + response.message());
                }


            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return fetchArticleList;
    }

    @Override
    public void fetchAdditionalArticles(String section, int limit, int offset,
                                        final CallbackListener listener) {

        final Call<NewsResponse> call = newsService.newsWireArticles(section, limit, offset,
                context.getResources().getString(R.string.nyt_api_key_newswire));

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse resp = response.body();
                    Log.d(TAG, "refresh response received: " + resp.getStatus() + " : "
                            + resp.getArticles().size() + " newsWireArticles received");

                    for (int i = 0; i < resp.getArticles().size(); i++) {

                        listener.appendArticleToAdapter(resp.getArticles().get(i));
                    }

                } else {
                    Log.e(TAG, "response fail : " + response.message());
                }
                listener.onCompleted();
            }
            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    @Override
    public void fetchAdditionalArticlesToInsert(String section, final List<Article> articlesList,
                                                final CallbackListener listener) {

        final Call<NewsResponse> call = newsService.newsWireArticles(section, 1, 0,
                context.getResources().getString(R.string.nyt_api_key_newswire));

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse resp = response.body();
                    Log.d(TAG, "refresh response received: " + resp.getStatus() + " : "
                            + resp.getArticles().size() + " newsWireArticles received");

                    if(!resp.getArticles().get(0).getAbstract()
                            .equals(articlesList.get(0).getAbstract())) {

                        listener.insertArticleInAdapter(0, resp.getArticles().get(0));

                        Log.d(TAG, "Item added to the top ...");
                    }

                } else {
                    Log.e(TAG, "response fail : " + response.message());
                }

                listener.onCompleted();
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
