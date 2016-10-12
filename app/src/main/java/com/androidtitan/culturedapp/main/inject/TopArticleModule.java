package com.androidtitan.culturedapp.main.inject;

import android.content.Context;

import com.androidtitan.culturedapp.main.toparticle.TopArticlePresenter;
import com.androidtitan.culturedapp.main.toparticle.TopArticleProvider;

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
    public TopArticleProvider provideTopArticleProvider(Context context) {
        return new TopArticleProvider(context);
    }

}
