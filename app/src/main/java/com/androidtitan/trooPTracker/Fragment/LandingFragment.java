package com.androidtitan.trooPTracker.Fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooPTracker.Adapter.LandingExpandableListAdapter;
import com.androidtitan.trooPTracker.Data.DatabaseHelper;
import com.androidtitan.trooPTracker.Interface.LandingInterface;


public class LandingFragment extends Fragment {

    DatabaseHelper databaseHelper;

    LandingInterface landingInterface;

    Toolbar toolbar;

    ImageView delete;
    ImageView edit;
    ImageView add;

    private ExpandableListView landingListView;
    private LandingExpandableListAdapter landingAdapter;

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
        landingAdapter = new LandingExpandableListAdapter(getActivity(), databaseHelper.getAllDivisions());


        Runnable run = new Runnable() {
            @Override
            public void run() {
                landingAdapter.notifyDataSetChanged();
                landingListView.invalidateViews();
            }
        };

        /*for(Division div : databaseHelper.getAllDivisions()) {
            Log.e("database LOG", div.getId() + ": " + div.getName());
        }*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_landing, container, false);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        delete = (ImageView) getActivity().findViewById(R.id.deleteBtn);
        edit = (ImageView) getActivity().findViewById(R.id.editBtn);
        add = (ImageView) getActivity().findViewById(R.id.addBtn);

        landingListView = (ExpandableListView) v.findViewById(R.id.divisionList);
        landingListView.setAdapter(landingAdapter);
        landingListView.invalidateViews();


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
                if(selection != -1) {
                    //this is a divison, we are editing, this is what we are editing.
                    landingInterface.divPasser(true, true, selection);
                }
                else {

                }

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selection != -1) {
                    //try to check for children...
                    try {
                        databaseHelper.deleteDivision(databaseHelper.getAllDivisions().get(selection), true);

                    } catch (NullPointerException e) {

                        databaseHelper.deleteDivision(databaseHelper.getAllDivisions().get(selection), false);

                    }

                    landingAdapter.removeGroup(selection);
                }
            }
        });

        landingListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousItem) {
                    landingListView.collapseGroup(previousItem);
                }
                previousItem = groupPosition;


            }
        });


        landingListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < landingAdapter.getGroupCount(); i++) {
                    View item = landingListView.getChildAt(i);
                    item.setBackgroundColor(0xFFFFFFFF);
                }
                if(selection == position) {
                    view.setBackgroundColor(0xFFFFFFFF);
                    selection = -1;
                }
                else {
                    view.setBackgroundColor(0xCC448AFF);
                    selection = position;
                }

                return true;
            }
        });


        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        landingInterface = null;
    }

}
