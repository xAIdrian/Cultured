package com.androidtitan.alphaarmyapp.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtitan.alphaarmyapp.Activity.MainActivity;
import com.androidtitan.alphaarmyapp.Data.DatabaseHelper;
import com.androidtitan.alphaarmyapp.Interface.SecondF2AInterface;
import com.androidtitan.alphaarmyapp.R;


public class AdderFragment extends Fragment {
    public static final String SINFO = "soldierInformation";
    private static final String SAVED_FIRST = "savedFirst";
    private static final String SAVED_LAST = "savedLast";
    private static final String SAVED_SPECIAL = "savedSpecial";

    DatabaseHelper databaseHelper;
    SecondF2AInterface toSecActivityInterface;

    private LinearLayout backLayout;
    private ImageView backImage;

    private EditText firstEdit;
    private EditText lastEdit;
    private EditText specialEdit;
    private TextView addBtn;

    private String newFname;
    private String newLname;
    private String newSpecs;

    ListView alertList;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            toSecActivityInterface = (SecondF2AInterface) activity;
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
        if(savedInstanceState != null){
            Log.e("AFonCreate", savedInstanceState.getString(SAVED_FIRST) + " " + savedInstanceState.getString(SAVED_LAST)
                    + " - " + savedInstanceState.getString(SAVED_SPECIAL));
            newFname = savedInstanceState.getString(SAVED_FIRST);
            newLname = savedInstanceState.getString(SAVED_LAST);
            newSpecs = savedInstanceState.getString(SAVED_SPECIAL);
        }
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_adder, container, false);
        if(getResources().getConfiguration().orientation != getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
            backImage = (ImageView) v.findViewById(R.id.back_header);
            backImage.setColorFilter(0xFFf16b0c);
        }

        firstEdit = (EditText) v.findViewById(R.id.firstName_edit);
        lastEdit = (EditText) v.findViewById(R.id.lastName_edit);
        specialEdit = (EditText) v.findViewById(R.id.specialty_edit);

        backLayout = (LinearLayout) v.findViewById(R.id.back_layout);

        addBtn = (TextView) v.findViewById(R.id.submit_button);

        alertList = (ListView) v.findViewById(R.id.basicList);

        backLayout.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("pass2FIRST", SINFO);
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
        specialEdit.setText(newSpecs);
        specialEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newSpecs = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        addBtn.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstEdit.getText().toString().matches("") || lastEdit.getText().toString().matches("") ||
                        specialEdit.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), "Please complete fields", Toast.LENGTH_LONG).show();

                } else {
                    toSecActivityInterface.soldierInfo(newFname, newLname, newSpecs);
                }
            }
        });

        return v;
    }

    //Saves data that is lost on rotation
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(savedInstanceState);
        Log.e("AFonSaveInstanceState", firstEdit.getText().toString() + " " + lastEdit.getText().toString()
            + " - " + specialEdit.getText().toString());

        outState.putString(SAVED_FIRST, firstEdit.getText().toString());
        outState.putString(SAVED_LAST, lastEdit.getText().toString());
        outState.putString(SAVED_SPECIAL, specialEdit.getText().toString());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

}
