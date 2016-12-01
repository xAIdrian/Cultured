package com.androidtitan.culturedapp.model.newyorktimes;

/**
 * Created by amohnacs on 3/21/16.
 */

import android.content.ContentValues;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.androidtitan.culturedapp.model.provider.DatabaseContract;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Multimedium implements Parcelable {

    private long storyId;
    private MediaSize size;

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("subtype")
    @Expose
    private String subtype;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("copyright")
    @Expose
    private String copyright;

    public Multimedium(){

    }

    public Multimedium(String url, Integer height,
                       Integer width, String caption) {
        this.url = url;
        this.height = height;
        this.width = width;
        this.caption = caption;
    }

    public Multimedium(String storyId, String size, String url, String format, int height, int width, String type, String subtype, String caption, String copyright) {

        this.storyId = Integer.valueOf(storyId);
        this.size = getMediaSize(size);
        this.url = url;
        this.format = format;
        this.height = height;
        this.width = width;
        this.type = type;
        this.subtype = subtype;
        this.caption = caption;
        this.copyright = copyright;

    }

    //must be read these in the same order as your wrote them
    public Multimedium(Parcel in) {

        url = in.readString();
        format = in.readString();
        height = in.readInt();
        width = in.readInt();
        type = in.readString();
        subtype = in.readString();
        caption = in.readString();
        copyright = in.readString();

    }

    public ContentValues getContentValues() {

        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.MediaTable.STORY_ID, storyId);
        cv.put(DatabaseContract.MediaTable.SIZE, size.toString());
        cv.put(DatabaseContract.MediaTable.URL, url);
        cv.put(DatabaseContract.MediaTable.FORMAT, format);
        cv.put(DatabaseContract.MediaTable.HEIGHT, height);
        cv.put(DatabaseContract.MediaTable.WIDTH, width);
        cv.put(DatabaseContract.MediaTable.TYPE, type);
        cv.put(DatabaseContract.MediaTable.SUBTYPE, subtype);
        cv.put(DatabaseContract.MediaTable.CAPTION, caption);
        cv.put(DatabaseContract.MediaTable.COPYRIGHT, copyright);

        return cv;
    }

    public long getStoryId() {
        return storyId;
    }

    public void setStoryId(long storyId) {
        this.storyId = storyId;
    }

    public MediaSize getSize() {
        return size;
    }

    public void setSize(MediaSize size) {
        this.size = size;
    }

    /**
     *
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     * The format
     */
    public String getFormat() {
        return format;
    }

    /**
     *
     * @param format
     * The format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     *
     * @return
     * The height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     *
     * @param height
     * The height
     */
    public void setHeight(Integer height) {
        this.height = height;
        setMediaSize(height);
    }

    /**
     *
     * @return
     * The width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     *
     * @param width
     * The width
     */
    public void setWidth(Integer width) {
        this.width = width;
        setMediaSize(width);

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
     * The subtype
     */
    public String getSubtype() {
        return subtype;
    }

    /**
     *
     * @param subtype
     * The subtype
     */
    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    /**
     *
     * @return
     * The caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     *
     * @param caption
     * The caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     *
     * @return
     * The copyright
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     *
     * @param copyright
     * The copyright
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return hashCode();
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(format);
        dest.writeInt(height);
        dest.writeInt(width);
        dest.writeString(type);
        dest.writeString(subtype);
        dest.writeString(caption);
        dest.writeString(copyright);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        /**
         * Create a new instance of the Parcelable class, instantiating it
         * from the given Parcel whose data had previously been written by
         * {@link Parcelable#writeToParcel Parcelable.writeToParcel()}.
         *
         * @param source The Parcel to read the object's data from.
         * @return Returns a new instance of the Parcelable class.
         */
        @Override
        public Multimedium createFromParcel(Parcel source) {
            return new Multimedium(source);
        }

        /**
         * Create a new array of the Parcelable class.
         *
         * @param size Size of the array.
         * @return Returns an array of the Parcelable class, with every entry
         * initialized to null.
         */
        @Override
        public Multimedium[] newArray(int size) {
            return new Multimedium[size];
        }
    };

    // todo: This can be reinforced with some returns, null checks, and number ranges
    private void setMediaSize(Integer height) {

        switch (height) {
            case 75:
                size = MediaSize.XSMALL;
                break;

            case 150:
                size = MediaSize.SMALL;
                break;

            case 127:
                size = MediaSize.MEDIUM;
                break;

            case 140:
                size = MediaSize.LARGE;
                break;

            case 1365:
                size = MediaSize.XLARGE;
                break;
        }

    }

    public MediaSize getMediaSize(String incomingSize) {

        switch (incomingSize) {
            case "xsmall":
                return MediaSize.XSMALL;

            case "small":
                return MediaSize.SMALL;

            case "medium":
                return MediaSize.MEDIUM;

            case "large":
                return MediaSize.LARGE;

            case "xlarge":
                return  MediaSize.XLARGE;

            default:
                return null;
        }
    }
}