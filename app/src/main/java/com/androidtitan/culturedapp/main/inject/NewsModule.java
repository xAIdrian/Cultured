package com.androidtitan.culturedapp.main.inject;

import android.content.Context;

import com.androidtitan.culturedapp.main.newsfeed.NewsPresenter;
import com.androidtitan.culturedapp.main.newsfeed.NewsProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 3/21/16.
 */

//todo: as this portion of the app is built out and we add more components and background processes we are going to include more module @Provide methods

@Module (
        includes = { AppModule.class }
)
public class NewsModule {

    @Provides
    public NewsPresenter provideNewsPresenter(Context context) {
        return new NewsPresenter(context);
    }

    /*@Provides
    public NewsDetailPresenter provideNewsDetailPresenter(Context context) {
        return new NewsDetailPresenterImpl(context);
    }*/

    @Provides
    public NewsProvider providesNewsProvider(Context context) {
        return new NewsProvider(context);
    }
}
