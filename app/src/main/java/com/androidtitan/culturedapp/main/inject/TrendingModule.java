package com.androidtitan.culturedapp.main.inject;

import android.content.Context;

import com.androidtitan.culturedapp.main.trending.TrendingPresenter;
import com.androidtitan.culturedapp.main.trending.TrendingProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Adrian Mohnacs on 1/24/17.
 */

@Module(
        includes = { AppModule.class }
)
public class TrendingModule {

    @Provides
    public TrendingPresenter provieTrendingPresenter(Context context) {
        return new TrendingPresenter(context);
    }

    @Provides
    public TrendingProvider provideTrendingProvider(Context context) {
        return new TrendingProvider(context);
    }
}
