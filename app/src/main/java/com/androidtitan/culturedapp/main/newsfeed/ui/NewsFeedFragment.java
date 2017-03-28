package com.androidtitan.culturedapp.main.newsfeed.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.structure.BasePresenter;
import com.androidtitan.culturedapp.main.newsfeed.NewsFeedMvp;
import com.androidtitan.culturedapp.model.newyorktimes.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends FeedFragment implements NewsFeedMvp.View{


    public NewsFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_feed, container, false);
    }

    @Override
    protected int getFragmentLayout() {
        return 0;
    }

    @Override
    public void initializeAnimation() {

    }

    @Override
    protected void initializeRecyclerView(int screenSize, RecyclerView.LayoutManager layoutManager) {

    }

    @Override
    public void startDetailActivity(Article article, ImageView articleImage) {

    }

    @Override
    public ArrayList<String> getGeoFacetArrayList(@NonNull Article article) {
        return null;
    }

    @Override
    public boolean isArticleBookmarked(@NonNull String articleTitle) {
        return false;
    }

    @Override
    public void onLoadComplete() {

    }

    @Override
    public void appendAdapterItem(Article article) {

    }

    @Override
    public void insertAdapterItem(int index, Article article) {

    }

    @Override
    public void insertAdapterItems(int index, ArrayList<Article> articles) {

    }

    @Override
    public List<Article> getArticles() {
        return null;
    }

    @Override
    public void displayError(String message, Map<String, Object> additionalProperties) {

    }
}
