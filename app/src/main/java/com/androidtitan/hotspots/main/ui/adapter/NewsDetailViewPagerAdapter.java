package com.androidtitan.hotspots.main.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.androidtitan.hotspots.main.ui.fragments.WikiWebViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amohnacs on 4/1/16.
 */
public class NewsDetailViewPagerAdapter extends FragmentStatePagerAdapter {

    public static int pos = 0;

    List<String> tabTitleArray;
    int tabNumber;

    List<Fragment> fragments = new ArrayList<>();

// public NewsDetailViewPagerAdapter(FragmentManager fm, CharSequence sequence, int numTags) {
    public NewsDetailViewPagerAdapter(FragmentManager fm, List<String> titles, int numOfTabs) {
        super(fm);

        this.tabTitleArray = titles;
        this.tabNumber = numOfTabs;

    }

    @Override
    public Fragment getItem(int position) {
        WikiWebViewFragment fragment;

        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitleArray.get(position);
    }

    @Override
    public int getCount() {
        return tabNumber;
    }

    public void add(Fragment fragment, String title) {
        fragments.add(fragment);
        tabTitleArray.add(title);
    }

    public static int getPos(int pos) {
        return pos;
    }

    public static void setPos(int pos) {
        NewsDetailViewPagerAdapter.pos = pos;
    }
}
