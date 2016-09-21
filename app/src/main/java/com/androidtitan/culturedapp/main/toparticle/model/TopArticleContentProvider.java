package com.androidtitan.culturedapp.main.toparticle.model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.androidtitan.culturedapp.main.toparticle.ContentProviderInterface;

/**
 * Created by amohnacs on 7/17/16.
 */

public class TopArticleContentProvider extends ContentProvider {
    private final String TAG = getClass().getSimpleName();

    private final static String AUTHORITY = "com.androidtitan.culturedapp.provider";
    private static String BASE_PATH = TopArticleSQLiteHelper.DATABASE_NAME;
    private static String path_CONTENT_URI =  "content://" + AUTHORITY+ "/" + BASE_PATH +"/";
    static final String SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd.marakana.android.lifecycle.status";
    static final String MULTIPLE_RECORDS_MIME_TYPE = "vnd.android.cursor.dir/vnd.marakana.android.lifecycle.status";

    public static final Uri CONTENT_URI = Uri.parse(path_CONTENT_URI);

    TopArticleSQLiteHelper haloSqlHelper;
    TopArticleTable.TopArticleEntry topArticleEntry;

    Context context;

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate");

        context = getContext();

        return true;

    }

    @Override
    public String getType(Uri uri) {
        String ret = context.getContentResolver().getType(CONTENT_URI);
        Log.d(TAG, "getType returning: " + ret);
        return ret;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert uri: " + uri.toString());

        SQLiteDatabase db = haloSqlHelper.getWritableDatabase();

        Uri result = null;

        long rowID = db.insert(topArticleEntry.TABLE_NAME, null, values);

        if (rowID > 0) {
            // Return a URI to the newly created row on success
            result = ContentUris.withAppendedId(CONTENT_URI, rowID);

            // Notify the Context's ContentResolver of the change
            context.getContentResolver().notifyChange(result, null);
        }
        return result;
    }



    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(TAG, "update uri: " + uri.toString());

        int count = 0;
        SQLiteDatabase db = haloSqlHelper.getWritableDatabase();

        count = db.update(topArticleEntry.TABLE_NAME, values, selection, selectionArgs);

        if(count > 0) {

            context.getContentResolver().notifyChange(uri, null);
        }

        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete uri: " + uri.toString());

        int count = 0;
        SQLiteDatabase db = haloSqlHelper.getWritableDatabase();

        count = db.delete(topArticleEntry.TABLE_NAME, selection, selectionArgs);

        if (count > 0) {
            context.getContentResolver().notifyChange(uri, null);
        }

        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query with uri: " + uri.toString());

        SQLiteDatabase db = haloSqlHelper.getWritableDatabase();

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(topArticleEntry.TABLE_NAME);

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null ,null, sortOrder);

        return cursor;
    }

    public void updateContext(Context context, ContentProviderInterface.ProviderCallback listener) {
        this.context = context;
        haloSqlHelper = new TopArticleSQLiteHelper(context);
        Log.d(TAG, "Lazy creation of SQLiteOpenHelper");
        if(haloSqlHelper == null) {
            listener.SQLiteCreationComplete(false);
        } else {
            listener.SQLiteCreationComplete(true);
        }
    }
}


