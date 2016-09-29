package com.androidtitan.culturedapp.main.toparticle;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;

/**
 * Created by amohnacs on 9/19/16.
 */

/*
    todo:

        Here we are going to get the data from our
        Content Provider that is going to be linked to `ArticleContentProvider` pulling data from a
        Service that will periodically fetch the five most recent Top Stories and then stores them in the Content Provider
 */
public class ArticleLoader extends CursorLoader {
    private final String TAG = getClass().getSimpleName();



    public ArticleLoader(Context context) {
        super(context);
    }

    public ArticleLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }
}
