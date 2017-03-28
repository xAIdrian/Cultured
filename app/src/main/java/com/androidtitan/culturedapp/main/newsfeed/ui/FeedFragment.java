package com.androidtitan.culturedapp.main.newsfeed.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidtitan.culturedapp.common.structure.BaseFragment;
import com.androidtitan.culturedapp.model.newyorktimes.Article;

import java.util.ArrayList;

public abstract class FeedFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeAnimation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected abstract int getFragmentLayout();

    public abstract void initializeAnimation();

    protected abstract void initializeRecyclerView(int screenSize, RecyclerView.LayoutManager layoutManager);

    public abstract void startDetailActivity(Article article, ImageView articleImage);

    public abstract ArrayList<String> getGeoFacetArrayList(@NonNull Article article);

    public abstract boolean isArticleBookmarked(@NonNull String articleTitle);
}
