package com.androidtitan.hotspots.Dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.Data.LocationBundle;
import com.androidtitan.hotspots.Interface.MapsPullInterface;
import com.androidtitan.hotspots.R;
import com.google.android.gms.maps.model.LatLng;

public class MapsAdderDialogFragment extends DialogFragment {
    public static final String TAG = "MAPADDERDIALOG";

    DatabaseHelper databaseHelper;
    MapsPullInterface mapsPullInterface;

    private TextView mapAddCancel;
    private TextView mapAddSubmit;

    private int locationBundleIndex = -1;
    private double latitude = -1;
    private double longitude = -1;

    LocationBundle tempLocation;

    private String dialogString;

    public static MapsAdderDialogFragment newInstance() {
        //use this on rotate
        MapsAdderDialogFragment fragment = new MapsAdderDialogFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public MapsAdderDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        databaseHelper = DatabaseHelper.getInstance(getActivity());

        locationBundleIndex = getArguments().getInt("locationBundleIndex");
        latitude = getArguments().getDouble("locationBundleLat");
        longitude = getArguments().getDouble("locationBundleLng");
//todo
        tempLocation = databaseHelper.getAllLocations().get(locationBundleIndex);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps_adder_dialog, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mapAddCancel = (TextView) v.findViewById(R.id.mapCancel);
        mapAddSubmit = (TextView) v.findViewById(R.id.mapSubmit);


        mapAddCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        mapAddSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LocationBundle tempBundle = new LocationBundle(new LatLng(latitude, longitude));
                tempLocation.setLatlng(new LatLng(latitude, longitude));

                databaseHelper.updateLocationBundle(tempLocation);
                //TODO

                getDialog().dismiss();
                mapsPullInterface.onDialogCompletion(tempLocation);

            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mapsPullInterface = (MapsPullInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mapsPullInterface = null;
    }

}
