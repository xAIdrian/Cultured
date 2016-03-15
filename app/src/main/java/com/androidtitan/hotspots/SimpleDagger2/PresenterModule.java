package com.androidtitan.hotspots.SimpleDagger2;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 3/14/16.
 */

@Module
public class PresenterModule {


    @Provides @PresenterScope
    Presenter providePresenter(Context context) {
        return new Presenter(context);
    }




}
