package com.androidtitan.culturedapp.main.provider;

import android.net.Uri;

/**
 * Created by amohnacs on 9/25/16.
 */

public class DatabaseContract {
    private final String TAG = getClass().getSimpleName();

    public final static String AUTHORITY = "com.androidtitan.culturedapp.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

/* Sample MIME types:

    private static String BASE_PATH = SQLiteHelper.DATABASE_NAME;
    private static String path_CONTENT_URI =  "content://" + AUTHORITY+ "/" + BASE_PATH +"/";
    static final String SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd.marakana.android.lifecycle.status";
    static final String MULTIPLE_RECORDS_MIME_TYPE = "vnd.android.cursor.dir/vnd.marakana.android.lifecycle.status";
*/
    public static final class ArticleTable {

        public static final String TABLE_NAME = "toparticles";
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(DatabaseContract.CONTENT_URI, TABLE_NAME);

        public static final String _ID = "_ID";
        public static final String TITLE = "title";
        public static final String SECTION = "section";
        public static final String ABSTRACT = "abstract";
        public static final String URL = "url";
        public static final String CREATED_DATE = "create_date";

    }

    public static final class MediaTable {

        public static final String TABLE_NAME = "media";
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(DatabaseContract.CONTENT_URI, TABLE_NAME);

        public static final String _ID = "_ID";
        public static final String STORY_ID = "story_id";
        public static final String SIZE = "media_size"; // we're not actually storing this shhh...
        public static final String URL = "url";
        public static final String FORMAT = "format";
        public static final String HEIGHT = "height"; //int
        public static final String WIDTH = "width"; //int
        public static final String TYPE = "type";
        public static final String SUBTYPE = "subtype";
        public static final String CAPTION = "caption";
        public static final String COPYRIGHT = "copyright";

    }

    public static final class FacetTable {

        public static final String TABLE_NAME = "facets";
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(DatabaseContract.CONTENT_URI, TABLE_NAME);

        public static final String _ID = "_ID";
        public static final String STORY_ID = "story_id";
        public static final String TYPE = "type";
        public static final String FACET = "facet";
        public static final String CREATED_DATE = "create_date";
    }
}
