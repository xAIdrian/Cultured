package com.androidtitan.trooPTracker.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooPTracker.Fragment.AdderFragment;
import com.androidtitan.trooPTracker.Interface.AdderInterface;

//ToDo: receive whether this is a SOLDIER or DIVISION
public class AdderActivity extends FragmentActivity implements AdderInterface {
    private final String ADD_FRAG_TAG = "adderTag";

    private FragmentManager fragMag;
    private FragmentTransaction fragTran;
    private AdderFragment adderFragment;

    private int divisionIndex;
    private int soldierIndex;
    private String soldierFname;
    private String soldierLname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adder);

        try {
            getSoldierEditItems();

        } catch(NullPointerException e) {
            Log.e("AAonCreate", String.valueOf(e));
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
        Intent intent = new Intent(this, ChampionActivity.class);
        intent.putExtra("landingDivision", divSelected);
        startActivity(intent);
    }

    public String getSoldierEditItems() {
        Intent intent = getIntent();
        divisionIndex = intent.getIntExtra("editSoloDivIndex", -1);
        soldierIndex = intent.getIntExtra("editSoloIndex", -1);
        soldierFname = intent.getStringExtra("editSoloFirst");
        soldierLname = intent.getStringExtra("editSoloLast");

        Bundle editArgs = new Bundle();
        editArgs.putInt("editSoloDivIndex", divisionIndex);
        editArgs.putInt("editSoloIndex", soldierIndex);
        editArgs.putString("editSoloFirst", soldierFname);
        editArgs.putString("editSoloLast", soldierLname);
        adderFragment = new AdderFragment();
        adderFragment.setArguments(editArgs);

        return soldierFname; //this is simply a check to make sure we received
        // for the purpose of the try/catch block
    }
}
