package com.androidtitan.hotspots.main.domain.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by amohnacs on 3/16/16.
 */

public class SpotifyRetrofit {
    private final String TAG = getClass().getSimpleName();

    public static final String SPOTIFY_BASE_URL = "https://api.spotify.com/v1/";
    private Retrofit spotifyRetrofit;

    public SpotifyRetrofit() {


        spotifyRetrofit = new Retrofit.Builder()
                .baseUrl(SPOTIFY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


    }

    public Retrofit getRetrofit() {
        return spotifyRetrofit;
    }


}
