package com.androidtitan.trooptracker.Fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooptracker.Adapter.LandingAdapter;
import com.androidtitan.trooptracker.Adapter.LandingExpandableListAdapter;
import com.androidtitan.trooptracker.Data.DatabaseHelper;
import com.androidtitan.trooptracker.Data.Division;
import com.androidtitan.trooptracker.Interface.LandingInterface;

import java.util.List;


public class LandingFragment extends Fragment {

    DatabaseHelper databaseHelper;

    LandingInterface landingInterface;

    Toolbar toolbar;

    //private ImageView delete;
    private ImageView edit;
    private ImageView add;

    private ListView simpleListView;
    private LandingAdapter landingAdapter;
    private ExpandableListView expandableListView;
    private LandingExpandableListAdapter expandableAdapter;

    private int selection = -1;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            landingInterface = (LandingInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SecondF2AInterface");
        }
    }

    public static LandingFragment newInstance(String param1, String param2) {
        LandingFragment fragment = new LandingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LandingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = databaseHelper.getInstance(getActivity());

        /*Runnable run = new Runnable() {
            @Override
            public void run() {

                expandableAdapter.notifyDataSetChanged();
                landingAdapter.notifyDataSetChanged();

                expandableListView.invalidateViews();
                simpleListView.invalidateViews();

            }
        };*/

        /* this is to check our divisions that are in our database
        for(Division div : databaseHelper.getAllDivisions()) {
            Log.e("database LOG", div.getId() + ": " + div.getName());
        }*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_landing, container, false);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        //delete = (ImageView) getActivity().findViewById(R.id.deleteBtn);
        edit = (ImageView) getActivity().findViewById(R.id.editBtn);
        add = (ImageView) getActivity().findViewById(R.id.addBtn);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            //listview when in LANDSCAPE

            simpleListView = (ListView) v.findViewById(R.id.divisionList);
            final List<Division> allDivisions = databaseHelper.getAllDivisions();

            landingAdapter = new LandingAdapter(getActivity(), databaseHelper.getAllDivisions());
            simpleListView.setAdapter(landingAdapter);


            simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*
                listView.getChildAt(i) works where 0 is the very first visible row and (n-1)
                is the last visible row (where n is the number of visible views you see).
                 */
                    for (int i = 0; i <= simpleListView.getLastVisiblePosition() - simpleListView.getFirstVisiblePosition(); i++) {
                        View item = simpleListView.getChildAt(i);
                        item.setBackgroundColor(0xFFFFFFFF);
                    }
                    if (selection != position) {
                        view.setBackgroundColor(0xCC448AFF);
                        selection = position;

                    } else {
                        view.setBackgroundColor(0xFFFFFFFF);
                        selection = -1;
                    }
                }
            });


        }
        else {
            //expandable listview when in PORTRAIT
            expandableAdapter = new LandingExpandableListAdapter(getActivity(), databaseHelper.getAllDivisions());

            expandableListView = (ExpandableListView) v.findViewById(R.id.expDivisionList);
            expandableListView.setAdapter(expandableAdapter);
            expandableListView.invalidateViews();


            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                int previousItem = -1;

                @Override
                public void onGroupExpand(int groupPosition) {
                    if (groupPosition != previousItem) {
                        expandableListView.collapseGroup(previousItem);
                    }
                    previousItem = groupPosition;


                }
            });


            expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
                        View item = expandableListView.getChildAt(i);
                        item.setBackgroundColor(0xFFFFFFFF);
                    }
                    if (selection == position) {
                        view.setBackgroundColor(0xFFFFFFFF);
                        selection = -1;
                    } else {
                        view.setBackgroundColor(0xCC448AFF);
                        selection = position;
                    }

                    return true;
                }
            });
        }



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this is a division, a new division,  nothing to edit
                landingInterface.divPasser(true, false, -1);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selection != -1) {
                    //this is a divison, we are editing, this is what we are editing.
                    landingInterface.divPasser(true, true, selection);
                } else {

                }

            }
        });

        /*delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selection != -1) {
                    //delete

                    //update!
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                        landingAdapter.removeItem(selection);
                        simpleListView.invalidateViews();
                    }
                    else {
                        expandableAdapter.removeGroup(selection);
                        expandableListView.invalidateViews();
                    }

                    //removing risidual highlighting
                    if(expandableAdapter != null) {
                        for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
                            View item = expandableListView.getChildAt(i);
                            item.setBackgroundColor(0xFFFFFFFF);
                        }
                        selection = -1;
                    }
                    else {
                        for (int i = 0; i <= simpleListView.getLastVisiblePosition() - simpleListView.getFirstVisiblePosition(); i++) {
                            View item = simpleListView.getChildAt(i);
                            item.setBackgroundColor(0xFFFFFFFF);
                        }
                        selection = -1;
                    }
                }
            }
        });*/

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        landingInterface = null;
    }


}
