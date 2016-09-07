package com.androidtitan.hotspots.main.inject;

import android.app.Application;
import android.content.Context;

import com.androidtitan.hotspots.main.newsdetail.NewsDetailActivity;
import com.androidtitan.hotspots.main.newsfeed.NewsAdapter;
import com.androidtitan.hotspots.main.newsfeed.NewsPresenter;
import com.androidtitan.hotspots.main.newsfeed.ui.NewsActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component (
        modules = { AppModule.class,
                    NewsModule.class}
)
public interface AppComponent {

    Application getApplication();
    Context getApplicationContext();

    void inject(NewsActivity activity);
    void inject(NewsAdapter adapter);
    void inject(NewsDetailActivity activity);

    void inject(NewsPresenter newsPresenter);

}
