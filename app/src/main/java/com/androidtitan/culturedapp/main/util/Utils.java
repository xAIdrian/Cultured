package com.androidtitan.culturedapp.main.util;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import com.androidtitan.culturedapp.main.CulturedApp;

/**
 * Created by amohnacs on 3/29/16.
 */
public class Utils {

    private static int screenWidth = 0;
    private static int screenHeight = 0;

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

    public static int getScreenHeight() {
        if(screenHeight == 0) {

            WindowManager wm = (WindowManager) CulturedApp.getAppComponent().getApplicationContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }
        return screenHeight;
    }

    public static int getScreenWidth() {
        if(screenWidth == 0) {

            WindowManager wm = (WindowManager) CulturedApp.getAppComponent().getApplicationContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.x;
        }
        return screenWidth;
    }

    public static boolean isAndroid5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
