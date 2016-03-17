package com.androidtitan.hotspots.main.model;

/**
 * Created by amohnacs on 3/16/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ExternalIds {

    @SerializedName("isrc")
    @Expose
    private String isrc;

    /**
     *
     * @return
     * The isrc
     */
    public String getIsrc() {
        return isrc;
    }

    /**
     *
     * @param isrc
     * The isrc
     */
    public void setIsrc(String isrc) {
        this.isrc = isrc;
    }

}
