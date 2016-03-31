package com.androidtitan.hotspots.main.presenter.newsdetail;

import com.androidtitan.hotspots.main.scopes.dvPresenterScope;
import com.androidtitan.hotspots.main.ui.NewsDetailActivity;

import dagger.Component;

/**
 * Created by amohnacs on 3/30/16.
 */
@dvPresenterScope
@Component(
        modules = { NewsDetailModule.class }
)
public interface NewsDetailPresenterComponent {
    NewsDetailPresenter getNewsDetailPresenter();

    void inject(NewsDetailActivity activity);
}
