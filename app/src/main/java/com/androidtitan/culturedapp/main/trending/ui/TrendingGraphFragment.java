package com.androidtitan.culturedapp.main.trending.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.structure.MvpFragment;
import com.androidtitan.culturedapp.main.trending.TrendingMvp;
import com.androidtitan.culturedapp.main.trending.TrendingPresenter;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;

import java.util.List;

import butterknife.ButterKnife;


public class TrendingGraphFragment extends MvpFragment<TrendingPresenter, TrendingMvp.View> implements TrendingMvp.View{
    private final String TAG = getClass().getSimpleName();

    private TrendingFragmentInterface fragmentInterface;
    private TrendingPresenter presenter;

    public TrendingGraphFragment() {
        // Required empty public constructor
    }

    public static TrendingGraphFragment newInstance(TrendingPresenter presenter) {
        TrendingGraphFragment fragment = new TrendingGraphFragment();
//        Bundle args = new Bundle();
//        args.putArgs(key, value);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        presenter = fragmentInterface.getTrendingPresenter();
        presenter.subscribe(this);
        presenter.loadInitialFacets();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.trending_graph_fragment, container, false);

        ButterKnife.bind(this, v);



        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TrendingFragmentInterface) {
            fragmentInterface = (TrendingFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DevConsoleCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentInterface = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe(this);
    }

    @Override
    public void initializeGeoFacetSpark(List<Facet> geoFacetList) {


    }

    @Override
    public void initializeOrgFacetSpark(List<Facet> orgFacetList) {


    }

    @Override
    public void initializeDesFacetSpark(List<Facet> desFacetList) {


    }

    @Override
    public void initializePerFacetSpark(List<Facet> perFacetList) {


    }

    @Override
    public void setLoading() {


    }

    @Override
    public void displayDataNotAvailable() {

    }

    @Override
    public void displayDataEmpty() {

    }

    @Override
    public TrendingPresenter getPresenter() {
        return presenter;
    }

    @Override
    public TrendingMvp.View getMvpView() {
        return this;
    }

    public interface TrendingFragmentInterface {
        TrendingPresenter getTrendingPresenter();
    }
}


