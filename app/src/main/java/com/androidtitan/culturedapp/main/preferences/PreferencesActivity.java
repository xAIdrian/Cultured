package com.androidtitan.culturedapp.main.preferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.androidtitan.culturedapp.R;

public class PreferencesActivity extends AppCompatPreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener, PreferencesFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupActionBar();

        String preferenceFragName = PreferencesFragment.class.getName();
        switchToPreferenceFragment(preferenceFragName, null);

    }

    @Override
    protected void onResume() {
        super.onResume();

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        //todo: this is applicable for when we change the style of the app

        if(key.equals(getString(R.string.pref_key_name))) {
            // this action is handled inside of our activity

        }

        /* Storing a reference to the listener in an instance data field of an object that will exist
            as long as the listener is needed:

            SharedPreferences.OnSharedPreferenceChangeListener listener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {

                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                // listener implementation
                }
            };

            prefs.registerOnSharedPreferenceChangeListener(listener);

         */
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferencesFragment.class.getName().equals(fragmentName);
    }

    /**
     * We ensure the fragment is valid and commited with a fade transition
     * @param fragmentName
     * @param args
     */
    private void switchToPreferenceFragment(String fragmentName, Bundle args) {

        if(!isValidFragment(fragmentName)) {
            throw new IllegalArgumentException(fragmentName + " is an Invalid Fragment for this activity");
        }

        Fragment fragment = Fragment.instantiate(this, fragmentName, args);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(android.R.id.content, fragment);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
