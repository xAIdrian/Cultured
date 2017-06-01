package com.androidtitan.culturedapp.common.structure;

import android.app.Fragment;

/**
 * Created by Adrian Mohnacs on 5/31/17.
 */

public abstract class MvpFragment<P extends BasePresenter<V>, V> extends Fragment{

    public abstract P getPresenter();

    public abstract V getMvpView();

    @Override
    public void onStart() {
        super.onStart();
        if(getPresenter() != null) {
            getPresenter().subscribe(getMvpView());
            getPresenter().onStart();
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
