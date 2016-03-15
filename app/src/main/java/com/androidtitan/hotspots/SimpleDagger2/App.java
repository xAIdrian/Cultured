package com.androidtitan.hotspots.SimpleDagger2;

import android.app.Application;

/**
 * Created by amohnacs on 3/14/16.
 */
public class App extends Application {

    private static AppComponent appComponent;
    private static PresenterComponent presenterComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        presenterComponent= DaggerPresenterComponent.builder()
                .presenterModule(new PresenterModule()) //this can be removed
                .build();

    }

    public static AppComponent getAppComponent(){
        return appComponent;
    }

    public static PresenterComponent getPresenterComponent() { //todo: Maybe this shouldn't be static?
        return presenterComponent;
    }

}
