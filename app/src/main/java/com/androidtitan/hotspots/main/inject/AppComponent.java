package com.androidtitan.hotspots.main.inject;

import android.app.Application;
import android.content.Context;

import com.androidtitan.hotspots.main.ui.activities.NewsActivity;
import com.androidtitan.hotspots.main.ui.activities.NewsDetailActivity;
import com.androidtitan.hotspots.main.ui.adapter.NewsAdapter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component (
        modules = { AppModule.class,
                    NewsPresenterModule.class,
                    NewsDetailPresenterModule.class }
)
public interface AppComponent {

    Application getApplication();
    Context getApplicationContext();

    //newspresenter
    void inject(NewsActivity activity);
    void inject(NewsAdapter adapter);

    //newsdetail presenter
    void inject(NewsDetailActivity activity);

}
