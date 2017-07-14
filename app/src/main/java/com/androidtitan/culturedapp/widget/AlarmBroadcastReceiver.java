package com.androidtitan.culturedapp.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

/**
 * Created by Triforce on 7/14/17.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        //get that lock yo
        wakeLock.acquire();

        //You can do the processing here update the widget/remote views.
        //todo: how do we get the articl here?  We can attach the Article as a parcelable to the intent and pull it out here.


        //release wakelock
        wakeLock.release();
    }
}
