package com.androidtitan.hotspots.main.inject;

import android.content.Context;

import com.androidtitan.hotspots.main.newsdetail.NewsDetailPresenter;
import com.androidtitan.hotspots.main.newsdetail.NewsDetailPresenterImpl;
import com.androidtitan.hotspots.main.newsfeed.NewsPresenter;
import com.androidtitan.hotspots.main.newsfeed.NewsProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 3/21/16.
 */

@Module (
        includes = { AppModule.class }
)
public class NewsModule {

    @Provides
    public NewsPresenter provideNewsPresenter(Context context) {
        return new NewsPresenter(context);
    }

    @Provides
    public NewsDetailPresenter provideNewsDetailPresenter(Context context) {
        return new NewsDetailPresenterImpl(context);
    }

    @Provides
    public NewsProvider providesNewsProvider(Context context) {
        return new NewsProvider(context);
    }
}
