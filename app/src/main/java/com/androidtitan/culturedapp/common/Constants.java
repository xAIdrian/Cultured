package com.androidtitan.culturedapp.common;

import java.util.UUID;

/**
 * Created by amohnacs on 9/7/16.
 */

public class Constants {

    public static final int ARTICLE_LOADER_ID = 73856;
    public static final int TOP_ARTICLE_MEDIA_LOADER_ID = 75841;
    public static final int TOP_ARTICLE_FACET_LOADER_ID = 79654;
    public static final int TRENDING_FACET_LOADER_ID = 79816;

    public static final int FACET_JOB_SCHEDULER = 45321;

    public final static int NO_ARTICLE_ID = 1000;

    public final static String BOOKMARK_ARTICLES_FILE = "culturedbookmarkedarticles";

    //shared preferences
    public static String CULTURED_PREFERENCES = "com.androidtitan.hotspots.main.culturedpreferences";
    public static String PREFERENCES_APP_FIRST_RUN = "com.androidtitan.hotspots.main.preferencesshouldonboard";
    public static final String PREFERENCES_SYNCING_PERIODICALLY = "sharedpreferences.syncingperiodically";
    public static final String PREFERENCES_ARTICLE_ID = "sharedpreferences.articleid";

    //passing articles to detail activity
    public static final String ARTICLE_EXTRA = "constant.articleextra";
    public static final String ARTICLE_GEO_FACETS = "constant.article_geo_facets";
    public static final String ARTICLE_BOOKMARKED = "constant.article_bookmarked";


}
