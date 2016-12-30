package com.androidtitan.culturedapp.main.web.services;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.structure.RxHelper;
import com.androidtitan.culturedapp.main.provider.DatabaseContract;
import com.androidtitan.culturedapp.main.web.retrofit.NewsEndpoint;
import com.androidtitan.culturedapp.main.web.retrofit.ServiceGenerator;
import com.androidtitan.culturedapp.model.ApiError;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;
import com.androidtitan.culturedapp.model.newyorktimes.NewsResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static com.androidtitan.culturedapp.common.Constants.NO_ARTICLE_ID;

/**
 * Created by Adrian Mohnacs on 12/29/16.
 */

public class FacetDownloadService extends Service {
    private final String TAG = getClass().getSimpleName();

    private NewsEndpoint newsService;

    private ArrayList<Article> fetchArticleList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        newsService = ServiceGenerator.createService(NewsEndpoint.class);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, TAG + " onStartCommand");

        final Observable<NewsResponse> call = newsService.newsWireArticles("world", 10, 0, //our offset
                getResources().getString(R.string.nyt_api_key_newswire));

        call.compose(RxHelper.applySchedulers())
                .retry(10)
                .subscribe(new Subscriber<NewsResponse>() {
                    @Override
                    public void onCompleted() {

                        Log.d(TAG, "response received: " + fetchArticleList.size() + " newsWireArticles received");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        ApiError error = new ApiError();
                        error.setMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(NewsResponse newsResponse) {

                        fetchArticleList = (ArrayList<Article>) newsResponse.getArticles();

                        for (Article article : fetchArticleList) {

                            insertFacetData(NO_ARTICLE_ID, article.getPerFacet());
                            insertFacetData(NO_ARTICLE_ID, article.getOrgFacet());
                            insertFacetData(NO_ARTICLE_ID, article.getDesFacet());
                            insertFacetData(NO_ARTICLE_ID, article.getGeoFacet());

                        }

                    }
                });

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private List<Facet> insertFacetData(long articleId, List<Facet> facets) {

        for(Facet facet : facets) {

            if(articleId == NO_ARTICLE_ID) { //we set our articles to -1 when we are just pulling Facets for our Trending
                if(facet.getFacetText() != null && facet.getFacetType() != null && facet.getCreatedDate() != null) {
                    facet.setStoryId(NO_ARTICLE_ID);

                    Uri insertedUri = getContentResolver()
                            .insert(DatabaseContract.FacetTable.CONTENT_URI, facet.getContentValues());
                }
            } else {
                facet.setStoryId((int) articleId + 1);
                Uri insertedUri = getContentResolver()
                        .insert(DatabaseContract.FacetTable.CONTENT_URI, facet.getContentValues());
            }
        }

        return facets;
    }
}
