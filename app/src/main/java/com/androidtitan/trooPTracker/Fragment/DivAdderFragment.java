package com.androidtitan.trooPTracker.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooPTracker.Activity.LandingActivity;
import com.androidtitan.trooPTracker.Data.DatabaseHelper;
import com.androidtitan.trooPTracker.Data.Division;


public class DivAdderFragment extends Fragment {

    DatabaseHelper databaseHelper;

    private EditText nameEdit;
    private TextView addBtn;

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

        //we need to receive our fragment's arguments
        if (getArguments() != null) {
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_div_adder, container, false);

        nameEdit = (EditText) v.findViewById(R.id.name_edit);
        addBtn = (TextView) v.findViewById(R.id.submit_button);

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

        addBtn.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call our interface
                Division tempDivision = new Division(newName);
                databaseHelper.createDivision(tempDivision);

                Intent intent = new Intent(getActivity(), LandingActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }


    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
