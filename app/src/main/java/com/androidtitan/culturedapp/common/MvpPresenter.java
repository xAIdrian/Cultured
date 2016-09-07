package com.androidtitan.culturedapp.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the MvpView type that wants to be attached with.
 */
public interface MvpPresenter<V extends MvpView> {

    void onCreate(@Nullable Bundle androidBundle);

    void onSavedInstanceState(@NonNull Bundle androidBundle);

    void onDestroy();

    void bindView(V mvpView);

    void unbindView();
}
