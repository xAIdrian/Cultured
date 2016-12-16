package com.androidtitan.culturedapp.main.inject;

import android.content.Context;

import com.androidtitan.culturedapp.main.toparticle.TopArticleLoaderProvider;
import com.androidtitan.culturedapp.main.toparticle.TopArticlePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 9/19/16.
 */

@Module(
        includes = { AppModule.class }
)
public class TopArticleModule {

    @Provides
    public TopArticlePresenter provideTopArticlePresenter(Context context) {
        return new TopArticlePresenter(context);
    }

    @Provides
    public TopArticleLoaderProvider provideTopArticleProvider(Context context) {
        return new TopArticleLoaderProvider(context);
    }

}
