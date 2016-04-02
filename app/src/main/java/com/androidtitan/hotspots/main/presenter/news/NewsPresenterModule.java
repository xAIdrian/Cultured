package com.androidtitan.hotspots.main.presenter.news;

import android.content.Context;

import com.androidtitan.hotspots.main.application.App;
import com.androidtitan.hotspots.main.scopes.nPresenterScope;
import com.androidtitan.hotspots.main.ui.activities.NewsActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 3/21/16.
 */

@Module
public class NewsPresenterModule {

    private final NewsActivity act;
    private final NewsView mainview;

    public NewsPresenterModule(NewsActivity activity, NewsView newsView) {
        this.act = activity;
        this.mainview = newsView;
    }

    @Provides
    @nPresenterScope
    public NewsPresenter providePresenter(Context context, NewsView view) {
        return new NewsPresenterImpl(context, view);
    }

    @Provides @nPresenterScope
    public NewsView provideNewsView() {
        return mainview;
    }

    @Provides @nPresenterScope
    public Context provideContext() {
        return App.getAppComponent().getApplicationContext();
    }

}
