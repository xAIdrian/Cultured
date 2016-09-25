package com.androidtitan.culturedapp.main.toparticle.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by amohnacs on 7/17/16.
 */

public class ArticleSQLiteHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "articles.sqlite";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public ArticleSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + DatabaseContract.Article.TABLE_NAME + " (" +
                        DatabaseContract.Article._ID + " INTEGER PRIMARY KEY," +
                        DatabaseContract.Article.TITLE + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.Article.SECTION + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.Article.ABSTRACT + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.Article.URL + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.Article.CREATED_DATE + TEXT_TYPE+ COMMA_SEP +
                        DatabaseContract.Article.DES_FACET + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.Article.ORG_FACET + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.Article.PER_FACET + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.Article.GEO_FACET + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.Article.MEDIA_URL + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.Article.MEDIA_CAPTION + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.Article.MEDIA_WIDTH + INT_TYPE + COMMA_SEP +
                        DatabaseContract.Article.MEDIA_HEIGHT + INT_TYPE + " )"
        );
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
