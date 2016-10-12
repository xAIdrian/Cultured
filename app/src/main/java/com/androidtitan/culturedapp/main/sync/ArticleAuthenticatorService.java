package com.androidtitan.culturedapp.main.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by amohnacs on 8/7/16.
 */

public class ArticleAuthenticatorService extends Service {
    private static final String TAG = "ArticleAuthenticatorService";

    private ArticleAuthenticator authenticator;

    @Override
    public void onCreate() {

        authenticator = new ArticleAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return START_STICKY;
//    }


}
