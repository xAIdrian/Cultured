package com.androidtitan.hotspots.Provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.androidtitan.hotspots.Data.DatabaseHelper;

/**
 * Created by amohnacs on 8/24/15.
 */

public class VenueProvider extends ContentProvider {
    public static final String TAG = "CustomContentProvider";

    DatabaseHelper databaseHelper;


    private static final String AUTHORITY = "com.androidtitan.hotspots.Provider.VenueProvider";
    private static final String BASE_PATH = DatabaseHelper.TABLE_VENUES;

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    //MIME Types for getting a single item or a list of them
    //todo:we night to change the string at the end
    public static final String VENUES_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/vnd.com.androidtitan.Data.venues";
    public static final String VENUE_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/vnd.com.androidtitan.Data.venues";

    //Column names
    public static class InterfaceConstants {
        public static final String id = "_id";
        public static final String venue_name = "venue_name";
        public static final String venue_city = "venue_city";
        public static final String venue_category = "venue_category";
        public static final String venue_id_string = "venue_string";
        public static final String venue_rating = "venue_rating";
    }

    //URI matcher variable
    private static final int GET_ALL = 0;
    private static final int GET_ONE = 1;

    static UriMatcher uriMatcher = BuildUriMatcher();

    static UriMatcher BuildUriMatcher() {

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        //Uri to match and the code to return when matched

        matcher.addURI(AUTHORITY, BASE_PATH, GET_ALL);
        matcher.addURI(AUTHORITY, BASE_PATH + "/#", GET_ONE);

        return matcher;
    }

    @Override
    public boolean onCreate() {

        databaseHelper = DatabaseHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch(uriMatcher.match(uri)) {
            case GET_ALL:

                return databaseHelper.getReadableDatabase().rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_VENUES, null);

                /*return databaseHelper.getReadableDatabase().rawQuery("SELECT " + DatabaseHelper.KEY_ID + ", "
                        + DatabaseHelper.COLUMN_NAME + ", " + DatabaseHelper.COLUMN_CITY + ", " + DatabaseHelper.COLUMN_CATEGORY
                        + " FROM " + DatabaseHelper.TABLE_VENUES, null);
                                                                            */
            case GET_ONE:

                return databaseHelper.getReadableDatabase().rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_VENUES
                    + " WHERE " + DatabaseHelper.KEY_ID + " = " + uri.getLastPathSegment(), null);

                //todo: uri.getLastPathSegment was ' KEY_ID' or 'id'
                /*return databaseHelper.getReadableDatabase().rawQuery("SELECT " + DatabaseHelper.KEY_ID + ", "
                        + DatabaseHelper.COLUMN_NAME + ", " + DatabaseHelper.COLUMN_CITY + ", " + DatabaseHelper.COLUMN_CATEGORY
                        + " FROM " + DatabaseHelper.TABLE_VENUES + " WHERE "
                        + DatabaseHelper.KEY_ID + " = " + uri.getLastPathSegment(), null);*/

            default:
                throw new IllegalArgumentException("Unknown Uri:" + uri);

        }
    }

    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)) {

            case GET_ALL:
                return VENUES_MIME_TYPE;
            case GET_ONE:
                return VENUE_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

//todo: we may need to implement these at some point
    //http://developer.xamarin.com/guides/android/platform_features/intro_to_content_providers/part_3_-_creating_a_custom_contentprovider/

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //todo
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //todo
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //todo
        return 0;
    }
}
