package com.androidtitan.culturedapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.RemoteViews;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.main.CulturedApp;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.widget.ui.ImageWidgetProvider;

/**
 * Created by Triforce on 7/14/17.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmBroadcastReceiver.class.getSimpleName();

    private Article passedProviderArticle;

    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        //get that lock yo
        wakeLock.acquire();

        if(intent != null) {
            passedProviderArticle = intent.getParcelableExtra(ImageWidgetProvider.ALARM_ARTICLE);

            RemoteViews views = new RemoteViews(CulturedApp.getAppContext().getPackageName(), R.layout.image_widget_provider);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName appWidgetComponent = new ComponentName(context, ImageWidgetProvider.class);
            WidgetSharedUpdater.updateAppWidget(views, manager, appWidgetComponent, passedProviderArticle);
        }

        //release wakelock
        wakeLock.release();
    }
}
