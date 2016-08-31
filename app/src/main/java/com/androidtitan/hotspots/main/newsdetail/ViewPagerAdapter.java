package com.androidtitan.hotspots.main.newsdetail;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.androidtitan.hotspots.main.newsdetail.fragments.WikiFragment;

import java.util.ArrayList;

/**
 * Created by amohnacs on 4/2/16.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;

    public static int pos = 0;
    private ArrayList<WikiFragment> fragments = new ArrayList<>();
    private ArrayList<String> titleStrings = new ArrayList<String>();
    private int numOfTitles;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    public ViewPagerAdapter(FragmentManager fm, ArrayList<String> titles, int numOfStrings) {
        super(fm);

        this.titleStrings = titles;
        this.numOfTitles = numOfStrings;
    }

    public ViewPagerAdapter(Context c, FragmentManager fm, ArrayList<WikiFragment> frags, ArrayList<String> titles) {
        super(fm);

        this.context = c;
        this.fragments = frags;
        this.titleStrings = titles;
        this.numOfTitles = titles.size();
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return numOfTitles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleStrings.get(position);
    }

    public void addFragment(WikiFragment fragment, String title) {
        fragments.add(fragment);
        titleStrings.add(title);
    }

    public static void setPos(int pos) {
        ViewPagerAdapter.pos = pos;
    }
}
