package com.androidtitan.culturedapp.main.toparticle;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.androidtitan.culturedapp.model.provider.DatabaseContract;

import java.security.InvalidParameterException;

import javax.inject.Inject;

import static com.androidtitan.culturedapp.common.Constants.ARTICLE_LOADER_ID;
import static com.androidtitan.culturedapp.common.Constants.MEDIA_LOADER_ID;

/**
 * Created by amohnacs on 9/19/16.
 */

public class TopArticleLoaderProvider implements TopArticleMvp.Provider {
    private final String TAG = getClass().getSimpleName();

    @NonNull
    private Context context;

    @Inject
    public TopArticleLoaderProvider(Context context) {
        this.context = context;

    }

    public CursorLoader createBasicCursorLoader(int loaderId) {

        CursorLoader cursorLoader;

        switch (loaderId) {
            case ARTICLE_LOADER_ID:

                cursorLoader = new CursorLoader(context, DatabaseContract.ArticleTable.CONTENT_URI,
                        null, null, null, null);

                break;

            case MEDIA_LOADER_ID:

                cursorLoader = new CursorLoader(context, DatabaseContract.MediaTable.CONTENT_URI,
                        null, null, null, null);

                break;

            default:
                throw new InvalidParameterException(TAG + " LoaderId is not valid");
        }
        return cursorLoader;
    }

    // we'll create a method for creating a loader with specific querying options when cross that bridge.
}
