package com.androidtitan.culturedapp.main.toparticle.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by amohnacs on 8/7/16.
 */

public class ArticleAuthenticatorService extends Service {
    private static final String TAG = "ArticleAuthenticatorService";

    private ArticleAuthenticator articleAuthenticator;

    @Override
    public void onCreate() {

        articleAuthenticator = new ArticleAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return articleAuthenticator.getIBinder();
    }
}
