package com.androidtitan.culturedapp.main.newsfeed;

import android.content.Context;
import android.util.Log;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.structure.RxHelper;
import com.androidtitan.culturedapp.main.web.retrofit.NewsEndpoint;
import com.androidtitan.culturedapp.main.web.retrofit.ServiceGenerator;
import com.androidtitan.culturedapp.model.ApiError;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.NewsResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.*;

/**
 * Created by amohnacs on 8/29/16.
 */

@Singleton
public class NewsFeedProvider implements NewsFeedMvp.Provider {
    private final String TAG = getClass().getSimpleName();

    private Context context;
    private NewsEndpoint newsService;

    private ArrayList<Article> fetchArticleList = new ArrayList<>();

    @Inject
    public NewsFeedProvider(Context context) {
        this.context = context;
        newsService = ServiceGenerator.createService(NewsEndpoint.class);
    }

    @Override
    public List<Article> fetchArticles(String section, int limit, final CallbackListener listener) {


        final Observable<NewsResponse> call = newsService.newsWireArticles(section, limit, 0, //our offset
                context.getResources().getString(R.string.nyt_api_key_newswire));

        call.compose(RxHelper.applySchedulers())
                .retry(10)
                .subscribe(new Subscriber<NewsResponse>() {
                    @Override
                    public void onCompleted() {

                    Log.d(TAG, "response received: " + fetchArticleList.size() + " newsWireArticles received");

                        listener.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        ApiError error = new ApiError();
                        error.setMessage(e.getMessage());
                        listener.responseFailed(error);
                    }

                    @Override
                    public void onNext(NewsResponse newsResponse) {

                        for(Article article : newsResponse.getArticles()) {
                            fetchArticleList.add(article);
                        }

                    }
                });

        return fetchArticleList;
    }

    @Override
    public void fetchAdditionalArticles(String section, int limit, int offset,
                                        final CallbackListener listener) {

        final Observable<NewsResponse> call = newsService.newsWireArticles(section, limit, offset,
                context.getResources().getString(R.string.nyt_api_key_newswire));

        call.compose(RxHelper.applySchedulers())
                .retry(10)
                .subscribe(new Subscriber<NewsResponse>() {
                    @Override
                    public void onCompleted() {

                        Log.d(TAG, "response received: " + fetchArticleList.size() + " newsWireArticles received");

                        listener.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        ApiError error = new ApiError();
                        error.setMessage(e.getMessage());
                        listener.responseFailed(error);
                    }

                    @Override
                    public void onNext(NewsResponse newsResponse) {

                        for (Article article : newsResponse.getArticles()) {
                            listener.appendArticleToAdapter(article);

                        }
                    }
                });

    }

    @Override
    public void refreshForAdditionalArticlesToInsert(String section, final List<Article> articlesList,
                                                final CallbackListener listener) {

        final Observable<NewsResponse> call = newsService.newsWireArticles(section, 10, 0,
            context.getResources().getString(R.string.nyt_api_key_newswire));

        call.compose(RxHelper.applySchedulers())
            .retry(10)
            .subscribe(new Subscriber<NewsResponse>() {
                @Override
                public void onCompleted() {

                    Log.d(TAG, "response received: " + fetchArticleList.size() + " newsWireArticles received");

                    listener.onCompleted();
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();

                    ApiError error = new ApiError();
                    error.setMessage(e.getMessage());
                    listener.responseFailed(error);
                }

                @Override
                public void onNext(NewsResponse newsResponse) {

                    if(articlesList.size() > 0) {
                        listener.insertArticlesIntoAdapter(0, addNewArticlesToArticleList (newsResponse.getArticles(), articlesList));
                    }
                }
            });
    }

    public ArrayList<Article> addNewArticlesToArticleList(List<Article> responseArticles, List<Article> articlesList) {
        ArrayList<Article> newArticles = new ArrayList<Article>();

        int tinyScopedIndex = 0;
        while(tinyScopedIndex < responseArticles.size()) {

            if(responseArticles.get(tinyScopedIndex).getTitle().equals(articlesList.get(0).getTitle())) {
                return  newArticles;
            } else {
                newArticles.add(responseArticles.get(tinyScopedIndex));
            }
            tinyScopedIndex ++;
        }

        return newArticles;
    }

}
