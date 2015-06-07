package com.androidtitan.trooPTracker.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooPTracker.Fragment.AdderFragment;
import com.androidtitan.trooPTracker.Interface.SecondInterface;

//ToDo: receive whether this is a SOLDIER or DIVISION
public class SecondActivity extends ActionBarActivity implements SecondInterface {
    private final String ADD_FRAG_TAG = "adderTag";

    private FragmentManager fragMag;
    private FragmentTransaction fragTran;
    private AdderFragment adderFragment;

    int divisionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        try {
            Intent intent = getIntent();
            divisionIndex = intent.getIntExtra("divIndex", -1);
        } catch (NullPointerException e) {

        }

        //onOrientationChange Block
        if(savedInstanceState != null) {
            //savedInstanceState, fragment may exist. Look up the instance that already exists by tag
            adderFragment = (AdderFragment) getSupportFragmentManager().findFragmentByTag(ADD_FRAG_TAG);
        }
        else if(adderFragment == null) {
            adderFragment = new AdderFragment();
        }
        if(!adderFragment.isInLayout()) {
            fragMag = getSupportFragmentManager();
            fragTran = fragMag.beginTransaction();
            fragTran.addToBackStack(null).replace(R.id.landingContainer, adderFragment, ADD_FRAG_TAG).commit();
        }

        //hide actionbar.  will it ever return? maybe...
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        divInteraction(divisionIndex);
    }


    //called from SecondF2AInterface.  Passes integer so main activity can page to
    //newly added soldier's division
    @Override
    public void divInteraction(int divSelected) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("landingDivision", divSelected);
        startActivity(intent);
    }
}
