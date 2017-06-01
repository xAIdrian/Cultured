package com.androidtitan.culturedapp.main.trending.ui;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.structure.MvpActivity;
import com.androidtitan.culturedapp.main.trending.TrendingMvp;
import com.androidtitan.culturedapp.main.trending.TrendingPresenter;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;

import java.util.List;

public class TrendingActivity extends MvpActivity<TrendingPresenter, TrendingMvp.View> implements TrendingMvp.View, TrendingGraphFragment.TrendingFragmentInterface {
    private final String TAG = getClass().getSimpleName();

    TrendingPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trending_activity);

        presenter = new TrendingPresenter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trending...");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TrendingGraphFragment tgf = new TrendingGraphFragment();

        //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.add(R.id.fragment_container, tgf).addToBackStack(null).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public TrendingPresenter getTrendingPresenter() {
        return presenter;
    }

    @Override
    public TrendingPresenter getPresenter() {
        return presenter;
    }

    @Override
    public TrendingMvp.View getMvpView() {
        return this;
    }

    @Override
    public void initializeGeoFacetSpark(List<Facet> geoFacetList) {

    }

    @Override
    public void initializeOrgFacetSpark(List<Facet> orgFacetList) {

    }

    @Override
    public void initializeDesFacetSpark(List<Facet> desFacetList) {

    }

    @Override
    public void initializePerFacetSpark(List<Facet> perFacetList) {

    }

    @Override
    public void setLoading() {

    }

    @Override
    public void displayDataNotAvailable() {

    }

    @Override
    public void displayDataEmpty() {

    }
}
