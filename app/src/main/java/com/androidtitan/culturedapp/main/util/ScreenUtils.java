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
public class ScreenUtils {

    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static float getDensity(Context context){
        float scale = context.getResources().getDisplayMetrics().density;
        return scale;
    }

    public static int convertDiptoPix(Context context, int dip){
        float scale = getDensity(context);
        return (int) (dip * scale + 0.5f);
    }

    public static int convertPixtoDip(Context context, int pixel){
        float scale = getDensity(context);
        return (int)((pixel - 0.5f)/scale);
    }

    public static int getScreenHeight(Context context) {
        if(screenHeight == 0) {

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }
        return screenHeight;
    }

    public static int getScreenWidth(Context context) {
        if(screenWidth == 0) {

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.x;
        }
        return screenWidth;
    }

}
