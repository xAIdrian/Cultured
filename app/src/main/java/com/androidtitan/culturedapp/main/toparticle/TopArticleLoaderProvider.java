package com.androidtitan.culturedapp.main.toparticle;

import android.content.Context;
import android.content.CursorLoader;
import android.support.annotation.NonNull;

import com.androidtitan.culturedapp.main.provider.DatabaseContract;

import java.security.InvalidParameterException;

import javax.inject.Inject;

import static com.androidtitan.culturedapp.common.Constants.ARTICLE_LOADER_ID;
import static com.androidtitan.culturedapp.common.Constants.TOP_ARTICLE_FACET_LOADER_ID;
import static com.androidtitan.culturedapp.common.Constants.TOP_ARTICLE_MEDIA_LOADER_ID;
import static com.androidtitan.culturedapp.common.Constants.TRENDING_FACET_LOADER_ID;

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

            case TOP_ARTICLE_MEDIA_LOADER_ID:

                cursorLoader = new CursorLoader(context, DatabaseContract.MediaTable.CONTENT_URI,
                        null, null, null, null);

                break;

            case TOP_ARTICLE_FACET_LOADER_ID:

                String[] projection = new String[] {DatabaseContract.FacetTable.STORY_ID,
                        DatabaseContract.FacetTable.TYPE, DatabaseContract.FacetTable.FACET};
                String topArticleSelection = DatabaseContract.FacetTable.STORY_ID + " IS NOT NULL";

                cursorLoader = new CursorLoader(context, DatabaseContract.FacetTable.CONTENT_URI,
                        projection, topArticleSelection, null, null);
                break;

            case TRENDING_FACET_LOADER_ID:

                String trendingSelection = DatabaseContract.FacetTable.STORY_ID + " IS NULL";
                String sortOrder = DatabaseContract.FacetTable.CREATED_DATE + " DESC";

                cursorLoader = new CursorLoader(context, DatabaseContract.FacetTable.CONTENT_URI,
                        null, trendingSelection, null, sortOrder);
                break;

            default:
                throw new InvalidParameterException(TAG + " LoaderId is not valid");
        }
        return cursorLoader;
    }

    // we'll create a method for creating a loader with specific querying options when we cross that bridge.
}
