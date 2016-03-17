package com.androidtitan.hotspots.main.presenter;

import com.androidtitan.hotspots.main.application.AppComponent;
import com.androidtitan.hotspots.main.presenter.adapter.SpotifyCardAdapter;
import com.androidtitan.hotspots.main.scopes.PresenterScope;
import com.androidtitan.hotspots.main.ui.ImageListFragment;

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

    MainPresenter getPresenter();

    // allows us to inject into ACTIVITY
    void inject(ImageListFragment imageListFragment);
    void inject(SpotifyCardAdapter adapter);



}
