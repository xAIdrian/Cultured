package com.androidtitan.hotspots.main.inject;

import android.content.Context;

import com.androidtitan.hotspots.main.presenter.music.MusicPresenter;
import com.androidtitan.hotspots.main.presenter.music.MusicPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 3/14/16.
 */

@Module(
        includes = { AppModule.class }
)
public class MusicPresenterModule {

    public MusicPresenterModule() {
    }

    @Provides @Singleton
    public MusicPresenter providePresenter(Context context) {
        return new MusicPresenterImpl(context);
    }




}
