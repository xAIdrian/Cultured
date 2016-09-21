package com.androidtitan.culturedapp.main.toparticle.service;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * Created by amohnacs on 8/7/16.
 */

public class ArticleSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "ArticleSyncAdapter";

    private ContentResolver contentResolver;

    public ArticleSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        contentResolver = context.getContentResolver();
    }

    public ArticleSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        contentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

        //make call to our retrofit endpoints
        //check to make sure how fresh it is...check your first article returned against the top article in the content provider
        //clean up



    }
}
