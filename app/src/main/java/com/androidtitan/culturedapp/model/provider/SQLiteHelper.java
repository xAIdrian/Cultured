package com.androidtitan.culturedapp.model.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by amohnacs on 7/17/16.
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "articles.sqlite";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + DatabaseContract.ArticleTable.TABLE_NAME + " ( " +
                        DatabaseContract.ArticleTable._ID + " INTEGER PRIMARY KEY," +
                        DatabaseContract.ArticleTable.TITLE + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.ArticleTable.SECTION + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.ArticleTable.ABSTRACT + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.ArticleTable.URL + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.ArticleTable.CREATED_DATE + TEXT_TYPE+ COMMA_SEP +
                        DatabaseContract.ArticleTable.DES_FACET + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.ArticleTable.ORG_FACET + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.ArticleTable.PER_FACET + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.ArticleTable.GEO_FACET + TEXT_TYPE + " )"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + DatabaseContract.MediaTable.TABLE_NAME + " ( " +
                        DatabaseContract.MediaTable._ID + " INTEGER KEY PRIMARY," +
                        DatabaseContract.MediaTable.STORY_ID + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.MediaTable.SIZE + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.MediaTable.URL + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.MediaTable.FORMAT + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.MediaTable.HEIGHT + INT_TYPE + COMMA_SEP +
                        DatabaseContract.MediaTable.WIDTH + INT_TYPE + COMMA_SEP +
                        DatabaseContract.MediaTable.TYPE + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.MediaTable.SUBTYPE + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.MediaTable.CAPTION + TEXT_TYPE + COMMA_SEP +
                        DatabaseContract.MediaTable.COPYRIGHT + TEXT_TYPE + " )"
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
