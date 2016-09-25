package com.androidtitan.culturedapp.main.toparticle.model;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * Created by amohnacs on 9/25/16.
 */

public class DatabaseContract {
    private final String TAG = getClass().getSimpleName();

    public final static String AUTHORITY = "com.androidtitan.culturedapp.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String LIST_CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "vnd." + AUTHORITY;
    public static final String SINGLE_CONTENT_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "vnd." + AUTHORITY;

    //private static String BASE_PATH = ArticleSQLiteHelper.DATABASE_NAME;
    //private static String path_CONTENT_URI =  "content://" + AUTHORITY+ "/" + BASE_PATH +"/";
//    static final String SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd.marakana.android.lifecycle.status";
//    static final String MULTIPLE_RECORDS_MIME_TYPE = "vnd.android.cursor.dir/vnd.marakana.android.lifecycle.status";



    public static final class Article {
        public static final String TABLE_NAME = "toparticles";
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(DatabaseContract.CONTENT_URI, TABLE_NAME);

        public static final String _ID = "_ID";
        public static final String TITLE = "title";
        public static final String SECTION = "section";
        public static final String ABSTRACT = "abstract";
        public static final String URL = "url";
        public static final String CREATED_DATE = "create_date";

        public static final String DES_FACET = "des_facet";
        public static final String ORG_FACET = "org_facet";
        public static final String PER_FACET = "per_facet";
        public static final String GEO_FACET = "geo_facet";

        public static final String MEDIA_URL = "media_url";
        public static final String MEDIA_CAPTION = "media_caption";
        public static final String MEDIA_WIDTH = "media_width"; //integer
        public static final String MEDIA_HEIGHT = "media_height"; //integer
    }

}
