package com.androidtitan.trooptracker.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooptracker.Activity.LandingActivity;
import com.androidtitan.trooptracker.Data.DatabaseHelper;
import com.androidtitan.trooptracker.Data.Division;


public class DivAdderFragment extends Fragment {
    private static final String SAVED_NAME = "savedName";

    DatabaseHelper databaseHelper;

    LinearLayout backLayout;
    private EditText nameEdit;
    private TextView deleteBtn;
    private TextView addBtn;

    private boolean isEdit = false;
    private int divSelected;
    Division simpleEditDivision;
    private String newName;

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

    public static DivAdderFragment newInstance(String param1, String param2) {
        DivAdderFragment fragment = new DivAdderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public DivAdderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = DatabaseHelper.getInstance(getActivity());

        setRetainInstance(true); //retains our data object when activity is desroyed
        if(savedInstanceState != null) {
            newName = savedInstanceState.getString(SAVED_NAME);
        }

        Bundle bundle = new Bundle();
        bundle = this.getArguments();
        isEdit = bundle.getBoolean("landingEdit"); //false
        divSelected = bundle.getInt("landingDivision"); // -1

        //checking if we are editing a division
        if(divSelected != -1) {
            simpleEditDivision = databaseHelper.getAllDivisions().get(divSelected);
            newName = simpleEditDivision.getName();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_div_adder, container, false);

        nameEdit = (EditText) v.findViewById(R.id.name_edit);
        addBtn = (TextView) v.findViewById(R.id.submit_button);
        deleteBtn = (TextView) v.findViewById(R.id.deleteBtn);
        backLayout = (LinearLayout) v.findViewById(R.id.back_layout);

        //changes to our view if we are editing an element.
        if(divSelected != -1) {
            addBtn.setText("Edit");
        }
            backLayout.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LandingActivity.class);
                    getActivity().finish();
                    startActivity(intent);
                }
            });

        nameEdit.setText(newName);
        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //try to delete the division and it's children
                    databaseHelper.deleteDivision(databaseHelper.getAllDivisions().get(divSelected), true);

                } catch (NullPointerException e) {
                    //if it doesn't have any children then delete just the division
                    databaseHelper.deleteDivision(databaseHelper.getAllDivisions().get(divSelected), false);

                }

                Intent intent = new Intent(getActivity(), LandingActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });

        addBtn.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nameEdit.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), "Please complete field", Toast.LENGTH_LONG).show();
                }
                else {

                    if(isEdit == false) {
                        Log.e("DAFaddBtnClick", "Adding Division: " + newName);
                        Division tempDivision = new Division(newName);
                        //tempDivision.setName(newName);
                        databaseHelper.createDivision(tempDivision);
                    }
                    else {
                        Division advEditDiv = databaseHelper.getAllDivisions().get(divSelected);
                        advEditDiv.setName(newName);
                        databaseHelper.updateDivision(advEditDiv);
                    }

                    Intent intent = new Intent(getActivity(), LandingActivity.class);
                    getActivity().finish();
                    startActivity(intent);
                }
            }
        });

        return v;
    }

    //Saves data that is lost on rotation
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.e("AFonSaveInstanceState", nameEdit.getText().toString());

        outState.putString(SAVED_NAME, nameEdit.getText().toString());
    }
    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }

}
