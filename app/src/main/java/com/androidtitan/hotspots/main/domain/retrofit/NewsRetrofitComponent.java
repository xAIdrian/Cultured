package com.androidtitan.hotspots.main.domain.retrofit;

import com.androidtitan.hotspots.main.presenter.news.NewsPresenterImpl;
import com.androidtitan.hotspots.main.scopes.nRetrofitScope;

import dagger.Component;

/**
 * Created by amohnacs on 3/21/16.
 */

@nRetrofitScope
@Component(
        modules = { NewsRetrofitModule.class }
)
public interface NewsRetrofitComponent {
    NewsRetrofit newsRetrofit();

    void inject(NewsPresenterImpl impl);
}
