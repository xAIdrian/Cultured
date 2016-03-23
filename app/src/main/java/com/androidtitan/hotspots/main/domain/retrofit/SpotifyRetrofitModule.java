package com.androidtitan.hotspots.main.domain.retrofit;

import com.androidtitan.hotspots.main.scopes.sRetrofitScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by amohnacs on 3/16/16.
 */

@Module
public class SpotifyRetrofitModule {

    @Provides @sRetrofitScope
    public SpotifyRetrofit provideSpotifyRetroFit() {
        return new SpotifyRetrofit();
    }

    @Provides @sRetrofitScope
    public Retrofit provideRetroFitBySpotify(SpotifyRetrofit spotifyRetrofit) {
        return spotifyRetrofit.getRetrofit();
    }

}
