package com.androidtitan.hotspots.main.presenter.music;

import android.content.Context;

import com.androidtitan.hotspots.main.application.App;
import com.androidtitan.hotspots.main.scopes.sPresenterScope;
import com.androidtitan.hotspots.main.ui.activities.MusicActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 3/14/16.
 */

@Module(
        //includes = ImageDownloadPresenter.class
)
public class MusicPresenterModule {
    private final MusicActivity act;
    private final MusicView mainview;

    public MusicPresenterModule(MusicActivity activity, MusicView musicView) {
        this.act = activity;
        this.mainview = musicView;
    }

    @Provides @sPresenterScope
    public MusicPresenter providePresenter(Context context, MusicView view) {
        return new MusicPresenterImpl(context, view);
    }



    @Provides @sPresenterScope
    public Context provideContext() {
        return App.getAppComponent().getApplicationContext();
    }

    @Provides @sPresenterScope
    public MusicView provideMusicView() {
        return mainview;
    }



}
