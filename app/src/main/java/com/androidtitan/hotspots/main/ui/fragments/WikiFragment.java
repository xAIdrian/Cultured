package com.androidtitan.hotspots.main.ui.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.common.BaseFragment;
import com.androidtitan.hotspots.main.presenter.newsdetail.NewsDetailPresenter;
import com.androidtitan.hotspots.main.ui.WikiFragScrollInterface;
import com.androidtitan.hotspots.main.ui.activities.NewsDetailActivity;
import com.androidtitan.hotspots.main.ui.adapter.SimpleWikiAdapter;

import butterknife.ButterKnife;


public class WikiFragment extends BaseFragment {
    private final String TAG = getClass().getSimpleName();

    private static final String WIKI_FRAG_LOCAL_BUNDLE = "wikifragment.wikifraglocalbundle";

    private NewsDetailPresenter presenter;

    WikiFragScrollInterface interfacer;
    public RecyclerView recyclerView;

    private String url;

    public WikiFragment() {
        // Required empty public constructor
    }

    public static WikiFragment newInstance(String param1) {
        Bundle args = new Bundle();
        args.putString(WIKI_FRAG_LOCAL_BUNDLE, param1);
        WikiFragment fragment = new WikiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            interfacer = (WikiFragScrollInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(WIKI_FRAG_LOCAL_BUNDLE, "nothing received");
            Log.e(TAG, url);
        }
        else {
            Log.e(TAG, "stump");
        }

        presenter = ((NewsDetailActivity)getActivity()).getPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wiki, container, false);
        ButterKnife.bind(this, v);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = (RecyclerView) v.findViewById(R.id.wikiRecycler);
        recyclerView.setLayoutManager(layoutManager);

        SimpleWikiAdapter adapter = new SimpleWikiAdapter(getContext(), url);
        recyclerView.setAdapter(adapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                interfacer.scrollViewParallax(dy);
            }
        });

        return v;
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        interfacer = null;
    }
}


