package com.androidtitan.hotspots.main.presenter;

import com.androidtitan.hotspots.main.model.Item;

import java.util.List;

/**
 * Created by amohnacs on 3/14/16.
 */
public interface MainPresenter {

    void respond(String string);
    List<Item> querySpotifyTracks(String search, int count);

}
