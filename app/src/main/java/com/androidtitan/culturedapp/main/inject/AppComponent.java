package com.androidtitan.culturedapp.main.inject;

import android.app.Application;
import android.content.Context;


import com.androidtitan.culturedapp.main.newsfeed.NewsAdapter;
import com.androidtitan.culturedapp.main.newsfeed.NewsPresenter;
import com.androidtitan.culturedapp.main.newsfeed.ui.NewsActivity;
import com.androidtitan.culturedapp.main.toparticle.ui.TopArticleActivity;
import com.androidtitan.culturedapp.main.toparticle.TopArticlePresenter;
import com.androidtitan.culturedapp.main.trending.TrendingPresenter;

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

    void inject(NewsActivity activity);
    void inject(TopArticleActivity activity);

    void inject(NewsAdapter adapter);

    void inject(NewsPresenter newsPresenter);
    void inject(TopArticlePresenter topArticlePresenter);
    void inject(TrendingPresenter trendingPresenter);

}
