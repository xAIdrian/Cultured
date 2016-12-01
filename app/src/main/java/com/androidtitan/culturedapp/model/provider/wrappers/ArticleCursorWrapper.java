package com.androidtitan.culturedapp.model.provider.wrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Multimedium;
import com.androidtitan.culturedapp.model.provider.DatabaseContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by amohnacs on 9/24/16.
 */

public class ArticleCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ArticleCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Article getArticle() {

        String id = getString(getColumnIndex(DatabaseContract.ArticleTable._ID));
        String title = getString(getColumnIndex(DatabaseContract.ArticleTable.TITLE));
        String section = getString(getColumnIndex(DatabaseContract.ArticleTable.SECTION));
        String articleAbstract = getString(getColumnIndex(DatabaseContract.ArticleTable.ABSTRACT));
        String url = getString(getColumnIndex(DatabaseContract.ArticleTable.URL));
        String createdDate = getString(getColumnIndex(DatabaseContract.ArticleTable.CREATED_DATE));

        String des = getString(getColumnIndex(DatabaseContract.ArticleTable.DES_FACET));
        String org = getString(getColumnIndex(DatabaseContract.ArticleTable.ORG_FACET));
        String per = getString(getColumnIndex(DatabaseContract.ArticleTable.PER_FACET));
        String geo = getString(getColumnIndex(DatabaseContract.ArticleTable.GEO_FACET));


        /*
        Date createdDate = convert Date
         */
        DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date date = new Date();
        try {
            date = format.parse(createdDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Article(id, title, section, articleAbstract, url, date, null, null, null, null, null);
    }
}
