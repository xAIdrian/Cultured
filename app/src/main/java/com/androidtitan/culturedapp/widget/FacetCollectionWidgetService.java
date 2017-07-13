package com.androidtitan.culturedapp.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Created by Adrian Mohnacs on 7/1/17.
 */

public class FacetCollectionWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Log.e("FacetWidgetService", "getViewFactory()");

        return new FacetCollectionRemoteViewFactory(this, intent);
    }
}
