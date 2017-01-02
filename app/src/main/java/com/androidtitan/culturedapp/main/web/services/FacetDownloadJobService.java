package com.androidtitan.culturedapp.main.web.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.net.Uri;
import android.os.AsyncTask;
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
 * Created by Adrian Mohnacs on 12/31/16.
 */

public class FacetDownloadJobService extends JobService {
    private final String TAG = getClass().getSimpleName();

    private NewsEndpoint newsService;
    FacetDownloadAsyncTask facetDownloadAsyncTask;

    private ArrayList<Article> fetchArticleList = new ArrayList<>();

    @Override
    public boolean onStartJob(JobParameters params) {
        newsService = ServiceGenerator.createService(NewsEndpoint.class);

        facetDownloadAsyncTask.execute(params);

        return true;
        /*
         If the return value is true, then the system assumes that the task is going to take some time
         and the burden falls on you, the developer, to tell the system when the given task is complete
          by calling jobFinished(JobParameters params, boolean needsRescheduled).
         */
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        facetDownloadAsyncTask.cancel(true); //should interrupt the running thread if cancel is called
        return true;
    }

    /*
    The three types used by an asynchronous task are the following:

        Params, the type of the parameters sent to the task upon execution.
        Progress, the type of the progress units published during the background computation.
        Result, the type of the result of the background computation.
     */
    private class FacetDownloadAsyncTask extends AsyncTask<JobParameters, Void, JobParameters> {

        @Override
        protected JobParameters doInBackground(JobParameters... params) {

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

            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {

            jobFinished(jobParameters, true);
        }
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

