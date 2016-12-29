package com.androidtitan.culturedapp.main.toparticle.wrappers;

import com.androidtitan.culturedapp.model.newyorktimes.Facet;
import com.androidtitan.culturedapp.model.newyorktimes.FacetType;

import java.util.List;

/**
 * Created by Adrian Mohnacs on 12/27/16.
 */

public class FacetWrapper {
    public FacetType facetType;
    public List<Facet> facetList;

    public FacetWrapper(FacetType facetType, List<Facet> facetList) {
        this.facetType = facetType;
        this.facetList = facetList;
    }

    public FacetType getFacetType() {
        return facetType;
    }

    public void setFacetType(FacetType facetType) {
        this.facetType = facetType;
    }

    public List<Facet> getFacetList() {
        return facetList;
    }

    public void setFacetList(List<Facet> facetList) {
        this.facetList = facetList;
    }

    public void appendFacetList(Facet facet) {
        this.facetList.add(facet);
    }
}
