package com.androidtitan.culturedapp.main.toparticle.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by amohnacs on 8/7/16.
 */

public class ArticleSyncService extends Service {
    private final String TAG = getClass().getSimpleName();

    private static ArticleSyncAdapter syncAdapter = null;
    private static final Object syncAdapterLock = new Object();

    public ArticleSyncService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (syncAdapterLock) {
            if(syncAdapter == null) {
                syncAdapter = new ArticleSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return START_STICKY;
//    }
}
