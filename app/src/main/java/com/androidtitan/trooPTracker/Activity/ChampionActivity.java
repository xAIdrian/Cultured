package com.androidtitan.trooPTracker.Activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooPTracker.Fragment.ChampionListFragment;
import com.androidtitan.trooPTracker.Interface.ChampionInterface;
import com.androidtitan.trooPTracker.Interface.ChampionDataPullInterface;

public class ChampionActivity extends AppCompatActivity implements ChampionDataPullInterface, ChampionInterface{

    ChampionListFragment championFragment;
    //ChampionDataPullInterface push4frag2pull;

    Boolean isDualPane;

    int divisionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion);

        //Data Section

        Intent intent = getIntent();
        divisionIndex = intent.getIntExtra("landingDivision", -1);
        Log.e("ChampAonCreate", "divisionIndex: " + divisionIndex);
        Bundle args = new Bundle();
        args.putInt("num", divisionIndex);

        //View Section
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        championFragment = new ChampionListFragment();
        championFragment.setArguments(args);


        FragmentTransaction fragTran = getFragmentManager().beginTransaction();
        fragTran.add(R.id.championContainer, championFragment, "championFragment").commit();

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

    @Override
    public void refreshViewPager(int index) {

    }

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
