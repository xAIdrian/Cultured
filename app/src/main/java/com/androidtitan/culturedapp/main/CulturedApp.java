package com.androidtitan.culturedapp.main;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Intent;

import com.androidtitan.culturedapp.main.inject.AppComponent;
import com.androidtitan.culturedapp.main.inject.AppModule;
import com.androidtitan.culturedapp.main.inject.DaggerAppComponent;
import com.androidtitan.culturedapp.main.newsfeed.ui.NewsActivity;
import com.androidtitan.culturedapp.main.toparticle.service.ArticleSyncService;

/**
 * Created by amohnacs on 3/14/16.
 */
public class CulturedApp extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

    }

    public static AppComponent getAppComponent(){
        return appComponent;
    }


}
