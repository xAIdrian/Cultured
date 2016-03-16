package com.androidtitan.hotspots.main.application;

import android.app.Application;

import com.androidtitan.hotspots.main.presenter.DaggerMainPresenterComponent;
import com.androidtitan.hotspots.main.presenter.MainPresenterComponent;
import com.androidtitan.hotspots.main.presenter.MainPresenterModule;

/**
 * Created by amohnacs on 3/14/16.
 */
public class App extends Application {

    private static AppComponent appComponent;
    private static MainPresenterComponent mainPresenterComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        mainPresenterComponent = DaggerMainPresenterComponent.builder()
                .appComponent(appComponent)
                .mainPresenterModule(new MainPresenterModule()) //this can be removed
                .build();

    }

    public static AppComponent getAppComponent(){
        return appComponent;
    }

    public static MainPresenterComponent getMainPresenterComponent() { //todo: Maybe this shouldn't be static?
        return mainPresenterComponent;
    }

}
