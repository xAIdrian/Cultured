package com.androidtitan.trooPTracker.Dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooPTracker.Data.DatabaseHelper;
import com.androidtitan.trooPTracker.Data.Division;
import com.androidtitan.trooPTracker.Data.Soldier;
import com.androidtitan.trooPTracker.Interface.SecondInterface;

import java.util.ArrayList;
import java.util.List;


public class DialogListFragment extends DialogFragment {

    DatabaseHelper databaseHelper;
    SecondInterface toSecondActivityInterface;

    private ListView myList;
    private TextView addDialog;

    private String fName;
    private String lName;
    private String sName;
    private int selection;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        databaseHelper = new DatabaseHelper(getActivity());
        ArrayList<String> divisionList = new ArrayList<String>();
        //We took the easy way out. This should be a dynamic list of Divisions
        final List<Division> allDivisions = databaseHelper.getAllDivisions();

        for (int i = 0; i < allDivisions.size(); i++) {
            divisionList.add(allDivisions.get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, divisionList);

        myList.setAdapter(adapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selection = position;
                Soldier temp = new Soldier(fName, lName, sName);
                Division assignerDiv = allDivisions.get(selection);

                databaseHelper.createSoldier(temp);
                databaseHelper.assignSoldierToDivision(temp, assignerDiv);

                dismiss();

                //toSecondActivityInterface.tabInteraction(selection);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            toSecondActivityInterface = (SecondInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement F2AInterface");
        }
    }

    public DialogListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bundle addSoldierBundle = new Bundle();
        try {
            fName = getArguments().getString("Fpassable");
            lName = getArguments().getString("Lpassable");
            sName = getArguments().getString("Spassable");
        } catch(NullPointerException e) {
            Log.e("receiveBundle", "NULL! Inside ListDialogFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_dialog, container, false);

        myList = (ListView) v.findViewById(R.id.basicList);
        getDialog().getWindow().setTitle("Which Division?");

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
