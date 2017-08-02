package com.androidtitan.culturedapp.model.newyorktimes;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.androidtitan.culturedapp.main.provider.DatabaseContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Adrian Mohnacs on 12/27/16.
 */

public class Facet implements Parcelable{

    private int storyId;
    private FacetType facetType;
    private String facetText;
    private Date createdDate;

    public Facet() {
    }

    public Facet(FacetType facetType, String facetText, Date createdDate) {
        this.facetType = facetType;
        this.facetText = facetText;
        this.createdDate = createdDate;
    }

    public Facet(String storyId, FacetType facetType, String facetText, Date createdDate) {
        this.storyId = Integer.valueOf(storyId);;
        this.facetType = facetType;
        this.facetText = facetText;
        this.createdDate = createdDate;
    }

    public Facet(String storyId, FacetType facetType, String facetText) {
        this.storyId = Integer.valueOf(storyId);;
        this.facetType = facetType;
        this.facetText = facetText;
    }

    public Facet(String articleFacet) {
        this.facetText = articleFacet;
    }

    public Facet(Parcel in) {
        storyId = in.readInt();
        facetType = FacetType.valueOf(in.readString());
        facetText = in.readString();

        try {
            DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.ENGLISH);
            createdDate = format.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public ContentValues getContentValues() {

        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.FacetTable.STORY_ID, storyId);
        cv.put(DatabaseContract.FacetTable.TYPE, facetType.toString());
        cv.put(DatabaseContract.FacetTable.FACET, facetText);
        cv.put(DatabaseContract.FacetTable.CREATED_DATE, createdDate.toString());

        return cv;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public FacetType getFacetType() {
        return facetType;
    }

    public void setFacetType(FacetType facetType) {
        this.facetType = facetType;
    }

    public String getFacetText() {
        return facetText;
    }

    public void setFacetText(String facetText) {
        this.facetText = facetText;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(storyId);
        if (facetType != null) {
            dest.writeString(facetType.toString());
        }
        dest.writeString(facetText);
        if (createdDate != null) {
            dest.writeString(createdDate.toString());
        }
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Facet createFromParcel(Parcel source) {
            return new Facet(source);
        }

        @Override
        public Facet[] newArray(int size) {
            return new Facet[size];
        }
    };
}
