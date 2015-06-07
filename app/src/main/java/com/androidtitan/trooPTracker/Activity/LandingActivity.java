package com.androidtitan.trooPTracker.Activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooPTracker.Fragment.LandingFragment;
import com.androidtitan.trooPTracker.Interface.LandingInterface;

public class LandingActivity extends FragmentActivity implements LandingInterface{

    FragmentTransaction fragTran = getFragmentManager().beginTransaction();
    LandingFragment landingFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        landingFrag = new LandingFragment();
        fragTran.addToBackStack(null).add(R.id.landingContainer, landingFrag, "landingFrag")
                .commit();

    }

    //Launches our main activity and tells which soldier item to display.
    @Override
    public void soldierListOpener(int groupPosition) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("landingDivision", groupPosition);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //effectively disabling the back button
    }

}
