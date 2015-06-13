package com.androidtitan.trooPTracker.Fragment;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooPTracker.Activity.ChampionActivity;
import com.androidtitan.trooPTracker.Data.DatabaseHelper;
import com.androidtitan.trooPTracker.Data.Division;
import com.androidtitan.trooPTracker.Data.Soldier;
import com.androidtitan.trooPTracker.Interface.AdderInterface;

import java.util.ArrayList;
import java.util.List;


public class AdderFragment extends Fragment {
    public static final String SINFO = "soldierInformation";
    private static final String SAVED_FIRST = "savedFirst";
    private static final String SAVED_LAST = "savedLast";

    DatabaseHelper databaseHelper;
    AdderInterface adderInterface;

    private LinearLayout backLayout;

    private EditText firstEdit;
    private EditText lastEdit;
    private ListView addListView;
    //private mapBtn;
    private TextView addBtn;

    private int soldierIndex;
    private String newFname;
    private String newLname;
    private Boolean editPage = false;

    private int divSelected = -1;

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

        try {
            Bundle bundle = new Bundle();
            bundle = this.getArguments();
            soldierIndex = bundle.getInt("editSoloIndex");
            divSelected = bundle.getInt("editSoloDivIndex");
            newFname = bundle.getString("editSoloFirst");
            newLname = bundle.getString("editSoloLast");

            Log.e("AFonCreate", "Are we editing? " + editPage);
            editPage = true;
            addBtn.setText("Edit");

        } catch (NullPointerException e) {
            Log.e("AFonCreate", String.valueOf(e));


            Log.e("AFonCreate", divSelected + " - " + " - " + soldierIndex + " - "
                    + newFname + " " + newLname);
        }


        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_adder, container, false);
        if(getResources().getConfiguration().orientation != getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
        }
        backLayout = (LinearLayout) v.findViewById(R.id.back_layout);

        firstEdit = (EditText) v.findViewById(R.id.firstName_edit);
        lastEdit = (EditText) v.findViewById(R.id.lastName_edit);

        //mapBtn
        addBtn = (TextView) v.findViewById(R.id.submit_button);

        backLayout.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ChampionActivity.class);
                startActivity(intent);
            }
        });
        firstEdit.setText(newFname);
        firstEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newFname = s.toString();
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

        //ListView and adapter
        addListView = (ListView) v.findViewById(R.id.adder_listview);
        final ArrayList<String> divisionList = new ArrayList<String>();
        final List<Division> allDivisions = databaseHelper.getAllDivisions();

        for (int i = 0; i < allDivisions.size(); i++) {
            divisionList.add(allDivisions.get(i).getName());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, divisionList);
        addListView.setAdapter(adapter);
/*

        for(int i = 0; i < adapter.getCount(); i++) {
            if(adapter.getItemId(i) == divSelected) {
                View item = addListView.getChildAt(i);
                item.setBackgroundColor(0xCC448AFF);
            }
        }
*/

        addListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                divSelected = position;

                for (int i = 0; i < adapter.getCount(); i++) {
                    View item = addListView.getChildAt(i);
                    item.setBackgroundColor(0xFFFFFFFF);
                }

                view.setBackgroundColor(0xCC448AFF);
            }
        });

        addBtn.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {

                Division assignedToDiv = assignedToDiv = allDivisions.get(divSelected);

                if (firstEdit.getText().toString().matches("") || lastEdit.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), "Please complete fields", Toast.LENGTH_LONG).show();
                } else {
                    //if we are adding a New user
                    if (editPage == false) {
                        //add to database. associate division
                        Soldier temp = new Soldier(newFname, newLname);

                        databaseHelper.createSoldier(temp);
                        databaseHelper.assignSoldierToDivision(temp, assignedToDiv);

                        adderInterface.divInteraction(divSelected);
                    }
                    //if we are editing an existing user
                    else {
                        Soldier updateSoldier = databaseHelper.getAllSoldiersByDivision(databaseHelper.getAllDivisions().get(divSelected))
                                .get(soldierIndex);

                        updateSoldier.setfName(newFname);
                        updateSoldier.setlName(newLname);

                        databaseHelper.updateSoldier(updateSoldier);
                        databaseHelper.updateSoldierDivision(updateSoldier, assignedToDiv);

                        adderInterface.divInteraction(divSelected);


                        //if the user is not in the Division selected. Assign and update division

                    }
                }
            }
        });

        return v;
    }

    //Saves data that is lost on rotation
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.e("AFonSaveInstanceState", firstEdit.getText().toString() + " " + lastEdit.getText().toString());

        outState.putString(SAVED_FIRST, firstEdit.getText().toString());
        outState.putString(SAVED_LAST, lastEdit.getText().toString());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

}
