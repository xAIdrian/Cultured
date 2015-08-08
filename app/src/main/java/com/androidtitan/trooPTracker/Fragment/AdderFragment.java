package com.androidtitan.trooptracker.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.androidtitan.trooptracker.Activity.ChampionActivity;
import com.androidtitan.trooptracker.Data.DatabaseHelper;
import com.androidtitan.trooptracker.Data.Soldier;
import com.androidtitan.trooptracker.Interface.AdderInterface;


public class AdderFragment extends Fragment {
    private static final String TAG = "AdderFragment";

    private static final String SAVED_FIRST = "savedFirst";
    private static final String SAVED_LAST = "savedLast";

    DatabaseHelper databaseHelper;
    AdderInterface adderInterface;

    private LinearLayout backLayout;

    private EditText firstEdit;
    private EditText lastEdit;
    private TextView deleteBtn;
    private TextView addBtn;


    private int soldierIndex = -1;
    private String newFname = "blank";
    private String newLname = "blank";
    private Boolean editPage = false;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            adderInterface = (AdderInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public AdderFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loads data that is saved when the screen is rotated
        setRetainInstance(true); //retains our data object when activity is desroyed
        if(savedInstanceState != null) {
            newFname = savedInstanceState.getString(SAVED_FIRST);
            newLname = savedInstanceState.getString(SAVED_LAST);
        }
        Bundle bundle = new Bundle();
        bundle = this.getArguments();
        soldierIndex = bundle.getInt("editSoloIndex");
        newFname = bundle.getString("editSoloFirst");
        newLname = bundle.getString("editSoloLast");


        if(newFname == null || newLname == null) {
            editPage = false;
        }
        else {
            editPage = true;
        }

        databaseHelper = new DatabaseHelper(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_adder, container, false);

        backLayout = (LinearLayout) v.findViewById(R.id.back_layout);

        firstEdit = (EditText) v.findViewById(R.id.firstName_edit);
        lastEdit = (EditText) v.findViewById(R.id.lastName_edit);

        deleteBtn = (TextView) v.findViewById(R.id.deleteBtn);
        if(soldierIndex == -1) {
            deleteBtn.setTextColor(0xFFFFFFFF);
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());

                // set title
                alertDialogBuilder.setTitle("Delete?");

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //todo
                                Soldier focusSoldier = databaseHelper.getSoldier(soldierIndex);
                                Log.e("AFdeleter", focusSoldier.getfName() + " " + focusSoldier.getId());

                                databaseHelper.deleteSoldier(focusSoldier);

                                Intent returnIntent = new Intent(getActivity(), ChampionActivity.class);
                                startActivity(returnIntent);
                                //getActivity().finish();  we don't need this. We override the onBackPressed
                                    // in the next activity
                                Log.e(TAG, "printSoldierTable()");
                                databaseHelper.printSoldierTable();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });

        addBtn = (TextView) v.findViewById(R.id.submit_button);

        if(editPage == true) {
            addBtn.setText("Edit");
        }

        //todo: we need to
        backLayout.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                adderInterface.returnToChamp();

            }
        });

        firstEdit.setText(newFname);

        firstEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstEdit.setSelectAllOnFocus(true);

            }
        });
        firstEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newFname = s.toString();

                //consider implementing this.  We would need to do it for all SPECIAL CHARS
                //newFname = newFname.replace("'","\'");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        lastEdit.setText(newLname);
        lastEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newLname = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        addBtn.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (firstEdit.getText().toString().matches("") || lastEdit.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), "Please complete fields", Toast.LENGTH_LONG).show();
                } else {

                    //if we are editing an existing user
                    if(editPage == true) {

                        //todo
                        Soldier updateSoldier = databaseHelper.getSoldier(soldierIndex);

                        updateSoldier.setfName(newFname);
                        updateSoldier.setlName(newLname);

                        databaseHelper.updateSoldier(updateSoldier);

                        adderInterface.returnToChamp();
                    }

                    //if we are adding a New user
                    //if (editPage == false)
                    else {
                        //add to database. associate division
                        Soldier temp = new Soldier(newFname, newLname);

                        databaseHelper.createSoldier(temp);

                        adderInterface.returnToChamp();
                    }

                }
            }
        });

        return v;
    }

    //Saves data that is lost on rotation
    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString(SAVED_FIRST, firstEdit.getText().toString());
        outState.putString(SAVED_LAST, lastEdit.getText().toString());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

}
