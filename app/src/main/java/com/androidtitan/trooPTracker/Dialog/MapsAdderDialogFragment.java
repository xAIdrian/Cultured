package com.androidtitan.trooptracker.Dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooptracker.Data.DatabaseHelper;

public class MapsAdderDialogFragment extends DialogFragment {

    DatabaseHelper databaseHelper;

    private EditText mapAddEditText;
    private TextView mapAddCancel;
    private TextView mapAddSubmit;

    private int locationBundleIndex = -1;

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
        Log.e("MADFonCreate", "location Index: " + locationBundleIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps_adder_dialog, container, false);

        mapAddEditText = (EditText) v.findViewById(R.id.mapEditText);
        mapAddCancel = (TextView) v.findViewById(R.id.mapCancel);
        mapAddSubmit = (TextView) v.findViewById(R.id.mapSubmit);

        //todo: set EDIT text listener

        //todo: on ADD use 'databaseHelper.getLocation(locationBundleIndex).setTitle("string")'

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

}
