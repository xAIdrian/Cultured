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

    //shared preferences
    public static String CULTURED_PREFERENCES = "com.androidtitan.hotspots.main.culturedpreferences";
    public static String PREFERENCES_APP_FIRST_RUN = "com.androidtitan.hotspots.main.preferencesshouldonboard";
    public static final String PREFERENCES_SYNCING_PERIODICALLY = "sharedpreferences.syncingperiodically";
    public static final String PREFERENCES_ARTICLE_ID = "sharedpreferences.articleid";

    //for testing
    public static final String TEST_SECTION = "world";
    public static final int TEST_LIMIT = 1;
    public static final int TEST_OFFSET = 2;
}
