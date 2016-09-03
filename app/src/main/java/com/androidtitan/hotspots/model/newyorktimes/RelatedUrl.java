package com.androidtitan.hotspots.model.newyorktimes;

/**
 * Created by amohnacs on 3/23/16.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class RelatedUrl implements Parcelable {

    @SerializedName("suggested_link_text")
    @Expose
    private String suggestedLinkText;
    @SerializedName("url")
    @Expose
    private String url;

    public RelatedUrl () {

    }

    public RelatedUrl(Parcel in) {
        suggestedLinkText = in.readString();
        url = in.readString();
    }

    /**
     * @return The suggestedLinkText
     */
    public String getSuggestedLinkText() {
        return suggestedLinkText;
    }

    /**
     * @param suggestedLinkText The suggested_link_text
     */
    public void setSuggestedLinkText(String suggestedLinkText) {
        this.suggestedLinkText = suggestedLinkText;
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    public void setUrl(String url) {
        this.url = url;
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

        dest.writeString(suggestedLinkText);
        dest.writeString(url);
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
        public RelatedUrl createFromParcel(Parcel source) {
            return new RelatedUrl(source);
        }

        /**
         * Create a new array of the Parcelable class.
         *
         * @param size Size of the array.
         * @return Returns an array of the Parcelable class, with every entry
         * initialized to null.
         */
        @Override
        public RelatedUrl[] newArray(int size) {
            return new RelatedUrl[size];
        }
    };
}
