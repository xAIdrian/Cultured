package com.androidtitan.alphaarmyapp.Dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.Data.DatabaseHelper;
import com.androidtitan.alphaarmyapp.Data.Division;
import com.androidtitan.alphaarmyapp.Data.Soldier;
import com.androidtitan.alphaarmyapp.Interface.F2AInterface;
import com.androidtitan.alphaarmyapp.R;

import java.util.List;

public class DialogAdderFragment extends DialogFragment {

    DatabaseHelper databaseHelper;
    F2AInterface toActivityInterface;

    //back button needs to be added
    private TextView d_back;

    private TextView d_titleText;
    private EditText d_editText;

    private TextView d_mapBtn;
    private TextView d_divBtn;
    private TextView d_addBtn;

    private  int soldierInt;
    private int divisionInt;
    private String d_newFname;
    private String d_newLname;
    private String d_newSpecs;

    private int pageIndex = 0;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            toActivityInterface = (F2AInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public static DialogAdderFragment newInstance(String param1, String param2) {
        DialogAdderFragment fragment = new DialogAdderFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public DialogAdderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = new Bundle();
        bundle = this.getArguments();
        soldierInt = bundle.getInt("dialogInt");
        divisionInt = bundle.getInt("dialogDiv");
        d_newFname = bundle.getString("dialogFirst");
        d_newLname = bundle.getString("dialogLast");
        d_newSpecs = bundle.getString("dialogSpec");

        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_adder, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        d_back = (TextView) v.findViewById(R.id.d_back);
        d_titleText = (TextView) v.findViewById(R.id.d_TitleText);
        d_editText = (EditText) v.findViewById(R.id.d_edit);
        d_addBtn = (TextView) v.findViewById(R.id.d_submit_button);

        if(pageIndex == 0) {
            d_editText.setText(d_newFname);
        }
        else if(pageIndex == 1) {
            d_editText.setText(d_newLname);
        }
        else if (pageIndex == 2) {
            d_editText.setText(d_newSpecs);
        }

        d_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pageIndex == 0) {
                    d_newFname = s.toString();
                }
                else if(pageIndex == 1) {
                    d_newLname = s.toString();
                }
                else if(pageIndex == 2) {
                    d_newSpecs = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        d_addBtn.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update soldier in database and add to new division
                if(pageIndex == 0) {
                    pageIndex ++;
                    d_titleText.setText("last name...");
                    d_editText.setText(d_newLname);
                }
                else if(pageIndex == 1) {
                    pageIndex ++;
                    d_titleText.setText("specialty...");
                    d_editText.setText(d_newSpecs);
                    d_addBtn.setText("ADD!");
                }
                else if(pageIndex == 2) {
                    //here we are going to implement our new layout...
                    Log.e("DAFaddBtn", "variable checker: " + d_newFname + " " + d_newLname +
                            " - " + d_newSpecs);

                    Division division = databaseHelper.getAllDivisions().get(divisionInt);
                    Log.e("DAFonAddBtn", division.getName());
                    List<Soldier> troops = databaseHelper.getAllSoldiersByDivision(division);
                    Soldier soldier = troops.get(soldierInt);

                    soldier.setfName(d_newFname);
                    soldier.setlName(d_newLname);
                    soldier.setSpecialty(d_newLname);

                    databaseHelper.updateSoldier(soldier);

                    getDialog().dismiss();



                    toActivityInterface.refreshViewPager(divisionInt);
                }
            }
        });

        d_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageIndex == 0) {
                    getDialog().dismiss();
                }
                else if(pageIndex == 1) {
                    pageIndex --;
                    d_titleText.setText("first name...");
                    d_editText.setText(d_newFname);
                }
                else if(pageIndex == 2) {
                    pageIndex --;
                    d_titleText.setText("last name...");
                    d_editText.setText(d_newLname);
                }
            }
        });

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }


}
