package com.androidtitan.culturedapp.common.structure;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;


/**
 * Base class that implements the MainPresenter interface and provides a base implementation for
 * subscribe() and unsubscribe(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
public abstract class BasePresenter<V> implements MvpPresenter<V> {

    private V mMvpView;

    /**
     * Lifecycle methods
     */

    @UiThread
    public void onCreate(){
    }

    @UiThread
    public void onResume() {
    }

    @UiThread
    public void onPause() {
    }

    @UiThread
    public void onStop() {
    }

    @UiThread
    public void onStart() {
    }

    @UiThread
    public void onDestroy() {
    }

    @Override
    public void subscribe(@NonNull V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void unsubscribe(@NonNull V view) {
        mMvpView = null;
    }


    // ---------------
    // Convenience methods.  Let's use these.

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call MainPresenter.subscribe(MvpView) before" +
                    " requesting data to the MainPresenter");
        }
    }
}
