package com.androidtitan.trooPTracker.Fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooPTracker.Adapter.LandingExpandableListAdapter;
import com.androidtitan.trooPTracker.Data.DatabaseHelper;
import com.androidtitan.trooPTracker.Interface.LandingInterface;


public class LandingFragment extends Fragment {

    Runnable runnable;
    DatabaseHelper databaseHelper;

    LandingInterface landingInterface;

    private ExpandableListView landingListView;
    private ExpandableListAdapter landingAdapter;


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
        if (getArguments() != null) {
        }

        databaseHelper = databaseHelper.getInstance(getActivity());

        //ToDo: notifyDataSetChanged;
        Runnable run = new Runnable() {
            @Override
            public void run() {
                //landingAdapter.notifDataSetChanged();
                landingListView.invalidateViews();
            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_landing, container, false);

        landingListView = (ExpandableListView) v.findViewById(R.id.divisionList);
        landingAdapter = new LandingExpandableListAdapter(getActivity(), databaseHelper.getAllDivisions());
        landingListView.setAdapter(landingAdapter);
        landingListView.invalidateViews();

        landingListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousItem)
                    landingListView.collapseGroup(previousItem);
                previousItem = groupPosition;
            }
        });

        landingListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (childPosition == 0) {
                    /**Create another (method) within your Interface, pass the group position values to interface then to activity
                     *from activity in (method) open add frag and populate EditText with passsed values
                     *
                     *activityInterface.passCrewMember(firstName, lastName, duty);
                     **/
                    //activityCommunicator.passMemberToActivity(activityPos,
                    //      dutyCrew.get(groupPosition).getFname(), dutyCrew.get(groupPosition).getLname(), groupPosition);
                }
                if (childPosition == 1) {
                    //this is delete
                    //dutyCrew.remove(groupPosition);
                    //activityCommunicator.passDataToActivity(activityPos);
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
