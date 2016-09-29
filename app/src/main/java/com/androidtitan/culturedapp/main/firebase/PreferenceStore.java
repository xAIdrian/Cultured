package com.androidtitan.culturedapp.main.firebase;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by amohnacs on 9/28/16.
 */

public class PreferenceStore {
    private final String TAG = getClass().getSimpleName();

    private static final String FCM_TOKEN_KEY =
            "com.androidtitan.culturedapp.main.firebase.FCM_TOKEN";

    private static PreferenceStore preferenceStore;
    private Context context;

    public static PreferenceStore get(Context context) {
        if(preferenceStore == null) {
            preferenceStore = new PreferenceStore(context);
        }
        return preferenceStore;
    }

    public PreferenceStore(Context context) {
        this.context = context.getApplicationContext();
    }

    public String getFcmToken() {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(FCM_TOKEN_KEY, null);
    }

    public void setFcmToken(String token) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(FCM_TOKEN_KEY, token)
                .apply();
    }


}
