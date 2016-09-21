package com.androidtitan.culturedapp.main.toparticle;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;

import javax.inject.Inject;

/**
 * Created by amohnacs on 9/19/16.
 */

public class TopArticleProvider implements TopArticleMvp.Provider, LoaderManager.LoaderCallbacks<Cursor> {
    private final String TAG = getClass().getSimpleName();

    private Context context;

    @Inject
    public TopArticleProvider(Context context) {
        this.context = context;

    }

    @Override
    public void fetchTopArticleImage(CallbackListener listener) {

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private Bitmap generateBitmap(String url) {

        return null;
    }
}
