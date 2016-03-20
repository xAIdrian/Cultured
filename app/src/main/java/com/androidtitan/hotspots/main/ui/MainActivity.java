package com.androidtitan.hotspots.main.ui;

import android.os.Bundle;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.common.BaseActivity;
import com.androidtitan.hotspots.main.presenter.DaggerPresenterComponent;
import com.androidtitan.hotspots.main.presenter.PresenterComponent;
import com.androidtitan.hotspots.main.presenter.MainPresenterModule;

public class MainActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();

    private static PresenterComponent presenterComponent;


    private ImageListFragment imageListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenterComponent = DaggerPresenterComponent.builder()
                .mainPresenterModule(new MainPresenterModule(MainActivity.this)) //this can be removed
                .build();


        imageListFragment = new ImageListFragment();
        //
        getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .add(R.id.container, imageListFragment, "imageListFragment")
                .commit();


    }

    public static PresenterComponent getPresenterComponent() { //todo: Maybe this shouldn't be static?
        return presenterComponent;
    }

    public void updateImageAdapter() {
        if(imageListFragment != null) {
            imageListFragment.adapter.notifyDataSetChanged();
        }
    }

}
