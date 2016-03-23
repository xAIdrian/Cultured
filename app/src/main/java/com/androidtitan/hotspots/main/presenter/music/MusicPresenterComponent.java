package com.androidtitan.hotspots.main.presenter.music;

import com.androidtitan.hotspots.main.scopes.sPresenterScope;
import com.androidtitan.hotspots.main.ui.MusicActivity;
import com.androidtitan.hotspots.main.ui.adapter.MusicAdapter;

import dagger.Component;

/**
 * Created by amohnacs on 3/14/16.
 */

@sPresenterScope
@Component (
        modules = { MusicPresenterModule.class }
)
public interface MusicPresenterComponent {

    MusicPresenter getMainPresenter();

    // allows us to inject into ACTIVITY
    void inject(MusicActivity activity);
    void inject(MusicAdapter adapter);

}
