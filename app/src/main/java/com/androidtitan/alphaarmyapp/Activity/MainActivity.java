package com.androidtitan.alphaarmyapp.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.androidtitan.alphaarmyapp.Adapter.ViewPagerAdapter;
import com.androidtitan.alphaarmyapp.Data.DatabaseHelper;
import com.androidtitan.alphaarmyapp.Data.Division;
import com.androidtitan.alphaarmyapp.Data.Soldier;
import com.androidtitan.alphaarmyapp.Fragment.ListViewFragment;
import com.androidtitan.alphaarmyapp.Interface.F2AInterface;
import com.androidtitan.alphaarmyapp.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements F2AInterface {
    private static MainActivity instance = null;
    public static int NUM_OF_LISTS = 3;

    DatabaseHelper databaseHelper;

    // When requested, this adapter returns a ListViewFragment,
    // representing an object in the collection.
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;

    private ListViewFragment listFrag1;
    private ListViewFragment listFrag2;
    private ListViewFragment listFrag3;

    private ActionBar actionBar;

    private List<Soldier> troops;
    private List<Division> battalion;

    private List<Soldier> divisionTroops;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Database initialization and population follows
        databaseHelper = new DatabaseHelper(this);

        troops = new ArrayList<Soldier>();
        divisionTroops = new ArrayList<Soldier>();
        battalion = new ArrayList<Division>();

        Division div1 = new Division("Academy", "Honogi, Japan");
        Division div2 = new Division("Hawk", "North Carolina, USA");
        Division div3 = new Division("Boar", "Moscow, Russia");

        databaseHelper.createDivision(div1);
        databaseHelper.createDivision(div2);
        databaseHelper.createDivision(div3);
        //View Pager Follows .....
        //WE MIGHT REMOVE THIS AND MAKE IT DYNAMIC LATER
        listFrag1 = new ListViewFragment();
        listFrag2 = new ListViewFragment();
        listFrag3 = new ListViewFragment();
        List<Fragment> fragments = getFragments();

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(viewPagerAdapter);


    }

    private List<Fragment> getFragments() {
        List<Fragment> fragList = new ArrayList<Fragment>();
        fragList.add(listFrag1);
        fragList.add(listFrag2);
        fragList.add(listFrag3);
        return fragList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void tabInteraction(int id) {
        //We need to pass arguments here too for our adapter.
        ListViewFragment fragment = new ListViewFragment();
        //this.myFrags.get(pos)
        Bundle args = new Bundle();
        args.putInt("num", id);
        fragment.setArguments(args);

        viewPager.setCurrentItem(id, true);
    }

}
