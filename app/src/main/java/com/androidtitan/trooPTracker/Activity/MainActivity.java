package com.androidtitan.trooPTracker.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooPTracker.Adapter.ViewPagerAdapter;
import com.androidtitan.trooPTracker.Data.DatabaseHelper;
import com.androidtitan.trooPTracker.Dialog.DialogAdderFragment;
import com.androidtitan.trooPTracker.Fragment.AdderFragment;
import com.androidtitan.trooPTracker.Fragment.NavigationDrawerFragment;
import com.androidtitan.trooPTracker.Fragment.SoldierListFragment;
import com.androidtitan.trooPTracker.Interface.MainDataPullInterface;
import com.androidtitan.trooPTracker.Interface.MainInterface;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements MainInterface, MainDataPullInterface {

    private static MainActivity instance = null;
    public static int NUM_OF_LISTS = 1;

    DatabaseHelper databaseHelper;
    List<Fragment> fragList = new ArrayList<Fragment>();

    private ViewPagerAdapter viewPagerAdapter;
    public ViewPager viewPager;
    private NavigationDrawerFragment navigationDrawerFragment;

    FragmentTransaction fragTrans = getFragmentManager().beginTransaction();
    private AdderFragment adderFragment;
    private SoldierListFragment soldierFrag;

    int divisionIndex;
    public int currentPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        divisionIndex = intent.getIntExtra("landingDivision", -1);
        Log.e("MAonCreate", "divisionIndex: " + divisionIndex);

        navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.drawer_container);

        // Set up the drawer.
        navigationDrawerFragment.setUp(
                R.id.drawer_container,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        databaseHelper = new DatabaseHelper(this);

        List<Fragment> fragments = getFragments();


        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setToolbarHighlight(position);
                currentPage = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    //adds all fragments to viewPager
    //@return: List of Fragments for listview
    private List<Fragment> getFragments() {

        fragList.add(soldierFrag);
        return fragList;
    }

    //returns the index of the viewPager
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void tabInteraction(int id) {

        SoldierListFragment fragment = new SoldierListFragment();
        Bundle args = new Bundle();
        args.putInt("num", id);
        fragment.setArguments(args);

        currentPage = id;
        viewPager.setCurrentItem(id, true);

        switch (id) {

            case 0:
                setToolbarHighlight(0);
                break;

            case 1:
                setToolbarHighlight(1);
                break;

            case 2:
                setToolbarHighlight(2);
                break;
        }
    }

    @Override
    public void dialogPasser(int soldierInt, int divisionInt, String first, String last, String spec) {

        DialogAdderFragment dialogAdder = new DialogAdderFragment();

        Bundle args = new Bundle();
        args.putInt("dialogInt", soldierInt);
        args.putInt("dialogDiv", divisionInt);
        args.putString("dialogFirst", first);
        args.putString("dialogLast", last);
        args.putString("dialogSpec", spec);

        dialogAdder.setArguments(args);

        FragmentManager fm = getFragmentManager();
        dialogAdder.show(fm, "dialog");
    }

    @Override
    public void refreshViewPager(int index) {

        List<Fragment> fragments = getFragments();
        currentPage = index;
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(index, true);

    }

    @Override
    public void drawerSelection(int selection) {

    }


        /*fragTrans.addToBackStack(null).replace(R.id.drawer_container, adderFragment).commit();
        // Set up the drawer.
        navigationDrawerFragment.setUp(
                R.id.drawer_container,
                (DrawerLayout) findViewById(R.id.drawer_layout));*/


    //changes the tab that is "selected"
    public int setToolbarHighlight(int item) {

        TextView tab_one = (TextView) findViewById(R.id.tab_one);
        TextView tab_two = (TextView) findViewById(R.id.tab_two);
        TextView tab_three = (TextView) findViewById(R.id.tab_three);

        switch (item) {
            case 0:
                tab_one.setBackgroundColor(0xFF4CAF50);
                break;
            case 1:
                tab_two.setBackgroundColor(0xFF448AFF);
                break;
            case 2:
                tab_three.setBackgroundColor(0xFF727272);
                break;
        }
        return item;
    }

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
