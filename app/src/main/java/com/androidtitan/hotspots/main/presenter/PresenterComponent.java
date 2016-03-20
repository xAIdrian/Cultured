package com.androidtitan.hotspots.main.presenter;

import com.androidtitan.hotspots.main.scopes.PresenterScope;
import com.androidtitan.hotspots.main.ui.MainActivity;
import com.androidtitan.hotspots.main.ui.adapter.ImageAdapter;

import dagger.Component;

/**
 * Created by amohnacs on 3/14/16.
 */

@PresenterScope
@Component (
        //dependencies = { AppComponent.class },
        modules = { MainPresenterModule.class,
                    ImageDownloadModule.class}
)
public interface PresenterComponent {

    MainPresenter getMainPresenter();
    ImageDownloadPresenter getImagePresenter();

    // allows us to inject into ACTIVITY
    void inject(MainActivity activity);
    void inject(ImageAdapter adapter);



}
