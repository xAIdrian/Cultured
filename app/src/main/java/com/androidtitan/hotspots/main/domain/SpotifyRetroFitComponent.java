package com.androidtitan.hotspots.main.domain;

import com.androidtitan.hotspots.main.presenter.MainPresenterImpl;
import com.androidtitan.hotspots.main.scopes.SpotifyScope;

import dagger.Component;

/**
 * Created by amohnacs on 3/16/16.
 */

@SpotifyScope
@Component(
        modules = { SpotifyRetroFitModule.class }
)
public interface SpotifyRetroFitComponent {

    SpotifyRetroFit spotifyRetrofit();
    void inject(MainPresenterImpl impl);
}
