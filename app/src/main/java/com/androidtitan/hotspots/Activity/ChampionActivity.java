package com.androidtitan.hotspots.Activity;

import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.Fragment.ChampionListFragment;
import com.androidtitan.hotspots.Interface.ChampionDataPullInterface;
import com.androidtitan.hotspots.Interface.ChampionInterface;

public class ChampionActivity extends AppCompatActivity implements ChampionDataPullInterface,
        ChampionInterface {

    ChampionListFragment championFragment;

    int selectionIndex = - 1;

    boolean shouldCursorUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion);

        //Data Section
/*
    This code is coming from AdderActivity

        Intent intent = getIntent();
        shouldCursorUpdate = intent.getBooleanExtra(AdderActivity.CURSOR_UPDATE, false);
        Bundle args = new Bundle();
        args.putBoolean(AdderActivity.CURSOR_UPDATE, shouldCursorUpdate);*/

        //View Section
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        championFragment = new ChampionListFragment();
        //championFragment.setArguments(args);


        FragmentTransaction fragTran = getSupportFragmentManager().beginTransaction();
        fragTran.replace(R.id.championContainer, championFragment, "championFragment").commit();

    }

    @Override
    public void tabInteraction(int id) {

    }

    @Override
    public void soldierPasser(int soldierInt, String first, String last) {

        Intent intent = new Intent(this, AdderActivity.class);
        intent.putExtra("editSoloIndex", soldierInt);
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
    public void setListViewSelection(int selection) {
        selectionIndex = selection;
    }

    @Override
    public void selectionToMap(int selection) {

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("selectionToMap", selection);
        //intent.putExtra("selectionToMapDiv", divisionIndex);
        startActivity(intent);
    }

    public int getListViewSelection() {
        return selectionIndex;
    }

    //todo: this needs to be deleted at some point
    @Override
    public int getDivisionIndex() {
        return -1;
    }


    @Override
    public void onBackPressed() {
        //disable the back button
    }


}
