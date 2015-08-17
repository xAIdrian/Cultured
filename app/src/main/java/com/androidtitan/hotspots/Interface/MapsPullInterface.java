package com.androidtitan.hotspots.Interface;

import com.androidtitan.hotspots.Data.LocationBundle;

/**
 * Created by amohnacs on 7/19/15.
 */
public interface MapsPullInterface {
    //we will need this as a PULL
    //for when we need to pull data from a fragment
    public void onDialogCompletion(LocationBundle locationBundle);
}
