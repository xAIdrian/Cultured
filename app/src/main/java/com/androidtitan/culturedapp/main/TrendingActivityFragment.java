package com.androidtitan.culturedapp.main;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidtitan.culturedapp.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class TrendingActivityFragment extends Fragment {

    public TrendingActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trending_fragment, container, false);
    }
}
