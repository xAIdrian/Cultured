package com.androidtitan.hotspots.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by amohnacs on 3/15/16.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //create things that are going to be needed in all of the Activities
        //todo: perhaps our 'Presenter'

    }
}
