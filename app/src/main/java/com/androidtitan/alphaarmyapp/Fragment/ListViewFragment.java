package com.androidtitan.alphaarmyapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.Activity.SecondActivity;
import com.androidtitan.alphaarmyapp.Adapter.ListViewAdapter;
import com.androidtitan.alphaarmyapp.Data.DatabaseHelper;
import com.androidtitan.alphaarmyapp.Data.Division;
import com.androidtitan.alphaarmyapp.Data.Soldier;
import com.androidtitan.alphaarmyapp.Interface.F2AInterface;
import com.androidtitan.alphaarmyapp.R;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p>
 * Activities containing this fragment MUST implement the {@link F2AInterface}
 * interface.
 */
public class ListViewFragment extends ListFragment implements AdapterView.OnItemLongClickListener{
    //this is just a way to recognize this as a public variable that is being passed across
    //   fragments or to other controllers and views.
    //   This could just as easily be a lowercase int, locationXY, or poop.
    // //   Just so long as it is public and static
    public static final String TOSECONDACTIVITY = "Hello, SecondActivity";

    DatabaseHelper databaseHelper;
    Runnable run;

    private String[] drawerTitles;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;

    private F2AInterface toActivityInterface;
    private AbsListView listView;
    private ListViewAdapter adapter;

    private View toolbarContainer;
    private LinearLayout madderLayout;
    private ImageView drawerToggle;

    private TextView tab_one;
    private TextView tab_two;
    private TextView tab_three;

    private List<Soldier> troopsByDivision;
    private List<Division> allDivisions;

    int previousPosition = -1;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            toActivityInterface = (F2AInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public static ListViewFragment newInstance() {
        ListViewFragment fragment = new ListViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    //Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation changes).
    public ListViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());

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


        //Main Activity View Controls as follows...
        //Navigation Drawer
        drawerTitles = new String[]{"Map", "All Soldiers"};
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerListView = (ListView) getActivity().findViewById(R.id.right_drawer);

        drawerListView.setAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.drawer_item, drawerTitles));
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //keeps from swiping open
        //drawerListView.setOnClickListener(new DrawerItemClickListener());

        final View toolbar = getActivity().findViewById(R.id.toolbar);
        toolbarContainer = getActivity().findViewById(R.id.toolbar_container);
        madderLayout = (LinearLayout) getActivity().findViewById(R.id.adder_layout);
        drawerToggle = (ImageView) getActivity().findViewById(R.id.drawer_icon);

        tab_one = (TextView) getActivity().findViewById(R.id.tab_one);
        tab_two = (TextView) getActivity().findViewById(R.id.tab_two);
        tab_three = (TextView) getActivity().findViewById(R.id.tab_three);

        listView = (ListView) v.findViewById(android.R.id.list);
        adapter = new ListViewAdapter(getActivity(), getMyListItems());
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);

        madderLayout.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SecondActivity.class);
                intent.putExtra("pass2second", TOSECONDACTIVITY);
                startActivity(intent);
            }
        });
        drawerToggle.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerListView);
            }
        });
        tab_one.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivityInterface.tabInteraction(0);
                toolbarContainer.setBackgroundColor(0xFF33b5e5);
                tab_one.setBackgroundColor(0xFF33b5e5);
                tab_two.setBackgroundColor(0xFFee3124);
                tab_three.setBackgroundColor(0xFF0f9d58);
            }
        });
        tab_two.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivityInterface.tabInteraction(1);
                toolbarContainer.setBackgroundColor(0xFFf16b0c);
                tab_two.setBackgroundColor(0xFFf1b0c);
                tab_one.setBackgroundColor(0xFF4285f4);
                tab_three.setBackgroundColor(0xFF0f9d58);
            }
        });
        tab_three.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivityInterface.tabInteraction(2);
                toolbarContainer.setBackgroundColor(0xFF39bd00);
                tab_three.setBackgroundColor(0xFF39bd00);
                tab_one.setBackgroundColor(0xFF4285f4);
                tab_two.setBackgroundColor(0xFFee3124);
            }
        });

        drawerListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }

        });

        return v;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        toActivityInterface = null;
    }


    private List<Soldier> getMyListItems() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Integer receiver = bundle.getInt("num", 0);
            Log.e("getMyListItems", "valueOf(receiver) = " + String.valueOf(receiver));
                    Log.e("getMyListItems", "Made it here " + databaseHelper.getAllDivisions().isEmpty());
            for(Division div : databaseHelper.getAllDivisions()) {
                Log.e("getMyListItems", "Div " + div.getName());
            }
            troopsByDivision = databaseHelper.getAllSoldiersByDivision(databaseHelper.getAllDivisions().get(receiver));
        }
        else{
            troopsByDivision = databaseHelper.getAllSoldiersByDivision(databaseHelper.getAllDivisions().get(0));
        }
        return troopsByDivision;
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int currPosition, long id) {


        TextView removeOne = (TextView) view.findViewById(R.id.text_1);
        TextView removeTwo = (TextView) view.findViewById(R.id.text_2);

        removeOne.setVisibility(View.GONE);
        removeTwo.setVisibility(View.GONE);

        LinearLayout holderLayout = (LinearLayout) view.findViewById(R.id.listItem_linear);
        View child = view.inflate(getActivity(), R.layout.listview_long_item, null);
        View older = view.inflate(getActivity(), R.layout.listview_item, null);

        if(previousPosition != -1 && currPosition != previousPosition) {
            Log.e("LVFonItemLongClick", String.valueOf(previousPosition) + " -- " + String.valueOf(currPosition));

            removeOne.setVisibility(View.VISIBLE);
            removeTwo.setVisibility(View.VISIBLE);

            holderLayout.addView(older, previousPosition);

        }
        else{
            holderLayout.addView(child);
        }
        Log.e("LVFonItemLongClick", String.valueOf(previousPosition) + " -- " + String.valueOf(currPosition));
        previousPosition = currPosition;
        Log.e("LVFonItemLongClick", String.valueOf(previousPosition) + " -- " + String.valueOf(currPosition));

        TextView deleteText = (TextView) view.findViewById(R.id.delete_text);
        TextView editText = (TextView) view.findViewById(R.id.edit_text);
        TextView exitText = (TextView) view.findViewById(R.id.exit_text);

        return true;
    }
}
