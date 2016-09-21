package com.androidtitan.culturedapp.main.inject;

import android.app.Application;
import android.content.Context;


import com.androidtitan.culturedapp.main.newsfeed.NewsAdapter;
import com.androidtitan.culturedapp.main.newsfeed.NewsPresenter;
import com.androidtitan.culturedapp.main.newsfeed.ui.NewsActivity;
import com.androidtitan.culturedapp.main.toparticle.TopArticlePresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component (
        modules = { AppModule.class,
                    NewsModule.class,
                    TopArticleModule.class}
)
public interface AppComponent {

    Application getApplication();
    Context getApplicationContext();

    void inject(NewsActivity activity);
    void inject(NewsAdapter adapter);
//    void inject(NewsDetailActivity activity);

    void inject(NewsPresenter newsPresenter);
    void inject(TopArticlePresenter topArticlePresenter);

}
