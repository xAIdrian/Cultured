package com.androidtitan.culturedapp.main.trending;

import android.content.Context;

import javax.inject.Inject;


/**
 * Created by Adrian Mohnacs on 1/22/17.
 */

public class TrendingProvider implements TrendingMvp.Provider {
    private final String TAG = getClass().getSimpleName();

    private Context context;

    @Inject
    public TrendingProvider(Context context) {
        this.context = context;

    }
}
