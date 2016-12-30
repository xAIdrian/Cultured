package com.androidtitan.culturedapp.main;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;

import com.androidtitan.culturedapp.main.inject.AppComponent;
import com.androidtitan.culturedapp.main.inject.AppModule;
import com.androidtitan.culturedapp.main.inject.DaggerAppComponent;
import com.androidtitan.culturedapp.main.web.services.FacetDownloadService;

import java.util.Calendar;

/**
 * Created by amohnacs on 3/14/16.
 */
public class CulturedApp extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        createFacetAlarm();
    }

    public static AppComponent getAppComponent(){
        return appComponent;
    }

    public void createFacetAlarm() {

        Intent intent = new Intent(this, FacetDownloadService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),
                AlarmManager.INTERVAL_HOUR * 2, pendingIntent);
    }
}


