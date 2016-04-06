package com.androidtitan.hotspots.main.presenter.music;

import com.androidtitan.hotspots.main.model.spotify.Item;
import com.androidtitan.hotspots.main.ui.activities.MusicActivity;

import java.util.List;

/**
 * Created by amohnacs on 3/14/16.
 */
public interface MusicPresenter {

    void takeActivity(MusicActivity activity);
    List<Item> querySpotifyTracks(String search, int count);

}
