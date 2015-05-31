package com.androidtitan.alphaarmyapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
    public ViewPager viewPager;

    private ListViewFragment listFrag1;
    private ListViewFragment listFrag2;
    private ListViewFragment listFrag3;

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

        //View Pager Follows .....
        //WE MIGHT REMOVE THIS AND MAKE IT DYNAMIC LATER
        listFrag1 = new ListViewFragment();
        listFrag2 = new ListViewFragment();
        listFrag3 = new ListViewFragment();
        List<Fragment> fragments = getFragments();

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(viewPagerAdapter);

        if(getResources().getConfiguration().orientation != getResources().getConfiguration().ORIENTATION_LANDSCAPE) {

            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    setToolbarHighlight(position);
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            try {
                Intent intent = getIntent();
                int temp = intent.getIntExtra("viewpagerIndex", 0);
                tabInteraction(temp);
            } catch (NullPointerException e) {
                Log.e("MAonCreate", "catcher " + String.valueOf(e));
            }
        }
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragList = new ArrayList<Fragment>();
        fragList.add(listFrag1);
        fragList.add(listFrag2);
        fragList.add(listFrag3);
        return fragList;
    }

    @Override
    public void tabInteraction(int id) {

        ListViewFragment fragment = new ListViewFragment();
        Bundle args = new Bundle();
        args.putInt("num", id);
        fragment.setArguments(args);

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

    //changes the tab that is "selected"
    public int setToolbarHighlight(int item) {

        View toolbarContainer = findViewById(R.id.toolbar_container);
        TextView tab_one = (TextView) findViewById(R.id.tab_one);
        TextView tab_two = (TextView) findViewById(R.id.tab_two);
        TextView tab_three = (TextView) findViewById(R.id.tab_three);

        switch (item) {
            case 0:
                toolbarContainer.setBackgroundColor(0xFF33b5e5);
                tab_one.setBackgroundColor(0xFF33b5e5);
                tab_two.setBackgroundColor(0xFFee7125);
                tab_three.setBackgroundColor(0xFF0f9d58);

                break;

            case 1:
                toolbarContainer.setBackgroundColor(0xFFf16b0c);
                tab_two.setBackgroundColor(0xFFf1b0c);
                tab_one.setBackgroundColor(0xFF4285f4);
                tab_three.setBackgroundColor(0xFF0f9d58);

                break;

            case 2:
                toolbarContainer.setBackgroundColor(0xFF39bd00);
                tab_three.setBackgroundColor(0xFF39bd00);
                tab_one.setBackgroundColor(0xFF4285f4);
                tab_two.setBackgroundColor(0xFFee7125);

                break;

        }
        return item;
    }
}
