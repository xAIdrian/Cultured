package com.androidtitan.culturedapp.main;

import android.app.Application;

import com.androidtitan.culturedapp.main.inject.AppComponent;
import com.androidtitan.culturedapp.main.inject.AppModule;
import com.androidtitan.culturedapp.main.inject.DaggerAppComponent;

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
