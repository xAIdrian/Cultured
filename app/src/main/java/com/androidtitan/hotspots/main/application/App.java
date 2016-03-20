package com.androidtitan.hotspots.main.application;

import android.app.Application;

/**
 * Created by amohnacs on 3/14/16.
 */
public class App extends Application {

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
