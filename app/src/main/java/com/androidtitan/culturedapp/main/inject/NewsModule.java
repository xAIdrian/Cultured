package com.androidtitan.culturedapp.main.inject;

import android.content.Context;

import com.androidtitan.culturedapp.main.newsfeed.NewsFeedPresenter;
import com.androidtitan.culturedapp.main.newsfeed.NewsFeedProvider;

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
    public NewsFeedPresenter provideNewsPresenter(Context context) {
        return new NewsFeedPresenter(context);
    }

    /*@Provides
    public NewsDetailPresenter provideNewsDetailPresenter(Context context) {
        return new NewsDetailPresenterImpl(context);
    }*/

    @Provides
    public NewsFeedProvider providesNewsProvider(Context context) {
        return new NewsFeedProvider(context);
    }
}
