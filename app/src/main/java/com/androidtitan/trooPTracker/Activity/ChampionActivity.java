package com.androidtitan.trooptracker.Activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooptracker.Fragment.ChampionListFragment;
import com.androidtitan.trooptracker.Interface.ChampionDataPullInterface;
import com.androidtitan.trooptracker.Interface.ChampionInterface;

public class ChampionActivity extends AppCompatActivity implements ChampionDataPullInterface,
        ChampionInterface {

    ChampionListFragment championFragment;

    int divisionIndex;
    int selectionIndex = - 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion);

        //Data Section

        Intent intent = getIntent();
        divisionIndex = intent.getIntExtra("landingDivision", -1);
        Bundle args = new Bundle();
        args.putInt("num", divisionIndex);

        //View Section
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        championFragment = new ChampionListFragment();
        championFragment.setArguments(args);


        FragmentTransaction fragTran = getFragmentManager().beginTransaction();
        fragTran.replace(R.id.championContainer, championFragment, "championFragment").commit();

    }


    @Override
    public void tabInteraction(int id) {

    }

    @Override
    public void soldierPasser(int soldierInt, int divisionInt, String first, String last) {

        Intent intent = new Intent(this, AdderActivity.class);
        intent.putExtra("editSoloIndex", soldierInt);
        intent.putExtra("editSoloDivIndex", divisionInt);
        intent.putExtra("editSoloFirst", first);
        intent.putExtra("editSoloLast", last);
        startActivity(intent);
    }

    //this is going to be used for navigation once we have more pieces
    //All soldiers and All map
    @Override
    public void drawerListViewSelection(int selection) {

    }

    @Override
    public void adderFragDivReference(int divIndex) {

        Intent intent = new Intent(this, AdderActivity.class);
        intent.putExtra("divIndex", divIndex);
        ChampionActivity.this.finish();
        startActivity(intent);

    }

    @Override
    public void setListViewSelection(int selection) {
        selectionIndex = selection;
    }

    @Override
    public void selectionToMap(int selection) {

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("selectionToMap", selection);
        startActivity(intent);
    }

    public int getListViewSelection() {
        return selectionIndex;
    }

    @Override
    public int getDivisionIndex() {
        return divisionIndex;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LandingActivity.class);
        this.finish();
        startActivity(intent);
    }


}
