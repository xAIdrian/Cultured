package com.androidtitan.hotspots.main.presenter;

import com.androidtitan.hotspots.main.application.AppComponent;
import com.androidtitan.hotspots.main.ui.MainActivity;
import com.androidtitan.hotspots.main.scopes.PresenterScope;

import dagger.Component;

/**
 * Created by amohnacs on 3/14/16.
 */
@PresenterScope
@Component (
        dependencies = { AppComponent.class },
        modules = { MainPresenterModule.class }
)
public interface MainPresenterComponent {

    // allow to inject into ACTIVITY
    // method name not important
    void inject(MainActivity activity); //let's change this to general activity

    MainPresenter getPresenter();
}
