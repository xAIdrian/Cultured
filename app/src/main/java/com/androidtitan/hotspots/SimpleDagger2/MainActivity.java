package com.androidtitan.hotspots.SimpleDagger2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androidtitan.hotspots.R;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //App.getPresenterComponent().inject(this);

        //presenter.respond("here I am you cocksuckers!");

    }
}
