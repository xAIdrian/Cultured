package com.androidtitan.alphaarmyapp.Adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.androidtitan.alphaarmyapp.Data.DatabaseHelper;
import com.androidtitan.alphaarmyapp.Fragment.ListViewFragment;

import java.util.List;

/**
 * Created by A. Mohnacs on 5/16/2015.
 *
 * Since this is an object collection, use a FragmentStatePagerAdapter,
 * and NOT a FragmentPagerAdapter.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {


    //http://www.androidhive.info/2013/10/android-tab-layout-with-swipeable-views-1/

    private Activity context;
    private List<Fragment> myFrags;
    DatabaseHelper databaseHelper;
    public ViewPagerAdapter(FragmentManager fm, List<Fragment> littleViews) {
        super(fm);
        this.myFrags = littleViews;
    }

    @Override
    public Fragment getItem(int pos) {
        ListViewFragment fragment = new ListViewFragment();
        //this.myFrags.get(pos)
        Bundle args = new Bundle();
        args.putInt("num", pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return myFrags.size();
    }

}
