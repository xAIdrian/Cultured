package com.androidtitan.trooptracker.Interface;

import com.androidtitan.trooptracker.Data.LocationBundle;

import java.util.List;

/**
 * Created by amohnacs on 7/19/15.
 */
public interface MapsPullInterface {
    //we will need this as a PULL
    //for when we need to pull data from a fragment
    public void onDialogCompletion(LocationBundle locationBundle, List<LocationBundle> daBundle);
}
