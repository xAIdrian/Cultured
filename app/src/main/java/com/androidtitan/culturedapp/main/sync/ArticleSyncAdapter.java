package com.androidtitan.culturedapp.main.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.structure.RxHelper;
import com.androidtitan.culturedapp.main.web.retrofit.NewsEndpoint;
import com.androidtitan.culturedapp.main.web.retrofit.ServiceGenerator;
import com.androidtitan.culturedapp.model.newyorktimes.Multimedium;
import com.androidtitan.culturedapp.model.provider.DatabaseContract;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.NewsResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static com.androidtitan.culturedapp.common.Constants.PREFERENCES_ARTICLE_ID;
import static com.androidtitan.culturedapp.main.newsfeed.NewsAdapter.CULTURED_PREFERENCES;

/**
 * Created by amohnacs on 8/7/16.
 */

public class ArticleSyncAdapter extends AbstractThreadedSyncAdapter {
    private final String TAG = getClass().getSimpleName();

    private Context context;

    private final NewsEndpoint newsService;
    private ContentResolver contentResolver;
    private SharedPreferences preferences;

    int currentId;

    public ArticleSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        this.context = context;

        contentResolver = context.getContentResolver();
        newsService = ServiceGenerator.createService(NewsEndpoint.class);
        preferences = context.getSharedPreferences(CULTURED_PREFERENCES, Context.MODE_PRIVATE);

        currentId = 0;

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

        /* todo:
            check to make sure how fresh it is...check your first article returned against
                  the top article in the content provider
            clean up
        */

        clearDdValues();
        Log.d(TAG, "SyncAdapter onPerformSync()");
        List<Article> articles = fetchTopArticles(10);

    }

    private ArrayList<Article> fetchTopArticles(final int limit) {

        currentId = preferences.getInt(PREFERENCES_ARTICLE_ID, 0);

        final ArrayList<Article> articles = new ArrayList<>();
        final Observable<NewsResponse> call = newsService.topStories("world", context.getResources().getString(R.string.nyt_api_key_topstories));

        call.compose(RxHelper.applySchedulers())
                .retry(10)
                .doOnError(e -> {
                    e.printStackTrace();
                })
                .subscribe(new Subscriber<NewsResponse>() {
                    @Override
                    public void onCompleted() {

                        Log.d(TAG, "response received: " + articles.size() + " topArticles received");

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(NewsResponse newsResponse) {

                        ArrayList<Article> insideArticles = (ArrayList<Article>) newsResponse.getArticles();

                        for (int i = 0; i < limit; i++) {

                            Article articleAtHand = insideArticles.get(i);
                            articleAtHand.setId(currentId);

                            articles.add(articleAtHand);
                            //just a heads up this is a double for loop :/
                            insertMultimediumData(articleAtHand.getId(), articleAtHand.getMultimedia());
                            currentId++;
                        }

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt(PREFERENCES_ARTICLE_ID, currentId);
                        editor.apply();

                        insertArticleData(articles);
                        //todo: insertFacetData
                    }
                });

        return articles;

    }

    private List<Article> insertArticleData(List<Article> articles) {

        for(Article article : articles) {

            Uri insertedUri = getContext().getContentResolver()
                    .insert(DatabaseContract.ArticleTable.CONTENT_URI, article.getArticleContentValues());

        }

        return articles;
    }

    private List<Multimedium> insertMultimediumData(long articleId, List<Multimedium> multimedia) {

        for(Multimedium multimedium : multimedia) {

            multimedium.setStoryId(articleId + 1);
            Uri insertedUri = getContext().getContentResolver()
                    .insert(DatabaseContract.MediaTable.CONTENT_URI, multimedium.getContentValues());
        }


        return null;
    }

    private void clearDdValues() {
        Log.d(TAG, "clearing all database values..");

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREFERENCES_ARTICLE_ID, 0);
        editor.apply();

        context.getContentResolver().delete(DatabaseContract.ArticleTable.CONTENT_URI, null, null);
        context.getContentResolver().delete(DatabaseContract.MediaTable.CONTENT_URI, null, null);
    }
}
