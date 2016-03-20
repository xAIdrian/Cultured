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
    private final MainView mainview;

    public MainPresenterModule(MainActivity activity, MainView mainView) {
        this.act = activity;
        this.mainview = mainView;
    }

    @Provides @PresenterScope
    public MainPresenter providePresenter(Context context, MainView view) {
        return new MainPresenterImpl(context, view);
    }

    @Provides @PresenterScope
    public Context provideContext() {
        return act;
    }

    @Provides @PresenterScope
    public MainView provideMainView() {
        return mainview;
    }



}
