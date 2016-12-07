package com.androidtitan.culturedapp.main.toparticle;

import android.graphics.Bitmap;

import com.androidtitan.culturedapp.common.structure.MvpView;

/**
 * Created by amohnacs on 9/19/16.
 */

public interface TopArticleMvp {

    interface Provider {

        void fetchTopArticleImage(CallbackListener listener);


        interface CallbackListener {

            void topArticleImageLoaded(Bitmap bitmap);
        }

    }

    interface Presenter {

        void loadTopArticleImage();

    }

    interface View extends MvpView {

        void displayTopArticleImage(Bitmap bitmap);
    }
}
