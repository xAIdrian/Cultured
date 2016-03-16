package com.androidtitan.hotspots.main.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.common.BaseFragment;
import com.androidtitan.hotspots.main.presenter.MainPresenter;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SplashFragment extends BaseFragment {

    private MainPresenter presenter;

    public static final String BASE_URL = "https://www.haloapi.com/metadata/h5/metadata/medals";

    public SplashFragment() {
        // Required empty public constructor
    }

    public static SplashFragment newInstance(String param1, String param2) {
        SplashFragment fragment = new SplashFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //
        }

        presenter = ((MainActivity)getActivity()).getMainPresenter();

        //todo: create Dagger2 dependency with these objects
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_splash, container, false);

        presenter.respond("Earths");

        return v;
    }

}
