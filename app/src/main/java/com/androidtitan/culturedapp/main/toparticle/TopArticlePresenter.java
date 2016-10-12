package com.androidtitan.culturedapp.main.toparticle;

import android.content.Context;
import android.graphics.Bitmap;

import com.androidtitan.culturedapp.common.BasePresenter;
import com.androidtitan.culturedapp.main.CulturedApp;

import javax.inject.Inject;

/**
 * Created by amohnacs on 9/19/16.
 */

//todo: The functionality that the PRESENTER contains should be driven by the Business Requirements.

public class TopArticlePresenter extends BasePresenter<TopArticleMvp.View> implements TopArticleMvp.Presenter,
        TopArticleMvp.Provider.CallbackListener, ContentProviderInterface.ProviderCallback {
    private final String TAG = getClass().getSimpleName();

    private Context context;

    @Inject
    TopArticleProvider topArticleProvider;

    public TopArticlePresenter(Context context) {
        this.context = context;

        CulturedApp.getAppComponent().inject(this);
    }

    @Override
    public void loadTopArticleImage() {

    }

    @Override
    public void topArticleImageLoaded(Bitmap bitmap) {

    }

    @Override
    public void SQLiteCreationComplete(boolean isSuccessful) {
        //if it does not exist then we are not going to load anything..it should be though.
    }
}
