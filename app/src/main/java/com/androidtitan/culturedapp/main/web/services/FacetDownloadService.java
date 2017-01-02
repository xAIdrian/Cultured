package com.androidtitan.culturedapp.main.web.services;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.structure.RxHelper;
import com.androidtitan.culturedapp.main.provider.DatabaseContract;
import com.androidtitan.culturedapp.main.web.retrofit.NewsEndpoint;
import com.androidtitan.culturedapp.main.web.retrofit.ServiceGenerator;
import com.androidtitan.culturedapp.model.ApiError;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;
import com.androidtitan.culturedapp.model.newyorktimes.NewsResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static com.androidtitan.culturedapp.common.Constants.NO_ARTICLE_ID;

/**
 * Created by Adrian Mohnacs on 12/29/16.
 */

public class FacetDownloadService extends Service {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, TAG + " onStartCommand");



        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
