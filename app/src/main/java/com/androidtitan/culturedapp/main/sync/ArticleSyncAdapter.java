package com.androidtitan.culturedapp.main.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.main.domain.retrofit.NewsEndpoint;
import com.androidtitan.culturedapp.main.domain.retrofit.ServiceGenerator;
import com.androidtitan.culturedapp.model.provider.DatabaseContract;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.NewsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amohnacs on 8/7/16.
 */

public class ArticleSyncAdapter extends AbstractThreadedSyncAdapter {
    private final String TAG = getClass().getSimpleName();

    private Context context;

    private final NewsEndpoint newsService;
    private ContentResolver contentResolver;

    public ArticleSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        this.context = context;

        contentResolver = context.getContentResolver();
        newsService = ServiceGenerator.createService(NewsEndpoint.class);

    }

    public ArticleSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        this.context = context;

        contentResolver = context.getContentResolver();
        newsService = ServiceGenerator.createService(NewsEndpoint.class);

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

        //check to make sure how fresh it is...check your first article returned against
        //      the top article in the content provider
        //clean up

        clearDd();
        Log.d(TAG, "SyncAdapter onPerformSync()");
        List<Article> articles = fetchTopArticles(5);

    }

    private ArrayList<Article> fetchTopArticles(final int limit) {

        final ArrayList<Article> articles = new ArrayList<>();
        final Call<NewsResponse> call = newsService.topStories("world", context.getResources().getString(R.string.nyt_api_key_topstories));

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse resp = response.body();
                    Log.d(TAG, "response received: " + resp.getStatus() + " : "
                            + resp.getArticles().size() + " topArticles received");

                    ArrayList<Article> insidArticles = (ArrayList<Article>) resp.getArticles();

                    for (int i = 0; i < limit; i++) {

                        articles.add(insidArticles.get(i));
                    }

                    insertArticleData(articles);

                } else {
                    Log.e(TAG, "response fail : " + response.message());
                }

            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return articles;
    }

    private void insertArticleData(List<Article> articles) {

        for(Article article : articles) {

            Uri insertedUri = getContext().getContentResolver()
                    .insert(DatabaseContract.Article.CONTENT_URI, article.getContentValues());

            if(insertedUri != null ) {
                Log.e(TAG, insertedUri.toString());
            } else {
                Log.e(TAG, "empty");
            }


        }
    }

    private void clearDd() {
        Log.e(TAG, "clearing Database sir");
        context.getContentResolver().delete(DatabaseContract.Article.CONTENT_URI, null, null);
    }
}
