package com.androidtitan.hotspots.main.ui;

import android.os.Bundle;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.common.BaseActivity;
import com.androidtitan.hotspots.main.application.App;
import com.androidtitan.hotspots.main.presenter.MainPresenter;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    @Inject
    MainPresenter mainPresenter;

    private SplashFragment splashFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //telling our component Where to inject Presenter
        App.getMainPresenterComponent().inject(this);


        splashFragment = new SplashFragment();
        //
        getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .add(R.id.container,splashFragment, "splashFragment")
                .commit();


    }

    public MainPresenter getMainPresenter(){
        return mainPresenter;
    }
}
