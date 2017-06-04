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
        if(getPresenter() != null) {
            getPresenter().subscribe(getMvpView());
            getPresenter().onCreate();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        if(getPresenter() != null) {
            getPresenter().onResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if(getPresenter() != null) {
            getPresenter().onPause();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if(getPresenter() != null) {
            getPresenter().onStop();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if(getPresenter() != null) {
            getPresenter().onDestroy();
            getPresenter().unsubscribe(getMvpView());
        }
        super.onDestroy();
    }
}
