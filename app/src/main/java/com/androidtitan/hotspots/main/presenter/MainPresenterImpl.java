package com.androidtitan.hotspots.main.presenter;

import android.content.Context;
import android.util.Log;

/**
 * Created by amohnacs on 3/15/16.
 */
public class MainPresenterImpl implements MainPresenter{

    private Context context;

    public MainPresenterImpl(Context context) {
        this.context = context;
    }

    public void respond(String string) {
        if(context != null) {
            Log.e("TAG", string);
        }
    }
}
