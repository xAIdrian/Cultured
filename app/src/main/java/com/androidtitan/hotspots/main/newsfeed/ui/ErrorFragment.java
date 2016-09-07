package com.androidtitan.hotspots.main.newsfeed.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidtitan.hotspots.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

import static com.androidtitan.hotspots.main.newsfeed.ui.NewsActivity.ERROR_MAP;
import static com.androidtitan.hotspots.main.newsfeed.ui.NewsActivity.ERROR_MESSAGE;


public class ErrorFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    @Bind(R.id.restartTextView) TextView restartText;

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
        View v = inflater.inflate(R.layout.fragment_error, container, false);

        restartText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo : restart app
            }
        });

        return v;
    }

}
