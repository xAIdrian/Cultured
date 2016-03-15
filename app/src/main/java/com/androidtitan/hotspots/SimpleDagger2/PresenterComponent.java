package com.androidtitan.hotspots.SimpleDagger2;

import dagger.Component;

/**
 * Created by amohnacs on 3/14/16.
 */
@PresenterScope
@Component (
        modules = { PresenterModule.class },
        dependencies = { AppComponent.class }
)
public interface PresenterComponent {

    void inject(MainActivity activity); //let's change this to general activity

    Presenter getPresenter();
}
