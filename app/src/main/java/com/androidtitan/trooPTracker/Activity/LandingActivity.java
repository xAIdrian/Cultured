package com.androidtitan.trooPTracker.Activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooPTracker.Fragment.LandingFragment;
import com.androidtitan.trooPTracker.Interface.LandingInterface;

public class LandingActivity extends FragmentActivity implements LandingInterface{

    Toolbar toolbar;

    FragmentTransaction fragTran = getFragmentManager().beginTransaction();
    LandingFragment landingFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

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

    @Override
    public void onBackPressed() {
        //effectively disabling the back button
    }

}
