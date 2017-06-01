package com.androidtitan.culturedapp.common.structure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Adrian Mohnacs on 5/31/17.
 */

public abstract class MvpActivity <P extends BasePresenter<V>, V> extends AppCompatActivity {

    public abstract P getPresenter();

    public abstract V getMvpView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getPresenter() != null) {
            getPresenter().subscribe(getMvpView());
            getPresenter().onCreate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getPresenter() != null) {
            getPresenter().onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(getPresenter() != null) {
            getPresenter().onPause();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(getPresenter() != null) {
            getPresenter().onStop();
            getPresenter().unsubscribe(getMvpView());
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(getPresenter() != null) {
            getPresenter().onDestroy();
        }
        super.onDestroy();
    }
}
