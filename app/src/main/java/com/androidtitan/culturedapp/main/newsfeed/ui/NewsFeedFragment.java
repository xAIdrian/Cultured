package com.androidtitan.culturedapp.main.newsfeed.ui;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.structure.BasePresenter;
import com.androidtitan.culturedapp.main.newsfeed.NewsFeedMvp;
import com.androidtitan.culturedapp.main.newsfeed.NewsFeedPresenter;
import com.androidtitan.culturedapp.main.newsfeed.adapter.NewsFeedAdapter;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

import static com.androidtitan.culturedapp.main.newsfeed.ui.NewsFeedActivity.ARTICLE_BOOKMARKED;
import static com.androidtitan.culturedapp.main.newsfeed.ui.NewsFeedActivity.ARTICLE_EXTRA;
import static com.androidtitan.culturedapp.main.newsfeed.ui.NewsFeedActivity.ARTICLE_GEO_FACETS;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends FeedFragment implements NewsFeedMvp.View{

    private static final String TAG = NewsFeedFragment.class.getSimpleName();

    private NewsFeedPresenter presenter;

    private Handler handler;
    private Animation fadeAnim;

    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager staggeredLayoutManager;
    private NewsFeedAdapter adapter;

    @Bind(R.id.newsList)
    RecyclerView recyclerView;

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    public NewsFeedFragment newInstance() {
        NewsFeedFragment fragment = new NewsFeedFragment();
        //Bundle bundle = new Bundle();
        //arguments set to bundle
        //fragment.setArgs
        return fragment;
    }

    @Override
    public BasePresenter getPresenter() {
        presenter = ((NewsFeedActivity) getActivity()).getPresenter();
        presenter.bindView(this);
        return presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.news_feed_fragment, container, false);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.news_feed_fragment;
    }

    @Override
    public void initializeAnimation() {
        handler = new Handler();
        fadeAnim = AnimationUtils.loadAnimation(this, R.anim.fade_out);
    }

    @Override
    protected void setupRecyclerView(int screenSize, RecyclerView.LayoutManager layoutManager) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {


            if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManager = new StaggeredGridLayoutManager(3, 1);
                recyclerView.setLayoutManager(layoutManager);
            } else {
                throw new IllegalArgumentException("Inappropriate layout manager used for this condition");
            }

        } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE ||
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManager = new StaggeredGridLayoutManager(2, 1);
                recyclerView.setLayoutManager(layoutManager);
            } else {
                throw new IllegalArgumentException("Inappropriate layout manager used for this condition");
            }

        } else {

            if (layoutManager instanceof LinearLayoutManager) {
                layoutManager = new LinearLayoutManager(getActivity());
                ((LinearLayoutManager)layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
            } else {
                throw new IllegalArgumentException("Inappropriate layout manager used for this condition");
            }
        }

        adapter = new NewsFeedAdapter(this, articles);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void startDetailActivity(Article article, ImageView articleImage) {
        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        intent.putExtra(ARTICLE_EXTRA, article);
        intent.putStringArrayListExtra(ARTICLE_GEO_FACETS, getGeoFacetArrayList(article));
        intent.putExtra(ARTICLE_BOOKMARKED, isArticleBookmarked(article.getTitle()));

        startActivity(intent);
    }

    @Override
    public ArrayList<String> getGeoFacetArrayList(@NonNull Article article) {
        ArrayList<String> facets = new ArrayList<>();
        for (Facet facet : article.getGeoFacet()) {
            facets.add(facet.getFacetText());
        }
        return facets;
    }

    @Override
    public boolean isArticleBookmarked(@NonNull String articleTitle) {
        if(bookMarkedArticles.get(articleTitle) != null) {
            return bookMarkedArticles.get(articleTitle);
        }
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
