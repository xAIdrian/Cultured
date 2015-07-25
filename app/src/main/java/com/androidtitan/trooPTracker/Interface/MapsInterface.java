package com.androidtitan.trooptracker.Interface;

import com.androidtitan.trooptracker.Data.LocationBundle;

/**
 * Created by amohnacs on 7/19/15.
 */
public interface MapsInterface {
    //we will need this as a PULL
    //for when we need to pull data from a fragment
    public void dialogMarkerAdd(LocationBundle locationBundle);
}
