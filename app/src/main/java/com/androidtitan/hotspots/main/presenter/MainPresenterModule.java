package com.androidtitan.hotspots.main.presenter;

import android.content.Context;

import com.androidtitan.hotspots.main.scopes.PresenterScope;
import com.androidtitan.hotspots.main.ui.MainActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 3/14/16.
 */

@Module(
        //includes = ImageDownloadPresenter.class
)
public class MainPresenterModule {
    private final MainActivity act;

    public MainPresenterModule(MainActivity activity) {
        this.act = activity;
    }

    @Provides @PresenterScope
    public MainPresenter providePresenter(Context context) {
        return new MainPresenterImpl(context);
    }

    @Provides @PresenterScope
    public Context provideContext() {
        return act;
    }



}
