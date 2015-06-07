package com.androidtitan.trooPTracker.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooPTracker.Activity.MainActivity;
import com.androidtitan.trooPTracker.Adapter.ListViewAdapter;
import com.androidtitan.trooPTracker.Data.DatabaseHelper;
import com.androidtitan.trooPTracker.Data.Division;
import com.androidtitan.trooPTracker.Data.Soldier;
import com.androidtitan.trooPTracker.Interface.MainDataPullInterface;
import com.androidtitan.trooPTracker.Interface.MainInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p>
 * Activities containing this fragment MUST implement the {@link MainInterface}
 * interface.
 */
public class SoldierListFragment extends ListFragment {
    //this is just a way to recognize this as a public variable that is being passed across
    //   fragments or to other controllers and views.
    //   This could just as easily be a lowercase int, locationXY, or poop.
    // //   Just so long as it is public and static
    public static final String TOSECONDACTIVITY = "Hello, SecondActivity";
    private static final String ORIENT = "orientInt";

    DatabaseHelper databaseHelper;
    MainActivity mainActivity;

    MainInterface mainInterface;
    MainDataPullInterface pullInterface;

    private AbsListView listView;
    private ListViewAdapter adapter;

    private TextView tab_one;
    private TextView tab_two;
    private TextView tab_three;

    ImageView addBtn;
    ImageView editBtn;

    private int soldierSelected;
    private int receivedIndex;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mainInterface = (MainInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MainInterface");
        }

        try {
            pullInterface = (MainDataPullInterface) activity;
        } catch(ClassCastException e) {

            throw new ClassCastException(activity.toString()
                    + " must implement MainDataPullInterface");
        }

        receivedIndex = pullInterface.getDivisionIndex();
        Log.e("SLFonAttach", "receivedIndex: " + receivedIndex);
    }

    public static SoldierListFragment newInstance() {
        SoldierListFragment fragment = new SoldierListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    //Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation changes).
    public SoldierListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
        mainActivity = (MainActivity) getActivity();
/*
        //setRetainInstance(true);
        if(savedInstanceState != null){
            int temp = savedInstanceState.getInt(ORIENT);
            Log.e("LVFonCreateView", String.valueOf(temp));
            mainActivity.setToolbarHighlight(temp);
        }*/

        Runnable run = new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                listView.invalidateViews();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item, container, false);

        tab_one = (TextView) getActivity().findViewById(R.id.tab_one);
        tab_two = (TextView) getActivity().findViewById(R.id.tab_two);
        tab_three = (TextView) getActivity().findViewById(R.id.tab_three);

        listView = (ListView) v.findViewById(android.R.id.list);

        adapter = new ListViewAdapter(getActivity(), initialGetMyListItems());

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        //listView.setOnItemLongClickListener(this);

        tab_one.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainInterface.tabInteraction(0);
                mainActivity.setToolbarHighlight(0);
            }
        });
        tab_two.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainInterface.tabInteraction(1);
                mainActivity.setToolbarHighlight(1);
            }
        });
        tab_three.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainInterface.tabInteraction(2);
                mainActivity.setToolbarHighlight(2);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                soldierSelected = position;

                for (int i = 0; i < adapter.getCount(); i++) {
                    View item = listView.getChildAt(i);
                    item.setBackgroundColor(0xFFFFFFFF);
                }
                Toast.makeText(getActivity(), "Pop! " + position, Toast.LENGTH_SHORT);

                view.setBackgroundColor(0xCC448AFF);

            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Division tempDiv = databaseHelper.getAllDivisions().get(receivedIndex);
                Soldier tempSolo = databaseHelper.getAllSoldiersByDivision(tempDiv).get(position);

                databaseHelper.deleteSoldier(tempSolo);

                adapter.remove(position);
                adapter.notifyDataSetChanged();

                mainActivity.refreshViewPager(0);

                return true;
            }
        });

        addBtn = (ImageView) v.findViewById(R.id.toolbar_addBtn);
        editBtn = (ImageView) v.findViewById(R.id.toolbar_editBtn);

        addBtn.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainInterface.adderFragDivReference(receivedIndex);
            }
        });

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainInterface = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        //outState.putInt(ORIENT, tabTracker);
    }

/*    //Receiving viewpager index from MainActivity. Determines which division to display.
    private List<Soldier> getMyListItems() {
        Bundle bundle = new Bundle();

        if(bundle != null){
             bundle = this.getArguments();
            Integer receiver = bundle.getInt("num");

            Log.e("SLFgetItems", "receivedIndex: " + String.valueOf(receiver));
            troopsByDivision = databaseHelper.getAllSoldiersByDivision(databaseHelper.getAllDivisions().get(receiver));
        }
        else {
            troopsByDivision = databaseHelper.getAllSoldiersByDivision(databaseHelper.getAllDivisions().get(0));
        }
        return troopsByDivision;
    }*/

    private List<Soldier> initialGetMyListItems() {
        ArrayList<Soldier> soldierItems = new ArrayList<Soldier>();

        if(receivedIndex == -1) {
            soldierItems.addAll(databaseHelper.getAllSoldiers());

        } else {
            soldierItems.addAll(databaseHelper.getAllSoldiersByDivision(databaseHelper.getAllDivisions().get(receivedIndex)));
        }


        return soldierItems;
    }


}
