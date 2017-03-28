package com.androidtitan.culturedapp.main.provider;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.androidtitan.culturedapp.main.firebase.PreferenceStore;
import com.androidtitan.culturedapp.main.newsfeed.ui.NewsFeedActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.iid.InstanceID;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

import static com.androidtitan.culturedapp.common.Constants.PREFERENCES_SYNCING_PERIODICALLY;

/**
 * Created by Adrian Mohnacs on 3/28/17.
 */

public class FCMRegistrationTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = FCMRegistrationTask.class.getSimpleName();

    private static final String SENDER_ID = "612691836045";
    // Sync interval constants...one hour
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 180L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE;

    private Context context;
    private SharedPreferences sharedPreferences;
    private Account account;
    private boolean isSyncingPeriodically;

    public FCMRegistrationTask(Context context, SharedPreferences sharedPreferences, Account account, boolean isSyncingPeriodically) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.account = account;
        this.isSyncingPeriodically = isSyncingPeriodically;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {

        if(context == null) {
            return null;
        }

        int googleApiAvailable = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(context);
        if (googleApiAvailable != ConnectionResult.SUCCESS) {
            Log.e(TAG, "Play services not available, cannot register for GCM");
            return null;
        }

        InstanceID instanceID = InstanceID.getInstance(context);

        try {
            String token = instanceID.getToken(SENDER_ID, FirebaseMessaging.INSTANCE_ID_SCOPE, null);
            Log.d(TAG, "Got token: " + token);
            return token;

        } catch (IOException e) {
            Log.e(TAG, "Failed to get token from InstanceID", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String token) {
        super.onPostExecute(token);

        if (token == null) {
            setupPeriodicSync();
        } else {
            PreferenceStore.get(context).setFcmToken(token);
        }
    }

    public void setupPeriodicSync() {

        isSyncingPeriodically = true;
        //todo: interface callback to Activity updating the syncstatus
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFERENCES_SYNCING_PERIODICALLY, true).apply();

        ContentResolver.setIsSyncable(account, DatabaseContract.AUTHORITY, 1);
        ContentResolver.setSyncAutomatically(
                account, DatabaseContract.AUTHORITY, true);
        ContentResolver.addPeriodicSync(
                account, DatabaseContract.AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);
        ContentResolver.requestSync(account, DatabaseContract.AUTHORITY, Bundle.EMPTY);
    }

    public interface FCMRegistratorCallback {
        void updateSyncingStatus(boolean syncStatus);
    }
}
