package com.androidtitan.culturedapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.main.CulturedApp;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.widget.ui.ImageWidgetProvider;

import static com.androidtitan.culturedapp.widget.ui.ImageWidgetProvider.ALARM_BROADCAST_GEO_FACET;
import static com.androidtitan.culturedapp.widget.ui.ImageWidgetProvider.ALARM_BROADCAST_HEIGHT;
import static com.androidtitan.culturedapp.widget.ui.ImageWidgetProvider.ALARM_BROADCAST_TITLE;
import static com.androidtitan.culturedapp.widget.ui.ImageWidgetProvider.ALARM_BROADCAST_URL;
import static com.androidtitan.culturedapp.widget.ui.ImageWidgetProvider.ALARM_BROADCAST_WIDTH;

/**
 * Created by Triforce on 7/14/17.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, TAG);

        PowerManager powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        //get that lock yo
        wakeLock.acquire();

        if(intent != null) {

            buildArticle(intent);

            RemoteViews views = new RemoteViews(CulturedApp.getAppContext().getPackageName(), R.layout.image_widget_provider);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName appWidgetComponent = new ComponentName(context, ImageWidgetProvider.class);
            WidgetSharedUpdater.updateAppWidget(views, manager, appWidgetComponent, null);
        }

        //release wakelock
        wakeLock.release();
    }

    private Article buildArticle(Intent intent) {
        // TODO: 8/29/17 Unable to start receiver com.androidtitan.culturedapp.widget.AlarmBroadcastReceiver: java.lang.NullPointerException: println needs a message

        String articleTitle = intent.getStringExtra(ALARM_BROADCAST_TITLE);
        String articleFacet = intent.getStringExtra(ALARM_BROADCAST_GEO_FACET);

        String articleMediaUrl = intent.getStringExtra(ALARM_BROADCAST_URL);
        int articleMediaWidth = intent.getIntExtra(ALARM_BROADCAST_WIDTH, 0);
        int articleMediaHeight = intent.getIntExtra(ALARM_BROADCAST_HEIGHT, 0);

        /*
        Log.e(TAG, articleTitle);
        Log.e(TAG, articleFacet);
        Log.e(TAG, articleMediaUrl);
        Log.e(TAG, String.valueOf(articleMediaWidth));
        Log.e(TAG, String.valueOf(articleMediaHeight));
        */

        return null;


    }
}
