package com.androidtitan.culturedapp.main.toparticle;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.structure.BaseActivity;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Multimedium;
import com.androidtitan.culturedapp.model.provider.wrappers.ArticleCursorWrapper;
import com.androidtitan.culturedapp.model.provider.DatabaseContract;
import com.androidtitan.culturedapp.model.provider.wrappers.MultimediumCursorWrapper;

import java.util.ArrayList;

import static com.androidtitan.culturedapp.common.Constants.ARTICLE_LOADER_ID;
import static com.androidtitan.culturedapp.common.Constants.MEDIA_LOADER_ID;

public class TopArticleActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        TopArticleMvp.View{
    private final String TAG = getClass().getSimpleName();

    private TopArticleCursorAdapter topArticleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //todo: can we just pass in an empty Cursor for starters instead of NULL?
        topArticleCursorAdapter = new TopArticleCursorAdapter(this, null);

        getLoaderManager().initLoader(ARTICLE_LOADER_ID, null, this);
        getLoaderManager().initLoader(MEDIA_LOADER_ID, null, this);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case ARTICLE_LOADER_ID:

                return new CursorLoader(this, DatabaseContract.ArticleTable.CONTENT_URI,
                        null, null, null, null);

            case MEDIA_LOADER_ID:

                return new CursorLoader(this, DatabaseContract.MediaTable.CONTENT_URI,
                        null, null, null, null);

            default:
                throw new IllegalArgumentException("You have passed in an illegal Loader ID");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        ArrayList<Article> articles = new ArrayList<>();
        ArrayList<Multimedium> media = new ArrayList<>();

        switch(loader.getId()) {

            case ARTICLE_LOADER_ID:

                // this cursor loads Articles minus media
                if(cursor != null && cursor.getCount() > 0) {

                    ArticleCursorWrapper wrapper = new ArticleCursorWrapper(cursor);
                    wrapper.moveToFirst();
                    while(!wrapper.isAfterLast()) {

                        Article article = wrapper.getArticle();
                        articles.add(article);

                        //Log.e(TAG, article.getId() + " : " + article.getTitle());

                        wrapper.moveToNext();
                    }

                } else {

                    Log.e(TAG, "No articles were returned from the TopArticle search");
                }

                break;

            case MEDIA_LOADER_ID:

                // this cursor loads Media
                if(cursor != null && cursor.getCount() > 0) {

                    MultimediumCursorWrapper wrapper = new MultimediumCursorWrapper(cursor);
                    wrapper.moveToFirst();
                    while(!wrapper.isAfterLast()) {

                        Multimedium multimedium = wrapper.getMultimedium();
                        media.add(multimedium);

                        //Log.e(TAG, "media : " + multimedium.getStoryId() + " : " + multimedium.getCaption());

                        wrapper.moveToNext();
                    }

                } else {

                    Log.e(TAG, "No Media were returned from the TopArticle search");
                }

                break;

            default:
                throw new IllegalArgumentException("You have passed in an illegal Loader ID : " + loader.getId());
        }

        /*  todo:
        we are going to piece together the Article and it's pieces here and not in the Wrappers
        CursorLoaders refresh their data asyncronously whenever the CP has new data,
            meaning we don't know EXACTLY when it's going to refresh

         */

        topArticleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
// We have yet to build the display for all of our data!
// ((SimpleCursorAdapter)this.getListAdapter()).
//                swapCursor(cursor);

        topArticleCursorAdapter.changeCursor(null);
    }

    @Override
    public void displayTopArticleImage(Bitmap bitmap) {

    }
}