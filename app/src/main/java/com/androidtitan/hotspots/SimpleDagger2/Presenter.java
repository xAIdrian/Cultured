package com.androidtitan.hotspots.SimpleDagger2;

import android.content.Context;
import android.util.Log;

/**
 * Created by amohnacs on 3/14/16.
 */
public class Presenter {

    private Context context;


    public Presenter(Context context) {
        this.context = context;
    }

    public void respond(String string) {
        if(context != null) {
            Log.e("TAG", string);
        }
    }

}
