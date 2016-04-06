package com.androidtitan.hotspots.main.util;

import com.androidtitan.hotspots.main.CulturedApp;

/**
 * Created by amohnacs on 3/29/16.
 */
public class HelperMethods {

    public static float getDensity(){
        float scale = CulturedApp.getAppComponent().getApplicationContext()
                .getResources().getDisplayMetrics().density;
        return scale;
    }

    public static int convertDiptoPix(int dip){
        float scale = getDensity();
        return (int) (dip * scale + 0.5f);
    }

    public static int convertPixtoDip(int pixel){
        float scale = getDensity();
        return (int)((pixel - 0.5f)/scale);
    }
}
