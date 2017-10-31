package com.androidtitan.culturedapp.common;

import com.androidtitan.culturedapp.main.CulturedApp;
import com.androidtitan.culturedapp.model.newyorktimes.Article;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by amohnacs on 10/17/17.
 */

public class SessionManager {
    private static final String TAG = SessionManager.class.getSimpleName();

    private static volatile SessionManager instance;

    private final Context context;

    private FileManager fileManager;


    public static SessionManager getInstance() {
        if(instance == null) {
            synchronized (SessionManager.class) {
                if(instance == null) {
                    instance = new SessionManager(CulturedApp.getAppContext());
                }
            }
        }
        return instance;
    }

    private SessionManager(Context context) {
        this.context = context;
    }

    public HashMap<String, Boolean> getBookmarkedArticles() {
        fileManager = FileManager.getInstance(context);
        return fileManager.getInternalArticlesHashMap();
    }
}
