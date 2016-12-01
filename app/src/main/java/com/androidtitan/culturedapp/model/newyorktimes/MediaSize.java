package com.androidtitan.culturedapp.model.newyorktimes;

/**
 * Created by amohnacs on 11/30/16.
 */

public enum MediaSize {

    XSMALL("xsmall"),
    SMALL("small"),
    MEDIUM("medium"),
    LARGE("large"),
    XLARGE("xlarge");

    private String value;

    MediaSize(String size) {
        this.value = size;
    }

    //  do we even need this?
    public boolean equalsValue(String incomingSize) {
        return (incomingSize == null) ? false : value.equals(incomingSize);
    }

    public String toString() {
        return this.value;
    }
}
