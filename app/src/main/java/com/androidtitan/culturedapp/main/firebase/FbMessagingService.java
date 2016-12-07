package com.androidtitan.culturedapp.main.firebase;

import android.accounts.Account;
import android.os.Bundle;
import android.util.Log;

import com.androidtitan.culturedapp.model.provider.DatabaseContract;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.androidtitan.culturedapp.main.newsfeed.ui.NewsActivity.ACCOUNT;
import static com.androidtitan.culturedapp.main.newsfeed.ui.NewsActivity.ACCOUNT_TYPE;

/**
 * Created by amohnacs on 9/28/16.
 */

public class FbMessagingService extends FirebaseMessagingService {
    private final String TAG = getClass().getSimpleName();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "Received FCM from " + remoteMessage.getFrom() + " requesting Sync");
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        Account account = new Account(ACCOUNT, ACCOUNT_TYPE);

        getContentResolver().setIsSyncable(account, DatabaseContract.AUTHORITY, 1);
        getContentResolver().setSyncAutomatically(account, DatabaseContract.AUTHORITY, true);
        getContentResolver().requestSync(account, DatabaseContract.AUTHORITY, Bundle.EMPTY);
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {

    }

}
