package com.androidtitan.hotspots.main.newsdetail.fragments;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.common.BaseFragment;
import com.androidtitan.hotspots.main.newsdetail.NewsDetailPresenter;
import com.androidtitan.hotspots.main.newsdetail.NewsDetailActivity;
import com.androidtitan.hotspots.main.newsdetail.SimpleWikiAdapter;

import butterknife.ButterKnife;


public class WikiFragment extends BaseFragment {
    private final String TAG = getClass().getSimpleName();

    private static final String WIKI_FRAG_LOCAL_BUNDLE = "wikifragment.wikifraglocalbundle";

    private NewsDetailPresenter presenter;

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

        return v;
    }


}


