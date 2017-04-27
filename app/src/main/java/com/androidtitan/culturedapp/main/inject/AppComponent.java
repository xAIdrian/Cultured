package com.androidtitan.culturedapp.main.inject;

import android.app.Application;
import android.content.Context;


import com.androidtitan.culturedapp.main.newsfeed.adapter.NewsFeedAdapter;
import com.androidtitan.culturedapp.main.newsfeed.NewsFeedPresenter;
import com.androidtitan.culturedapp.main.newsfeed.ui.NewsFeedActivity;
import com.androidtitan.culturedapp.main.toparticle.ui.TopArticleActivity;
import com.androidtitan.culturedapp.main.toparticle.TopArticlePresenter;
import com.androidtitan.culturedapp.main.trending.TrendingPresenter;
import com.androidtitan.culturedapp.main.trending.ui.TrendingActivity;
import com.androidtitan.culturedapp.widget.ImageAppWidgetProvider;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component (
        modules = { AppModule.class,
                    NewsModule.class,
                    TopArticleModule.class,
                    TrendingModule.class }
)
public interface AppComponent {

    Application getApplication();
    Context getApplicationContext();

    void inject(NewsFeedActivity activity);
    void inject(TopArticleActivity activity);
    void inject(TrendingActivity activity);

    void inject(NewsFeedAdapter adapter);

    void inject(NewsFeedPresenter newsFeedPresenter);
    void inject(TopArticlePresenter topArticlePresenter);
    void inject(TrendingPresenter trendingPresenter);

    void inject(ImageAppWidgetProvider appWidgetProvider);
}
