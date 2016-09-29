package com.androidtitan.culturedapp.main.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by amohnacs on 9/28/16.
 */

public class FbInstanceIDService extends FirebaseInstanceIdService {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

    }

}
