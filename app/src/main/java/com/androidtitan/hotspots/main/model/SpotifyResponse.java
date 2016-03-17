package com.androidtitan.hotspots.main.model;

/**
 * Created by amohnacs on 3/16/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class SpotifyResponse {

    @SerializedName("tracks")
    @Expose
    private Tracks tracks;

    /**
     *
     * @return
     * The tracks
     */
    public Tracks getTracks() {
        return tracks;
    }

    /**
     *
     * @param tracks
     * The tracks
     */
    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }

}
