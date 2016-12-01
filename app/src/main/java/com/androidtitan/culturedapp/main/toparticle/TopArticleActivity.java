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
import com.androidtitan.culturedapp.model.provider.wrappers.ArticleCursorWrapper;
import com.androidtitan.culturedapp.model.provider.DatabaseContract;

import java.util.ArrayList;

import static com.androidtitan.culturedapp.common.Constants.ARTICLE_LOADER_ID;

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

        //todo: can we just pass in an empty Cursor for starters?
        topArticleCursorAdapter = new TopArticleCursorAdapter(this, null);

        getLoaderManager().initLoader(ARTICLE_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case ARTICLE_LOADER_ID:

                    return new CursorLoader(this, DatabaseContract.CONTENT_URI,
                            null, null, null, null);

            default:
                throw new IllegalArgumentException("You have passed in an illegal Loader ID");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        ArrayList<Article> articles = new ArrayList<>();

        if(cursor != null && cursor.getCount() > 0) {

            ArticleCursorWrapper wrapper = new ArticleCursorWrapper(cursor);
            wrapper.moveToFirst();
            while(!wrapper.isAfterLast()) {

                Article article = wrapper.getArticle();

                articles.add(article);

                wrapper.moveToNext();
            }

        } else {

            Log.e(TAG, "Uh Oh. No articles were returned from the TopArticle search");
        }

        //todo: we are going to piece together the Article and it's pieces here and not in the Wrappers

        topArticleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        ((SimpleCursorAdapter)this.getListAdapter()).
//                swapCursor(cursor);

        topArticleCursorAdapter.changeCursor(null);
    }

    @Override
    public void displayTopArticleImage(Bitmap bitmap) {

    }
}
