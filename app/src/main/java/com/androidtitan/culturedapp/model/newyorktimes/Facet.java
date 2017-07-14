package com.androidtitan.culturedapp.model.newyorktimes;

import android.content.ContentValues;

import com.androidtitan.culturedapp.main.provider.DatabaseContract;

import java.util.Date;

/**
 * Created by Adrian Mohnacs on 12/27/16.
 */

public class Facet {

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
}
