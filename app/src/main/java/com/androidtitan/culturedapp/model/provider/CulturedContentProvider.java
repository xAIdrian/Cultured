package com.androidtitan.culturedapp.model.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by amohnacs on 7/17/16.
 */

public class CulturedContentProvider extends android.content.ContentProvider {
    private final String TAG = getClass().getSimpleName();

    private static final int ARTICLE_ID = 1;
    private static final int ARTICLE_LIST = 2;
    private static final UriMatcher uriMatcher;

    Context context;
    SQLiteHelper sqLiteHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.Article.TABLE_NAME + "/#", ARTICLE_ID);
        uriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.Article.TABLE_NAME, ARTICLE_LIST);

    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate");

        context = getContext();
        sqLiteHelper = new SQLiteHelper(context);

        return true;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query with uri: " + uri.toString());

        String tableName;
        Cursor cursor;

        tableName = DatabaseContract.Article.TABLE_NAME;

        if(TextUtils.isEmpty(sortOrder)) {
            sortOrder = DatabaseContract.Article._ID + " DESC";

        }
        cursor = sqLiteHelper.getReadableDatabase()
                .query(tableName, projection, selection, selectionArgs, null, null, sortOrder);

       /* switch (uriMatcher.match(uri)) {
            case ARTICLE_LIST:

                tableName = DatabaseContract.Article.TABLE_NAME;

                if(TextUtils.isEmpty(sortOrder)) {
                    sortOrder = DatabaseContract.Article._ID + " DESC";

                }
                cursor = sqLiteHelper.getReadableDatabase()
                        .query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Unsupported Uri: " + uri);
        }*/

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ARTICLE_ID:
                return DatabaseContract.SINGLE_CONTENT_TYPE;

            case ARTICLE_LIST:
                return DatabaseContract.LIST_CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("unsupported Uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert uri: " + uri.toString());

        long insertedRowId = 0;

        switch (uriMatcher.match(uri)) {
            case ARTICLE_ID:

                insertedRowId = sqLiteHelper.getWritableDatabase()
                        .insert(DatabaseContract.Article.TABLE_NAME, null, values);

                break;

            case ARTICLE_LIST:

                insertedRowId = sqLiteHelper.getWritableDatabase()
                        .insert(DatabaseContract.Article.TABLE_NAME, null, values);

                break;

            default:
                throw new IllegalArgumentException("unsupported Uri: " + uri);
        }

        if(insertedRowId > 0) {
            notifyUriChanges(uri);
            return ContentUris.withAppendedId(uri, insertedRowId);
        } else {
            return null;
        }
    }



    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(TAG, "update uri: " + uri.toString());

        int updateCount = 0;

        switch (uriMatcher.match(uri)) {

            case ARTICLE_ID:
                updateCount = sqLiteHelper.getWritableDatabase().delete(
                        DatabaseContract.Article.TABLE_NAME, selection, selectionArgs);
                break;

            case ARTICLE_LIST:
                updateCount = sqLiteHelper.getWritableDatabase().delete(
                        DatabaseContract.Article.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        if(updateCount > 0) {
            notifyUriChanges(uri);
        }

        return updateCount;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete uri: " + uri.toString());

        int deleteCount;

        switch (uriMatcher.match(uri)) {

            case ARTICLE_ID:
                deleteCount = sqLiteHelper.getWritableDatabase().delete(
                        DatabaseContract.Article.TABLE_NAME, selection, selectionArgs);
                break;

            case ARTICLE_LIST:
                deleteCount = sqLiteHelper.getWritableDatabase().delete(
                        DatabaseContract.Article.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        if(deleteCount > 0) {
            notifyUriChanges(uri);
        }
        return deleteCount;
    }

    public void updateContext(Context context, ContentProviderInterface.ProviderCallback listener) {
        this.context = context;
        sqLiteHelper = new SQLiteHelper(context);
        Log.d(TAG, "Lazy creation of SQLiteOpenHelper");
        if(sqLiteHelper == null) {
            listener.SQLiteCreationComplete(false);
        } else {
            listener.SQLiteCreationComplete(true);
        }
    }

    private void notifyUriChanges(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }
}


