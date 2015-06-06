package com.androidtitan.trooPTracker.Activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooPTracker.Adapter.ViewPagerAdapter;
import com.androidtitan.trooPTracker.Data.DatabaseHelper;
import com.androidtitan.trooPTracker.Dialog.DialogAdderFragment;
import com.androidtitan.trooPTracker.Fragment.NavigationDrawerFragment;
import com.androidtitan.trooPTracker.Fragment.SoldierListFragment;
import com.androidtitan.trooPTracker.Interface.MainInterface;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, MainInterface {

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private static MainActivity instance = null;
    public static int NUM_OF_LISTS = 1;

    DatabaseHelper databaseHelper;

    private ViewPagerAdapter viewPagerAdapter;
    public ViewPager viewPager;
    private SoldierListFragment soldierFrag;

    public int currentPage;

/*
        private List<Soldier> troops;
        private List<Division> battalion;
        private List<Soldier> divisionTroops;
*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //Database initialization and population follows
        databaseHelper = new DatabaseHelper(this);

/*
        troops = new ArrayList<Soldier>();
        divisionTroops = new ArrayList<Soldier>();
        battalion = new ArrayList<Division>();
*/
        soldierFrag = new SoldierListFragment();

        List<Fragment> fragments = getFragments();

/*
        //returning to the main activity after adding a new soldier

        try{
            Intent intent = getIntent();
            int divisionIndex = intent.getIntExtra("landingDivision", -1);

            soldierFrag = new SoldierListFragment();

            Bundle args = new Bundle();
            args.putInt("num", divisionIndex);
            soldierFrag.setArguments(args);

        } catch(NullPointerException e) {

        }

        //ToDo: Edit. On Soldier Addition it will automatically go to 1
        try {
            Intent intent = getIntent();
            int temp = intent.getIntExtra("viewpagerIndex", 0);
            tabInteraction(temp);
        } catch (NullPointerException e) {
            Log.e("MAonCreate", "catcher " + String.valueOf(e));
        }
*/
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


    //TODO: complete navigation drawer fragment
    @Override
    public void onNavigationDrawerItemSelected(int position) {
    }

    //adds all fragments to viewPager
    //@return: List of Fragments for listview
    private List<Fragment> getFragments() {
        List<Fragment> fragList = new ArrayList<Fragment>();
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

    //changes the tab that is "selected"
    public int setToolbarHighlight(int item) {

        TextView tab_one = (TextView) findViewById(R.id.tab_one);
        TextView tab_two = (TextView) findViewById(R.id.tab_two);
        TextView tab_three = (TextView) findViewById(R.id.tab_three);

        switch (item) {
            case 0:
                tab_one.setBackgroundColor(0xFF7db701);
                break;
            case 1:
                tab_two.setBackgroundColor(0xFF4285f4);
                break;
            case 2:
                tab_three.setBackgroundColor(0xFFee3124);
                break;
        }
        return item;
    }


}
