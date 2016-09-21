package com.androidtitan.culturedapp.main.toparticle.model;

import android.provider.BaseColumns;

/**
 * Created by amohnacs on 7/17/16.
 */

public final class TopArticleTable {

    public TopArticleTable() {
    }

    //table of contents
    public static abstract class TopArticleEntry implements BaseColumns {
        public static final String TABLE_NAME = "toparticles";
        public static final String _ID = "_id";
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

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    //todo: we need to a complex primary key, that contains two keys.  Appended to SQL_CREATE_ENTRIES.
    // it will probably be _id and title

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TopArticleEntry.TABLE_NAME + " (" +
                    TopArticleEntry._ID + " INTEGER PRIMARY KEY," +
                    TopArticleEntry.TITLE + TEXT_TYPE + COMMA_SEP +
                    TopArticleEntry.SECTION + TEXT_TYPE + COMMA_SEP +
                    TopArticleEntry.ABSTRACT + TEXT_TYPE + COMMA_SEP +
                    TopArticleEntry.URL + TEXT_TYPE + COMMA_SEP +
                    TopArticleEntry.CREATED_DATE + TEXT_TYPE+ COMMA_SEP +
                    TopArticleEntry.DES_FACET + TEXT_TYPE + COMMA_SEP +
                    TopArticleEntry.ORG_FACET + TEXT_TYPE + COMMA_SEP +
                    TopArticleEntry.PER_FACET + TEXT_TYPE + COMMA_SEP +
                    TopArticleEntry.GEO_FACET + TEXT_TYPE + COMMA_SEP +
                    TopArticleEntry.MEDIA_URL + TEXT_TYPE + COMMA_SEP +
                    TopArticleEntry.MEDIA_URL + TEXT_TYPE + COMMA_SEP +
                    TopArticleEntry.MEDIA_CAPTION + TEXT_TYPE + COMMA_SEP +
                    TopArticleEntry.MEDIA_WIDTH + INT_TYPE + COMMA_SEP +
                    TopArticleEntry.MEDIA_HEIGHT + INT_TYPE +
            " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TopArticleEntry.TABLE_NAME;



}
