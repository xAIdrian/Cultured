package com.androidtitan.culturedapp.main.newsfeed.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidtitan.culturedapp.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.androidtitan.culturedapp.main.newsfeed.ui.NewsFeedActivity.ERROR_MAP;
import static com.androidtitan.culturedapp.main.newsfeed.ui.NewsFeedActivity.ERROR_MESSAGE;


public class ErrorFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    ErrorFragmentInterface errorInterface;

    TextView restartText;
    TextView messageText;

    private String errorMessage;
    private HashMap<String, Object> errorMap;

    public ErrorFragment() {
        // Required empty public constructor
    }

    public static ErrorFragment newInstance(String errorMessage, Map<String, Object> errorMap) {
        ErrorFragment fragment = new ErrorFragment();
        Bundle args = new Bundle();
        args.putString(ERROR_MESSAGE, errorMessage);
        args.putSerializable(ERROR_MAP, (Serializable) errorMap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            errorMessage = getArguments().getString(ERROR_MESSAGE);
            errorMap = (HashMap<String, Object>) getArguments().getSerializable(ERROR_MAP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.error_fragment, container, false);

        restartText = (TextView) v.findViewById(R.id.restartTextView);
        messageText = (TextView) v.findViewById(R.id.extraInfoTextView);

        messageText.setText(errorMessage);

        restartText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorInterface.restartArticleLoad();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ErrorFragmentInterface) {
            errorInterface = (ErrorFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        errorInterface = null;
    }
}

interface ErrorFragmentInterface {
    void restartArticleLoad();
}