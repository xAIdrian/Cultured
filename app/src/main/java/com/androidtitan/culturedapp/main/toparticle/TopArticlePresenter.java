package com.androidtitan.culturedapp.main.toparticle;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.util.Log;

import com.androidtitan.culturedapp.common.structure.BasePresenter;
import com.androidtitan.culturedapp.main.CulturedApp;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Multimedium;
import com.androidtitan.culturedapp.main.provider.wrappers.ArticleCursorWrapper;
import com.androidtitan.culturedapp.main.provider.wrappers.MultimediumCursorWrapper;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.androidtitan.culturedapp.common.Constants.ARTICLE_LOADER_ID;
import static com.androidtitan.culturedapp.common.Constants.MEDIA_LOADER_ID;

/**
 * Created by amohnacs on 9/19/16.
 */

//The functionality that the PRESENTER contains should be driven by the Business Requirements.

    /*
    https://github.com/googlesamples/android-architecture/tree/todo-mvp-contentproviders

    Because we do not need to tie our Loader to the lifecycle of activities and fragments , which is why we use
    LoaderManager, we can interact directly with the Loader in the Presenter.

    The Presenter will be created well before our activity before it is being manufactured by Dagger 2.
    We will store the list of Articles inside the presenter and the activity can reach up for them when
    it is ready.

     */

public class TopArticlePresenter extends BasePresenter<TopArticleMvp.View> implements TopArticleMvp.Presenter,
        TopArticleMvp.Provider.CallbackListener, Loader.OnLoadCompleteListener<Cursor> {

    private final String TAG = getClass().getSimpleName();

    private Context context;

    private ArrayList<Article> articles = new ArrayList<>();
    private ArrayList<Multimedium> media = new ArrayList<>();

    @Inject
    TopArticleLoaderProvider topArticleLoaderProvider;

    CursorLoader articleCursorLoader;
    CursorLoader mediaCursorLoader;

    public TopArticlePresenter(Context context) {
        this.context = context;

        CulturedApp.getAppComponent().inject(this);

        articleCursorLoader = topArticleLoaderProvider.createBasicCursorLoader(ARTICLE_LOADER_ID);
        articleCursorLoader.registerListener(ARTICLE_LOADER_ID, this);
        articleCursorLoader.startLoading();
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {

        switch (loader.getId()) {

            case ARTICLE_LOADER_ID:

                // this cursor loads Articles minus media
                if (cursor != null && cursor.getCount() > 0) {

                    ArticleCursorWrapper wrapper = new ArticleCursorWrapper(cursor);
                    wrapper.moveToFirst();
                    while (!wrapper.isAfterLast()) {

                        Article article = wrapper.getArticle();
                        articles.add(article);
                        //Log.e(TAG, article.getId() + " : " + article.getTitle());
                        wrapper.moveToNext();
                    }
                }

                //start loading our media here to ensure that we fetch the media post articles
                mediaCursorLoader = topArticleLoaderProvider.createBasicCursorLoader(MEDIA_LOADER_ID);
                mediaCursorLoader.registerListener(MEDIA_LOADER_ID, this);
                mediaCursorLoader.startLoading();

                break;

            case MEDIA_LOADER_ID:

                if (cursor != null && cursor.getCount() > 0) {
                    MultimediumCursorWrapper wrapper = new MultimediumCursorWrapper(cursor);
                    wrapper.moveToFirst();
                    while (!wrapper.isAfterLast()) {

                        Multimedium multimedium = wrapper.getMultimedium();
                        media.add(multimedium);
                        //Log.e(TAG, "media : " + multimedium.getStoryId() + " : " + multimedium.getCaption());
                        wrapper.moveToNext();
                    }
                }
                articles = getMergedArticles(articles, media);

                if(getMvpView() != null) {
                    sendDownArticles(articles);
                }

                break;

            default:
                throw new IllegalArgumentException("You have passed in an illegal Loader ID : " + loader.getId());
        }
    }

    /**
     * We will always have fresh data from remote, the Loaders handle the local data
     */
    public void loadArticles() {

        articleCursorLoader.startLoading();
    }

    public void sendDownArticles(ArrayList<Article> articleList) {
        //we are using an array here because we were getting a ConcurrentModificationException
        //when we just removed the article while we were iterating through the list
        ArrayList<Article> deletes = new ArrayList<>();
        if (articleList != null) {
            if (articleList.size() > 0) {

                //we only want to work with Articles that have images to display
                for(int i = 0; i < articleList.size(); i++) {
                    if(articleList.get(i).getMultimedia().size() <= 0) {
                        deletes.add(articleList.get(i));
                    }
                }

                articleList.removeAll(deletes);

                getMvpView().updateArticles(articleList);
            } else {
                getMvpView().cursorDataEmpty();
                Log.e(TAG, "No articles were returned");
            }
        } else {
            Log.e(TAG, "Articles not instantiated");
            getMvpView().cursorDataNotAvailable();
        }
    }

    private ArrayList<Article> getMergedArticles(ArrayList<Article> articles, ArrayList<Multimedium> media) {

        for (Article article : articles) {
            ArrayList<Multimedium> tempMultimedium = new ArrayList<>();
            for (Multimedium multimedium : media) {
                if (multimedium.getStoryId() == article.getId()) {
                    tempMultimedium.add(multimedium);
                }
                article.setMultimedia(tempMultimedium);
            }
        }
        return articles;
    }

    @Override
    public void onDestroy() {
        // Stop the cursor loader
        if (articleCursorLoader != null) {
            articleCursorLoader.unregisterListener(this);
            articleCursorLoader.cancelLoad();
            articleCursorLoader.stopLoading();
        }
    }
}
