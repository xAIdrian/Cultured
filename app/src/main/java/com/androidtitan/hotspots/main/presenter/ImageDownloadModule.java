package com.androidtitan.hotspots.main.presenter;

import com.androidtitan.hotspots.main.scopes.PresenterScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amohnacs on 3/19/16.
 */

@Module
public class ImageDownloadModule {

    @Provides @PresenterScope
    ImageDownloadPresenter provideImageDownloadPresenter() {
        return new ImageDownloadPresenterImpl();
    }
}
