package com.androidtitan.trooPTracker.Fragment;

import android.app.Activity;
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
import com.androidtitan.trooPTracker.Data.DatabaseHelper;
import com.androidtitan.trooPTracker.Data.Division;
import com.androidtitan.trooPTracker.Data.Soldier;
import com.androidtitan.trooPTracker.Interface.AdderInterface;

import java.util.ArrayList;
import java.util.List;


public class AdderFragment extends Fragment {
    //public static final String SINFO = "soldierInformation";
    private static final String SAVED_FIRST = "savedFirst";
    private static final String SAVED_LAST = "savedLast";

    DatabaseHelper databaseHelper;
    AdderInterface adderInterface;

    ArrayAdapter<Division> adapter;

    private LinearLayout backLayout;

    private EditText firstEdit;
    private EditText lastEdit;
    private ListView addListView;
    //private mapBtn;
    private TextView addBtn;

    private int soldierIndex = -1;
    private String newFname = "blank";
    private String newLname = "blank";
    private Boolean editPage = false;

    private int divSelected = -1;
    private int oldDivision = -1;

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
        divSelected = bundle.getInt("editSoloDivIndex");
        soldierIndex = bundle.getInt("editSoloIndex");
        newFname = bundle.getString("editSoloFirst");
        newLname = bundle.getString("editSoloLast");

        if(newFname == null || newLname == null) {
            editPage = false;
        }
        else {
            oldDivision = divSelected;
            editPage = true;
        }

        Log.e("AFonCreate", "Are we editing? " + editPage);

        databaseHelper = new DatabaseHelper(getActivity());

        Runnable run = new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                addListView.invalidateViews();
            }
        };
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
        if(editPage == true) {
            addBtn.setText("Edit");
        }

        backLayout.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                adderInterface.divInteraction(divSelected);

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

        for (Division div : allDivisions) {
            Log.e("AFonCreate", div.getName());
            divisionList.add(div.getName());
        }
        adapter = new AdderAdapter();
        addListView.setAdapter(adapter);
        //adapter = new ChampionAdapter((ArrayList) getListItems());
        //listView.setAdapter(adapter);

        addListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*
                listView.getChildAt(i) works where 0 is the very first visible row and (n-1)
                is the last visible row (where n is the number of visible views you see).
                 */
                for (int i = 0; i <= addListView.getLastVisiblePosition() - addListView.getFirstVisiblePosition(); i++) {

                    View item = addListView.getChildAt(i);
                    item.setBackgroundColor(0xFFFFFFFF);
                }

                view.setBackgroundColor(0xCC448AFF);

                divSelected = position;
            }
        });

        addBtn.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (firstEdit.getText().toString().matches("") || lastEdit.getText().toString().matches("")
                        || divSelected == -1) {
                    Toast.makeText(getActivity(), "Please complete fields", Toast.LENGTH_LONG).show();
                } else {
                    Division assignedToDiv = assignedToDiv = databaseHelper.getAllDivisions().get(divSelected);
                    //if we are editing an existing user
                    if(editPage == true) {
                        Log.e("AFaddBtn", "MADE IT HERE");

                        Soldier updateSoldier = databaseHelper.getAllSoldiersByDivision(databaseHelper.getAllDivisions().get(oldDivision))
                                .get(soldierIndex);

                        updateSoldier.setfName(newFname);
                        updateSoldier.setlName(newLname);

                        databaseHelper.updateSoldier(updateSoldier);
                        databaseHelper.updateSoldierDivision(updateSoldier, assignedToDiv);
                        //databaseHelper.updateDivision(databaseHelper.getAllDivisions().get(oldDivision));

                        adderInterface.divInteraction(divSelected);


                        //if the user is not in the Division selected. Assign and update division

                    }

                    //if we are adding a New user
                    //if (editPage == false)
                    else {
                        //add to database. associate division
                        Soldier temp = new Soldier(newFname, newLname);

                        databaseHelper.createSoldier(temp);
                        databaseHelper.assignSoldierToDivision(temp, assignedToDiv);

                        adderInterface.divInteraction(divSelected);
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

    //ADAPTER CLASS

    private class AdderAdapter extends ArrayAdapter<Division> {

        public AdderAdapter() {
            super(getActivity(), 0, databaseHelper.getAllDivisions()); // 0 is our resource
            //soldierItems = troops;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //if you weren't given a view inflate one
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.listview_champion_item, null);
            }
            if(position == oldDivision)
            {
                convertView.setBackgroundColor(0xCC448AFF);
            }
            Division division = databaseHelper.getAllDivisions().get(position);
            final TextView checkedText = (TextView) convertView.findViewById(R.id.champ_text);
            checkedText.setText(division.getName());

            return convertView;
        }
    }


}
