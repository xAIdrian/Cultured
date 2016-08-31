package com.androidtitan.hotspots.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androidtitan.hotspots.main.CulturedApp;
import com.androidtitan.hotspots.main.inject.AppComponent;

/**
 * Created by amohnacs on 3/15/16.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    public AppComponent getAppComponent() {
        return CulturedApp.getAppComponent();
    }
}
