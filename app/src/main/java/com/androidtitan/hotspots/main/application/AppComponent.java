package com.androidtitan.hotspots.main.application;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component (
        modules = { AppModule.class }
)
public interface AppComponent {

    Application getApplication();
    Context getApplicationContext();

}
