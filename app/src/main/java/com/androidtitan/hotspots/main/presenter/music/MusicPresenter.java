package com.androidtitan.hotspots.main.presenter.music;

import com.androidtitan.hotspots.main.model.spotify.Item;

import java.util.List;

/**
 * Created by amohnacs on 3/14/16.
 */
public interface MusicPresenter {

    List<Item> querySpotifyTracks(String search, int count);

}
