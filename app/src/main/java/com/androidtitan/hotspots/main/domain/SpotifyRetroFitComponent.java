package com.androidtitan.hotspots.main.domain;

import com.androidtitan.hotspots.main.presenter.MainPresenterImpl;

import dagger.Component;

/**
 * Created by amohnacs on 3/16/16.
 */

@Component(
        modules = { SpotifyRetroFitModule.class }
)
public interface SpotifyRetroFitComponent {

    SpotifyRetroFit spotifyRetrofit();
    void inject(MainPresenterImpl impl);
}
