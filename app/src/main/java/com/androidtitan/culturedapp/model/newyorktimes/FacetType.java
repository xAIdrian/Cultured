package com.androidtitan.culturedapp.model.newyorktimes;

/**
 * Created by Adrian Mohnacs on 12/27/16.
 */

public enum FacetType {
    DES("des"),
    PER("per"),
    ORG("org"),
    GEO("geo");

    private final String value;

    FacetType(final String text) {
        this.value = text;
    }


    @Override
    public String toString() {
        return value.toString();
    }
}
