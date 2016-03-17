package com.androidtitan.hotspots.main.domain;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by amohnacs on 3/16/16.
 */

public class SpotifyRetroFit {
    private final String TAG = getClass().getSimpleName();

    public static final String BASE_URL = "https://api.spotify.com/v1/";

    private Retrofit retrofit;

    public SpotifyRetroFit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }


}
