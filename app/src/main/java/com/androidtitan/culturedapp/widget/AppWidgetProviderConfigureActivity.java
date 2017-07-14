package com.androidtitan.culturedapp.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.widget.ui.ImageWidgetProvider;

/**
 * The configuration screen for the {@link ImageWidgetProvider ImageWidgetProvider} AppWidget.
 * <p>
 * It is the responsibility of the configuration Activity to request an update from the
 * AppWidgetManager when the App Widget is first created. However, onUpdate() will be called
 * for subsequent updates—it is only skipped the first time.
 * <p>
 * When an App Widget uses a configuration Activity, it is the responsibility of the Activity to
 * update the App Widget when configuration is complete. You can do so by requesting an update
 * directly from the AppWidgetManager.
 */
public class AppWidgetProviderConfigureActivity extends Activity implements View.OnClickListener{

    private static final String PREFS_NAME = "com.androidtitan.culturedapp.widget.ui.ImageWidgetProvider";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;


    public AppWidgetProviderConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.image_widget_provider);

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
        //mAppWidgetText.setText(loadTitlePref(AppWidgetProviderConfigureActivity.this, mAppWidgetId));



        //updating the app widget from the configuration activity
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.image_widget_provider);
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

