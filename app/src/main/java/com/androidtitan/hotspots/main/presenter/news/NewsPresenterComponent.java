package com.androidtitan.hotspots.main.presenter.news;

import com.androidtitan.hotspots.main.scopes.nPresenterScope;
import com.androidtitan.hotspots.main.ui.NewsActivity;
import com.androidtitan.hotspots.main.ui.adapter.NewsAdapter;

import dagger.Component;

/**
 * Created by amohnacs on 3/21/16.
 */
@nPresenterScope
@Component(
        modules = { NewsPresenterModule.class }
)
public interface NewsPresenterComponent {
    NewsPresenter getNewsPresenter();

    // allows us to inject into ACTIVITY
    void inject(NewsActivity activity);
    void inject(NewsAdapter adapter);
}
