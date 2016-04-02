package com.androidtitan.hotspots.main.presenter.newsdetail;

import android.content.Context;

import com.androidtitan.hotspots.main.application.App;
import com.androidtitan.hotspots.main.scopes.dvPresenterScope;
import com.androidtitan.hotspots.main.ui.activities.NewsDetailActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 3/30/16.
 */

@Module
public class NewsDetailModule {

    private final NewsDetailActivity newsDetailActivity;
    private final NewsDetailView newsDetailView;

    public NewsDetailModule(NewsDetailActivity act, NewsDetailView view) {
        this.newsDetailActivity = act;
        this.newsDetailView = view;
    }

    @Provides @dvPresenterScope
    public NewsDetailPresenter providePresenter(Context context, NewsDetailView view) {
        return new NewsDetailPresenterImpl(context, view);
    }

    @Provides @dvPresenterScope
    public NewsDetailView provideNewsDetailView() {
        return newsDetailView;
    }

    @Provides @dvPresenterScope
    public Context provideContext() {
        return App.getAppComponent().getApplicationContext();
    }

}
