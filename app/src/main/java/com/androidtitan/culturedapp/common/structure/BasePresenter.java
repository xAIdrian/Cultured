package com.androidtitan.culturedapp.common.structure;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Base class that implements the MainPresenter interface and provides a base implementation for
 * bindView() and unbindView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
public class BasePresenter<T extends MvpView> implements MvpPresenter<T> {

    private T mMvpView;

    // Methods tied to the View lifecycle

    @Override
    public void onCreate(@Nullable Bundle androidBundle) {

    }

    @Override
    public void onSavedInstanceState(@NonNull Bundle androidBundle) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void bindView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void unbindView() {
        mMvpView = null;
    }


    // ---------------
    // Convenience methods.  Let's use these.

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public T getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call MainPresenter.bindView(MvpView) before" +
                    " requesting data to the MainPresenter");
        }
    }
}
