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

import com.androidtitan.alphaarmyapp.Activity.MainActivity;
import com.androidtitan.alphaarmyapp.Activity.SecondActivity;
import com.androidtitan.alphaarmyapp.Adapter.ListViewAdapter;
import com.androidtitan.alphaarmyapp.Data.DatabaseHelper;
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
    private static final String ORIENT = "orientInt";

    DatabaseHelper databaseHelper;
    MainActivity mainActivity;

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

    private int highlightInt;
    private Boolean itemsOpen = false;

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
                    + " must implement SecondF2AInterface");
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
        mainActivity = (MainActivity) getActivity();
/*
        //setRetainInstance(true);
        if(savedInstanceState != null){
            int temp = savedInstanceState.getInt(ORIENT);
            Log.e("LVFonCreateView", String.valueOf(temp));
            mainActivity.setToolbarHighlight(temp);
        }*/

        Bundle bundle = new Bundle();
        bundle = this.getArguments();
        highlightInt = bundle.getInt("num");
        Log.e("LVFonCreate", String.valueOf(highlightInt));

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
        drawerTitles = new String[]{"View Map", "Add Division"};
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerListView = (ListView) getActivity().findViewById(R.id.right_drawer);

        drawerListView.setAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.drawer_item, drawerTitles));
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //keeps from swiping open
        //drawerListView.setOnClickListener(new DrawerItemClickListener());

        toolbarContainer = getActivity().findViewById(R.id.toolbar_container);
        madderLayout = (LinearLayout) getActivity().findViewById(R.id.adder_layout);
        drawerToggle = (ImageView) getActivity().findViewById(R.id.drawer_icon);

        tab_one = (TextView) getActivity().findViewById(R.id.tab_one);
        tab_two = (TextView) getActivity().findViewById(R.id.tab_two);
        tab_three = (TextView) getActivity().findViewById(R.id.tab_three);

        listView = (ListView) v.findViewById(android.R.id.list);

        //setting listview items depending on the orientation.
        if(getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
            //In landscape we will display all of the soldiers
            adapter = new ListViewAdapter(getActivity(), databaseHelper.getAllSoldiers());

            adapter.notifyDataSetChanged();
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
            drawerListView.setOnItemClickListener(new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }

            });
        }
        else {
            //portrait. Soldiers by Division
            adapter = new ListViewAdapter(getActivity(), getMyListItems());

            adapter.notifyDataSetChanged();
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

            drawerListView.setOnItemClickListener(new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }

            });
            tab_one.setOnClickListener(new TextView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toActivityInterface.tabInteraction(0);
                    mainActivity.setToolbarHighlight(0);
                }
            });
            tab_two.setOnClickListener(new TextView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toActivityInterface.tabInteraction(1);
                    mainActivity.setToolbarHighlight(1);
                }
            });
            tab_three.setOnClickListener(new TextView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toActivityInterface.tabInteraction(2);
                    mainActivity.setToolbarHighlight(2);
                }
            });

        }
        return v;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        toActivityInterface = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        //outState.putInt(ORIENT, tabTracker);
    }

    //Receiving viewpager index from MainActivity. Determines which division to display.
    private List<Soldier> getMyListItems() {
        Bundle bundle = new Bundle();

        if (bundle != null) {
            bundle = this.getArguments();
            Integer receiver = bundle.getInt("num");
            troopsByDivision = databaseHelper.getAllSoldiersByDivision(databaseHelper.getAllDivisions().get(receiver));

        } else {
            troopsByDivision = databaseHelper.getAllSoldiersByDivision(databaseHelper.getAllDivisions().get(0));
        }
        return troopsByDivision;
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        if(itemsOpen == false) {
            itemsOpen = true;

            TextView removeOne = (TextView) view.findViewById(R.id.text_1);
            TextView removeTwo = (TextView) view.findViewById(R.id.text_2);

            removeOne.setVisibility(View.GONE);
            removeTwo.setVisibility(View.GONE);

            final LinearLayout holderLayout = (LinearLayout) view.findViewById(R.id.listItem_linear);
            final View child = view.inflate(getActivity(), R.layout.listview_long_item, null);
            holderLayout.addView(child);

            //wait for the view to be added
            new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            //final: the variable will always contain the same value but the context may change.
            final TextView deleteText = (TextView) child.findViewById(R.id.delete_text);
            final TextView editText = (TextView) child.findViewById(R.id.edit_text);
            final TextView closeText = (TextView) child.findViewById(R.id.close_text);

            final View older = view.inflate(getActivity(), R.layout.listview_item, null);

            closeText.setOnClickListener(new TextView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //resume clickable actions. Return view at position.
                    itemsOpen = false;

                    deleteText.setVisibility(View.GONE);
                    editText.setVisibility(View.GONE);
                    closeText.setVisibility(View.GONE);

                    holderLayout.removeAllViews();
                    holderLayout.setPadding(0, 0, 0, 0);
                    holderLayout.addView(adapter.getView(position, older, null));


                    //holderLayout.addView(adapter.getView(listView.getCheckedItemPosition(), getView(), null));
                }
            });

            deleteText.setOnClickListener(new TextView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("LVFonLongClick", "Deleted! " + troopsByDivision.get(position).getfName());
                    databaseHelper.deleteSoldier(position);
                    adapter.remove(position);
                    adapter.notifyDataSetChanged();

                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    itemsOpen = false;

                    deleteText.setVisibility(View.GONE);
                    editText.setVisibility(View.GONE);
                    closeText.setVisibility(View.GONE);

                    //if it is our last view we are just going to remove it...
                    try{
                        holderLayout.removeAllViews();
                        holderLayout.setPadding(0, 0, 0, 0);
                        holderLayout.addView(adapter.getView(position, older, null));
                    } catch (IndexOutOfBoundsException e) {
                        holderLayout.removeAllViews();
                    }

                }
            });
        }

        else {
            //hacky way to disable onItemLongClickListener()
        }

        return true;
    }

}
