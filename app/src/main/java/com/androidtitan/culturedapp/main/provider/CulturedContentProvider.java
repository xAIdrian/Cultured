package com.androidtitan.culturedapp.main.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import static android.content.ContentResolver.CURSOR_DIR_BASE_TYPE;
import static com.androidtitan.culturedapp.main.provider.DatabaseContract.AUTHORITY;

/**
 * Created by amohnacs on 7/17/16.
 */

public class CulturedContentProvider extends android.content.ContentProvider {
    private final String TAG = getClass().getSimpleName();


    public static final String SINGLE_CONTENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "vnd." + AUTHORITY;
    public static final String LIST_CONTENT_TYPE = CURSOR_DIR_BASE_TYPE + "vnd." + AUTHORITY;

    private static final int ARTICLE = 1;
    private static final int ARTICLE_LIST = 2;

    private static final int MEDIA = 3;
    private static final int MEDIA_LIST = 4;

    private static final UriMatcher uriMatcher;

    Context context;
    SQLiteHelper sqLiteHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, DatabaseContract.ArticleTable.TABLE_NAME + "/#", ARTICLE);
        uriMatcher.addURI(AUTHORITY, DatabaseContract.ArticleTable.TABLE_NAME, ARTICLE_LIST);

        uriMatcher.addURI(AUTHORITY, DatabaseContract.MediaTable.TABLE_NAME + "/#", MEDIA);
        uriMatcher.addURI(AUTHORITY, DatabaseContract.MediaTable.TABLE_NAME, MEDIA_LIST);
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate");

        context = getContext();
        sqLiteHelper = new SQLiteHelper(context);

        return true;

    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ARTICLE:
                return SINGLE_CONTENT_TYPE + DatabaseContract.ArticleTable.TABLE_NAME;

            case ARTICLE_LIST:
                return LIST_CONTENT_TYPE + DatabaseContract.ArticleTable.TABLE_NAME;

            case MEDIA:
                return SINGLE_CONTENT_TYPE + DatabaseContract.MediaTable.TABLE_NAME;

            case MEDIA_LIST:
                return LIST_CONTENT_TYPE + DatabaseContract.MediaTable.TABLE_NAME;

            default:
                throw new IllegalArgumentException("unsupported Uri: " + uri);
        }
    }

    /////
    // todo:
    // ContentProvider methods we need to add additional support for the querying of a single item
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query with uri: " + uri.toString());

        String tableName;
        Cursor cursor;

        if(TextUtils.isEmpty(sortOrder)) {
            sortOrder = "_ID" + " DESC";

        }

       switch (uriMatcher.match(uri)) {

            case ARTICLE_LIST:

                tableName = DatabaseContract.ArticleTable.TABLE_NAME;

                if(TextUtils.isEmpty(sortOrder)) {
                    sortOrder = "_ID DESC";

                }
                cursor = sqLiteHelper.getReadableDatabase()
                        .query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
                break;

           case MEDIA_LIST:

               tableName = DatabaseContract.MediaTable.TABLE_NAME;

               if(TextUtils.isEmpty(sortOrder)) {
                   sortOrder = "_ID DESC";

               }
               cursor = sqLiteHelper.getReadableDatabase()
                       .query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
               break;

            default:
                throw new IllegalArgumentException("Unsupported Uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert uri: " + uri.toString());

        long insertedRowId = 0;

        switch (uriMatcher.match(uri)) {
            case ARTICLE_LIST:

                insertedRowId = sqLiteHelper.getWritableDatabase()
                        .insert(DatabaseContract.ArticleTable.TABLE_NAME, null, values);

                break;

            case MEDIA_LIST:

                insertedRowId = sqLiteHelper.getWritableDatabase()
                        .insert(DatabaseContract.MediaTable.TABLE_NAME, null, values);
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

            case ARTICLE_LIST:
                updateCount = sqLiteHelper.getWritableDatabase().delete(
                        DatabaseContract.ArticleTable.TABLE_NAME, selection, selectionArgs);
                break;

            case MEDIA_LIST:

                updateCount = sqLiteHelper.getWritableDatabase()
                        .delete(DatabaseContract.MediaTable.TABLE_NAME, selection, selectionArgs);
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

            case ARTICLE_LIST:
                deleteCount = sqLiteHelper.getWritableDatabase().delete(
                        DatabaseContract.ArticleTable.TABLE_NAME, selection, selectionArgs);
                break;

            case MEDIA_LIST:

                deleteCount = sqLiteHelper.getWritableDatabase()
                        .delete(DatabaseContract.MediaTable.TABLE_NAME, selection, selectionArgs);
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


