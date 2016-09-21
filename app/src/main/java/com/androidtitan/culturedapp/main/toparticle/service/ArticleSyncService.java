package com.androidtitan.culturedapp.main.toparticle.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by amohnacs on 8/7/16.
 */

public class ArticleSyncService extends Service {
    private static final String TAG = "SyncService";

    private static ArticleSyncAdapter syncAdapter = null;
    private static final Object syncAdapterLock = new Object();

    @Override
    public void onCreate() {

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
}
