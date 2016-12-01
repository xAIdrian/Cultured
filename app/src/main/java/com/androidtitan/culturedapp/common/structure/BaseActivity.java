package com.androidtitan.culturedapp.common.structure;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androidtitan.culturedapp.main.CulturedApp;
import com.androidtitan.culturedapp.main.inject.AppComponent;


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
