package com.androidtitan.culturedapp.common;

import android.database.Cursor;

import com.androidtitan.culturedapp.main.provider.wrappers.FacetCursorWrapper;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;
import com.androidtitan.culturedapp.model.newyorktimes.FacetType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.androidtitan.culturedapp.model.newyorktimes.FacetType.DES;
import static com.androidtitan.culturedapp.model.newyorktimes.FacetType.GEO;
import static com.androidtitan.culturedapp.model.newyorktimes.FacetType.ORG;
import static com.androidtitan.culturedapp.model.newyorktimes.FacetType.PER;

/**
 * Created by Adrian Mohnacs on 2/3/17.
 */

public class FacetGenerator {

    public <T extends HashMap> T generateFacetMapFromCursor(Cursor cursor, T passedFacetMap, boolean storyFocused) {

        if (cursor != null && cursor.getCount() > 0) {
            FacetCursorWrapper wrapper = new FacetCursorWrapper(cursor);
            wrapper.moveToFirst();
            while (!wrapper.isAfterLast()) {
                Facet facet = wrapper.getFacet();

                switch (facet.getFacetType()) {
                    case DES:
                        if (storyFocused) {
                            updateFacetMap(passedFacetMap, null, DES, facet);
                        } else {
                            updateFacetMap(passedFacetMap, DES, facet);
                        }
                        break;
                    case ORG:
                        if (storyFocused) {
                            updateFacetMap(passedFacetMap, null, ORG, facet);
                        } else {
                            updateFacetMap(passedFacetMap, ORG, facet);
                        }
                        break;
                    case PER:
                        if (storyFocused) {
                            updateFacetMap(passedFacetMap, null, PER, facet);
                        } else {
                            updateFacetMap(passedFacetMap, PER, facet);
                        }
                        break;
                    case GEO:
                        if (storyFocused) {
                            updateFacetMap(passedFacetMap, null, GEO, facet);
                        } else {
                            updateFacetMap(passedFacetMap, GEO, facet);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid FacetType");
                }

                wrapper.moveToNext();
            }
            cursor.close();

        }

        return passedFacetMap;
    }

    private void updateFacetMap(HashMap<FacetType, List<Facet>> facetMap, FacetType facetType, Facet facet) {

        if(facetMap.get(facet.getFacetType()) != null) {
            List<Facet> facetArrayList = new ArrayList<>();
            facetArrayList.add(facet);
            facetMap.put(facetType, facetArrayList);

        } else {
            ArrayList<Facet> emptyFacetList = new ArrayList<>();
            facetMap.put(facetType, emptyFacetList);
            updateFacetMap(facetMap, facetType, facet);
        }
    }

    private void updateFacetMap(HashMap<FacetType, HashMap<Integer, List<Facet>>> facetMap,
                                HashMap<Integer, List<Facet>> storyIdMap,
                                FacetType facetType, Facet facet) {

        /*
        If our existing FacetMap does have a value associated with this key...update it
         */
        if(facetMap.get(facet.getFacetType()) != null) {
            storyIdMap = facetMap.get(facetType);
            // If our StoryId EXISTS and
            if(facetMap.get(facetType).get(facet.getStoryId()) != null) {

                List<Facet> facetArrayList = storyIdMap.get(facet.getStoryId());

                facetArrayList.add(facet);
                storyIdMap.put(facet.getStoryId(), facetArrayList);
                facetMap.put(facetType, storyIdMap);


            } else {
                List<Facet> facetArrayList = new ArrayList<>();

                facetArrayList.add(facet);
                storyIdMap.put(facet.getStoryId(), facetArrayList);
                facetMap.put(facetType, storyIdMap);
            }

        /*
            If our existing FacetMap does NOT have a value associated with the key...
                we are going to create the empty values instead of pulling it down and updating it
             */
        } else {
            storyIdMap = new HashMap<>();
            ArrayList<Facet> emptyFacetList = new ArrayList<>();
            storyIdMap.put(facet.getStoryId(), emptyFacetList);
            facetMap.put(facetType, storyIdMap);
            updateFacetMap(facetMap, storyIdMap, facetType, facet);
        }
    }
}
