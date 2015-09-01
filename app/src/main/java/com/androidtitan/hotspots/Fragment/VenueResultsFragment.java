package com.androidtitan.hotspots.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidtitan.hotspots.R;


public class VenueResultsFragment extends Fragment {
    private static final String TAG = "VenueResultsFragment";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        new FoursquareVenueHandler(MapsActivity.this, focusLocation.getId(), focusLocation.getVenueId());
         */


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_venue_results, container, false);


        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

}
