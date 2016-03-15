package com.androidtitan.hotspots.SimpleDagger2;

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
