package com.androidtitan.culturedapp.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;

import com.androidtitan.culturedapp.R;

/**
 * The configuration screen for the {@link ImageAppWidgetProvider ImageAppWidgetProvider} AppWidget.
 */
public class ImageAppWidgetProviderConfigureActivity extends Activity implements View.OnClickListener{

    private static final String PREFS_NAME = "com.androidtitan.culturedapp.widget.ImageAppWidgetProvider";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;


    public ImageAppWidgetProviderConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.image_app_widget_provider);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        //setting up our views which we received from the AppWidgetProvider
        //mAppWidgetText.setText(loadTitlePref(ImageAppWidgetProviderConfigureActivity.this, mAppWidgetId));



        //updating the app widget from the configuration activity
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.image_app_widget_provider);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        //return Intent being sent back to the AppWidgetProvider
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    public void onClick(View v) {

    }
}

