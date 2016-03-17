package com.androidtitan.hotspots.main.domain;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by amohnacs on 3/16/16.
 */

@Module
public class SpotifyRetroFitModule {

    @Provides
    public SpotifyRetroFit provideSpotifyretroFit() {
        return new SpotifyRetroFit();
    }

    @Provides
    public Retrofit provideRetroFit(SpotifyRetroFit spotifyRetroFit) {
        return spotifyRetroFit.getRetrofit();
    }
}
