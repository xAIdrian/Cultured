package com.androidtitan.trooPTracker.Interface;

import com.androidtitan.trooPTracker.Data.Division;

/**
 * Created by amohnacs on 6/5/15.
 */
public interface LandingInterface {
    public void soldierListOpener(int groupPosition);
    public void divPasser(boolean isDiv, boolean isEdit, int divPosition);
    public void upTick(Division div);
}
