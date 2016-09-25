package com.androidtitan.culturedapp.main.toparticle.model;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Multimedium;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.format;

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

    public boolean storeArticle(Cursor cursor) {

        return false;
    }

    public Article getArticle() {

        String id = getString(getColumnIndex(DatabaseContract.Article._ID));
        String title = getString(getColumnIndex(DatabaseContract.Article.TITLE));
        String section = getString(getColumnIndex(DatabaseContract.Article.SECTION));
        String articleAbstract = getString(getColumnIndex(DatabaseContract.Article.ABSTRACT));
        String url = getString(getColumnIndex(DatabaseContract.Article.URL));
        String createdDate = getString(getColumnIndex(DatabaseContract.Article.CREATED_DATE));

        String des = getString(getColumnIndex(DatabaseContract.Article.DES_FACET));
        String org = getString(getColumnIndex(DatabaseContract.Article.ORG_FACET));
        String per = getString(getColumnIndex(DatabaseContract.Article.PER_FACET));
        String geo = getString(getColumnIndex(DatabaseContract.Article.GEO_FACET));

        String mediaUrl = getString(getColumnIndex(DatabaseContract.Article.MEDIA_URL));
        String mediaCaption = getString(getColumnIndex(DatabaseContract.Article.MEDIA_CAPTION));
        int mediaWidth = getInt(getColumnIndex(DatabaseContract.Article.MEDIA_WIDTH));
        int mediaHeight = getInt(getColumnIndex(DatabaseContract.Article.MEDIA_HEIGHT));

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


        Multimedium multimedium = new Multimedium(mediaUrl, mediaWidth, mediaHeight, mediaCaption);

        return new Article(title, section, articleAbstract, url, date, des, org, per, geo, multimedium);
    }
}
