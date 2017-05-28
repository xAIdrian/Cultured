package com.androidtitan.culturedapp.main;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.androidtitan.culturedapp.main.inject.AppComponent;
import com.androidtitan.culturedapp.main.inject.AppModule;
import com.androidtitan.culturedapp.main.web.services.FacetDownloadJobService;
import com.androidtitan.culturedapp.main.web.services.FacetDeleteService;

import java.util.Calendar;

import static android.app.job.JobInfo.BACKOFF_POLICY_EXPONENTIAL;
import static android.app.job.JobInfo.NETWORK_TYPE_ANY;
import static com.androidtitan.culturedapp.common.Constants.FACET_JOB_SCHEDULER;

/**
 * Created by amohnacs on 3/14/16.
 */
public class CulturedApp extends Application {

    private static AppComponent appComponent;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

       appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        context = getBaseContext();

        launchTrendingFacetServices();
    }

    public static AppComponent getAppComponent(){
        return appComponent;
    }

    public static Context getAppContext() {
        return context;
    }

    public void launchTrendingFacetServices() {

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(FACET_JOB_SCHEDULER,
                new ComponentName(getPackageName(), FacetDownloadJobService.class.getName()));
        builder.setBackoffCriteria(10000, BACKOFF_POLICY_EXPONENTIAL);
        builder.setRequiredNetworkType(NETWORK_TYPE_ANY);
        builder.setPeriodic(300000);// 5 minutes. (AlarmManager.INTERVAL_FIFTEEN_MINUTES); //every two hours

        if( jobScheduler.schedule( builder.build() ) <= 0 ) {
            //If something goes wrong
        }
        //jobscheduler.cancelall()

        Intent intent = new Intent(this, FacetDeleteService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),
                AlarmManager.INTERVAL_DAY * 14, pendingIntent); //every two weeks
    }
}


