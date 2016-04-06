package com.androidtitan.hotspots.main.inject;

import android.content.Context;

import com.androidtitan.hotspots.main.presenter.newsdetail.NewsDetailPresenter;
import com.androidtitan.hotspots.main.presenter.newsdetail.NewsDetailPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 3/30/16.
 */

@Module(
        includes = { AppModule.class }
)
public class NewsDetailPresenterModule {

    public NewsDetailPresenterModule() {
    }

    @Provides
    public NewsDetailPresenter providePresenter(Context context) {
        return new NewsDetailPresenterImpl(context);
    }
}
