package com.androidtitan.hotspots.main.presenter;

import android.content.Context;

import com.androidtitan.hotspots.main.scopes.PresenterScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 3/14/16.
 */

@Module
public class MainPresenterModule {

    @Provides @PresenterScope
    public MainPresenter providePresenter(Context context) {
        return new MainPresenterImpl();
    }




}
