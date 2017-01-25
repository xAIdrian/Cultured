package com.androidtitan.culturedapp.main.trending;

import android.content.Context;

import com.androidtitan.culturedapp.common.structure.BasePresenter;
import com.androidtitan.culturedapp.main.CulturedApp;

/**
 * Created by Adrian Mohnacs on 1/22/17.
 */

public class TrendingPresenter extends BasePresenter<TrendingMvp.View> implements TrendingMvp.Presenter,
    TrendingMvp.Provider.CallbackListener{
    private final String TAG = getClass().getSimpleName();

    private Context context;


    public TrendingPresenter(Context context) {
        this.context = context;

        CulturedApp.getAppComponent().inject(this);
    }

}
