package com.androidtitan.culturedapp.common.structure;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androidtitan.culturedapp.main.CulturedApp;
import com.androidtitan.culturedapp.main.inject.AppComponent;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectViews();
    }

    /**
     * Replace every field annotated with ButterKnife annotations like @InjectView with the proper
     * value.
     */
    private void injectViews() {
        ButterKnife.bind(this);
    }

    public AppComponent getAppComponent() {
        return CulturedApp.getAppComponent();

    }
}



