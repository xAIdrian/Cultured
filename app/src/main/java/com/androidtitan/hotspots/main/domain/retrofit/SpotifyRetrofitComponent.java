package com.androidtitan.hotspots.main.domain.retrofit;

import com.androidtitan.hotspots.main.presenter.music.MusicPresenterImpl;
import com.androidtitan.hotspots.main.scopes.sRetrofitScope;

import dagger.Component;

/**
 * Created by amohnacs on 3/16/16.
 */

@sRetrofitScope
@Component(
        modules = { SpotifyRetrofitModule.class,}
)
public interface SpotifyRetrofitComponent {

    SpotifyRetrofit spotifyRetrofit();

    void inject(MusicPresenterImpl impl);
}
