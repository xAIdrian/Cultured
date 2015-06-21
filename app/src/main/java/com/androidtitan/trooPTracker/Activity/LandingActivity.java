package com.androidtitan.trooPTracker.Activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooPTracker.Data.DatabaseHelper;
import com.androidtitan.trooPTracker.Data.Division;
import com.androidtitan.trooPTracker.Fragment.LandingFragment;
import com.androidtitan.trooPTracker.Interface.LandingInterface;

public class LandingActivity extends FragmentActivity implements LandingInterface{

    DatabaseHelper databaseHelper;

    Toolbar toolbar;

    FragmentTransaction fragTran = getFragmentManager().beginTransaction();
    LandingFragment landingFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        databaseHelper = DatabaseHelper.getInstance(this);
        //we are trying to hide our actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //we will only create this fragment if activity is rebuilt
        if(savedInstanceState == null) {
            landingFrag = new LandingFragment();
            fragTran.addToBackStack(null).add(R.id.landingContainer, landingFrag, "landingFrag")
                    .commit();
        }

    }

    //Launches our main activity and tells which soldier item to display.
    @Override
    public void soldierListOpener(int groupPosition) {

        Intent intent = new Intent(this, ChampionActivity.class);
        intent.putExtra("landingDivision", groupPosition);
        startActivity(intent);
    }

    @Override
    public void divPasser(boolean isDiv, boolean isEdit, int divPosition) {
        Intent intent = new Intent(this, AdderActivity.class);
        intent.putExtra("landingBool", isDiv);
        intent.putExtra("landingEdit", isEdit);
        intent.putExtra("landingDivision", divPosition);
        startActivity(intent);
    }

    //Get # of Visits Increment and set # of Visits
    public void upTick(Division division) {
        int visitTick = division.getVisits();
        visitTick++;
        division.setVisits(visitTick);
        databaseHelper.updateDivision(division);
    }

    @Override
    public void onBackPressed() {
        //effectively disabling the back button
    }

}
