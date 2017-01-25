package com.androidtitan.culturedapp.main.trending.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.structure.BaseActivity;
import com.androidtitan.culturedapp.main.trending.TrendingMvp;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TrendingActivity extends BaseActivity implements TrendingMvp.View {
    private final String TAG = getClass().getSimpleName();

    @Bind(R.id.trendingFacetViewPager)
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trending_activity);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trending...");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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


}
