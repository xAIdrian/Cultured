package com.androidtitan.hotspots.main.ui;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.common.BaseFragment;
import com.androidtitan.hotspots.main.application.App;
import com.androidtitan.hotspots.main.model.Item;
import com.androidtitan.hotspots.main.presenter.MainPresenter;
import com.androidtitan.hotspots.main.presenter.adapter.SpotifyCardAdapter;

import java.util.List;

import javax.inject.Inject;


public class ImageListFragment extends BaseFragment {
    private final String TAG = getClass().getSimpleName();

    @Inject
    MainPresenter presenter;

    private RecyclerView recyclerView;
    private SpotifyCardAdapter adapter;

    private List<Item> trackItems;

    public ImageListFragment() {
        // Required empty public constructor
    }

    public static ImageListFragment newInstance(String param1, String param2) {
        ImageListFragment fragment = new ImageListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //
        }
        //telling our component Where to inject Presenter
        App.getMainPresenterComponent().inject(this);

        trackItems = presenter.querySpotifyTracks("Down the Line", 10);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        initializeRecyclerView(v);

        return v;
    }

    private void initializeRecyclerView(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SpotifyCardAdapter(getActivity(), trackItems);
        recyclerView.setAdapter(adapter);
    }

}
