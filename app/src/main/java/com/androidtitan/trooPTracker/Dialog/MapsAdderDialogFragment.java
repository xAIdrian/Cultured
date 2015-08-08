package com.androidtitan.trooptracker.Dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.androidtitan.trooptracker.Data.DatabaseHelper;
import com.androidtitan.trooptracker.Data.LocationBundle;
import com.androidtitan.trooptracker.Data.Soldier;
import com.androidtitan.trooptracker.Interface.MapsPullInterface;
import com.androidtitan.trooptracker.R;
import com.google.android.gms.maps.model.LatLng;

public class MapsAdderDialogFragment extends DialogFragment {
    public static final String TAG = "MAPADDERDIALOG";

    DatabaseHelper databaseHelper;
    MapsPullInterface mapsPullInterface;

    private EditText mapAddEditText;
    private TextView mapAddCancel;
    private TextView mapAddSubmit;

    private int soldierIndex = -1;
    private int locationBundleIndex = -1;
    private double latitude = -1;
    private double longitude = -1;

    Soldier tempSoldier;

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

        soldierIndex = getArguments().getInt("soldierIndex");
        locationBundleIndex = getArguments().getInt("locationBundleIndex");
        latitude = getArguments().getDouble("locationBundleLat");
        longitude = getArguments().getDouble("locationBundleLng");
//todo
        tempSoldier = databaseHelper.getAllSoldiers().get(soldierIndex);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps_adder_dialog, container, false);

        getDialog().getWindow().setTitle(getResources().getString(R.string.dialogTitle));

        mapAddEditText = (EditText) v.findViewById(R.id.mapEditText);
        mapAddCancel = (TextView) v.findViewById(R.id.mapCancel);
        mapAddSubmit = (TextView) v.findViewById(R.id.mapSubmit);

        mapAddEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dialogString = s.toString();

                try {
                    //consider adding new special characters
                    dialogString = dialogString.replace("'", "\'");
                } catch(NullPointerException e) {
                    //there were no special characters
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mapAddCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        mapAddSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationBundle tempBundle = new LocationBundle(new LatLng(latitude, longitude));
                tempBundle.setLocalName(dialogString);

                databaseHelper.createLocation(tempBundle);
                databaseHelper.assignLocationToSolider(tempBundle, tempSoldier);

                getDialog().dismiss();
                mapsPullInterface.onDialogCompletion(tempBundle,
                        databaseHelper.getAllLocationsBySoldier(tempSoldier));

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
