package com.androidtitan.culturedapp.main.web.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;

import com.androidtitan.culturedapp.main.provider.DatabaseContract;

/**
 * Created by Adrian Mohnacs on 12/29/16.
 */

public class FacetDeleteService extends Service {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, TAG + " onStartCommand");

        getContentResolver().delete(DatabaseContract.FacetTable.CONTENT_URI,
                "story_id is null", null);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
