package com.androidtitan.culturedapp.main.provider.wrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.androidtitan.culturedapp.main.provider.DatabaseContract;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;
import com.androidtitan.culturedapp.model.newyorktimes.FacetType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Adrian Mohnacs on 12/27/16.
 */

public class FacetCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public FacetCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Facet getFacet() {
        String storyId = getString(getColumnIndex(DatabaseContract.FacetTable.STORY_ID));
        FacetType facetType;
        switch (getString(getColumnIndex(DatabaseContract.FacetTable.TYPE))) {
            case "des":
                facetType = FacetType.DES;
                break;
            case "geo":
                facetType = FacetType.GEO;
                break;
            case "per":
                facetType = FacetType.PER;
                break;
            case "org":
                facetType = FacetType.ORG;
                break;
            default:
                throw new IllegalArgumentException("Incorrect String passed for FacetType");
        }
        String facetText = getString(getColumnIndex(DatabaseContract.FacetTable.FACET));

        try {
            String facetDateString = getString(getColumnIndex(DatabaseContract.FacetTable.CREATED_DATE));
            /*
            Date createdDate = convert Date
             */
            DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            Date date = new Date();
            try {
                date = format.parse(facetDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return new Facet(storyId, facetType, facetText, date);
        } catch (IllegalStateException e) {
            e.printStackTrace();

            return new Facet(storyId, facetType, facetText);
        }



    }
}
