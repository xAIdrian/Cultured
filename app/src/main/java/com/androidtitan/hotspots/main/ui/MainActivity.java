package com.androidtitan.hotspots.main.ui;

import android.os.Bundle;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.common.BaseActivity;

public class MainActivity extends BaseActivity {



    private ImageListFragment imageListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageListFragment = new ImageListFragment();
        //
        getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .add(R.id.container, imageListFragment, "imageListFragment")
                .commit();


    }

}
