package com.androidtitan.hotspots.main.domain.retrofit;

import com.androidtitan.hotspots.main.scopes.nRetrofitScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by amohnacs on 3/21/16.
 */

@Module
public class NewsRetrofitModule {

    @Provides @nRetrofitScope
    public NewsRetrofit provideNewsRetrofit() {
        return new NewsRetrofit();
    }

    @Provides @nRetrofitScope
    public Retrofit provideRetrofitByNews(NewsRetrofit newsRetrofit) {
        return newsRetrofit.getRetrofit();
    }
}
