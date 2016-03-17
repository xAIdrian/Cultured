package com.androidtitan.hotspots.main.model;

/**
 * Created by amohnacs on 3/16/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Track {

    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("popularity")
    @Expose
    private Integer popularity;
    @SerializedName("preview_url")
    @Expose
    private String previewUrl;
    @SerializedName("track_number")
    @Expose
    private Integer trackNumber;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("uri")
    @Expose
    private String uri;

    /**
     *
     * @return
     * The href
     */
    public String getHref() {
        return href;
    }

    /**
     *
     * @param href
     * The href
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The popularity
     */
    public Integer getPopularity() {
        return popularity;
    }

    /**
     *
     * @param popularity
     * The popularity
     */
    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    /**
     *
     * @return
     * The previewUrl
     */
    public String getPreviewUrl() {
        return previewUrl;
    }

    /**
     *
     * @param previewUrl
     * The preview_url
     */
    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    /**
     *
     * @return
     * The trackNumber
     */
    public Integer getTrackNumber() {
        return trackNumber;
    }

    /**
     *
     * @param trackNumber
     * The track_number
     */
    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The uri
     */
    public String getUri() {
        return uri;
    }

    /**
     *
     * @param uri
     * The uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

}
