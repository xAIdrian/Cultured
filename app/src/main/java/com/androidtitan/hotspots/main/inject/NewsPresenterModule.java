package com.androidtitan.hotspots.main.inject;

import android.content.Context;

import com.androidtitan.hotspots.main.presenter.news.NewsPresenter;
import com.androidtitan.hotspots.main.presenter.news.NewsPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 3/21/16.
 */

@Module (
        includes = { AppModule.class }
)
public class NewsPresenterModule {


    public NewsPresenterModule() {
    }

    @Provides
    public NewsPresenter providePresenter(Context context) {
        return new NewsPresenterImpl(context);
    }
}
