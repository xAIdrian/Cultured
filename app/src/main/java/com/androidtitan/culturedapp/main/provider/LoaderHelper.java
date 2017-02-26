package com.androidtitan.culturedapp.main.provider;


import android.content.CursorLoader;

import com.androidtitan.culturedapp.main.CulturedApp;

import java.security.InvalidParameterException;

import static com.androidtitan.culturedapp.common.Constants.ARTICLE_LOADER_ID;
import static com.androidtitan.culturedapp.common.Constants.TOP_ARTICLE_FACET_LOADER_ID;
import static com.androidtitan.culturedapp.common.Constants.TOP_ARTICLE_MEDIA_LOADER_ID;
import static com.androidtitan.culturedapp.common.Constants.TRENDING_FACET_LOADER_ID;
import static com.androidtitan.culturedapp.main.CulturedApp.getAppContext;

/**
 * Created by Adrian Mohnacs on 2/5/17.
 */

public class LoaderHelper {
    private static final String TAG = LoaderHelper.class.getSimpleName();

    // we'll create a method for creating a loader with specific querying options when we cross that bridge.
    public CursorLoader createBasicCursorLoader(int loaderId) {

         CursorLoader cursorLoader;

        switch (loaderId) {
            case ARTICLE_LOADER_ID:

                cursorLoader = new CursorLoader(getAppContext(), DatabaseContract.ArticleTable.CONTENT_URI,
                        null, null, null, null);

                break;

            case TOP_ARTICLE_MEDIA_LOADER_ID:

                cursorLoader = new CursorLoader(getAppContext(), DatabaseContract.MediaTable.CONTENT_URI,
                        null, null, null, null);

                break;

            case TOP_ARTICLE_FACET_LOADER_ID:

                String[] projection = new String[] {DatabaseContract.FacetTable.STORY_ID,
                        DatabaseContract.FacetTable.TYPE, DatabaseContract.FacetTable.FACET,
                        DatabaseContract.FacetTable.CREATED_DATE};
                String topArticleSelection = DatabaseContract.FacetTable.STORY_ID + " IS NOT NULL";

                cursorLoader = new android.content.CursorLoader(getAppContext(), DatabaseContract.FacetTable.CONTENT_URI,
                        projection, topArticleSelection, null, null);
                break;

            case TRENDING_FACET_LOADER_ID:

                String trendingSelection = DatabaseContract.FacetTable.STORY_ID + " = ?";
                String[] selectionArgs = {"1000"};
                String sortOrder = DatabaseContract.FacetTable.CREATED_DATE + " DESC";

                cursorLoader = new CursorLoader(getAppContext(), DatabaseContract.FacetTable.CONTENT_URI,
                        null, trendingSelection, null, sortOrder);

                break;

            default:
                throw new InvalidParameterException(TAG + " LoaderId is not valid");
        }
        return cursorLoader;
    }
}
