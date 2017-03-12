package com.androidtitan.culturedapp.main.toparticle.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.structure.BaseActivity;
import com.androidtitan.culturedapp.main.toparticle.TopArticleAdapter;
import com.androidtitan.culturedapp.main.toparticle.TopArticleMvp;
import com.androidtitan.culturedapp.main.toparticle.TopArticlePresenter;
import com.androidtitan.culturedapp.model.newyorktimes.Article;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TopArticleActivity extends BaseActivity implements TopArticleMvp.View {
    private final String TAG = getClass().getSimpleName();

    @Inject
    TopArticlePresenter presenter;

    @Bind(R.id.topArticleRecyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.pleaseWaitTextView)
    TextView pleaseWaitText;

    @Bind(R.id.fab)
    FloatingActionButton refreshFab;

    private LinearLayoutManager linearLayoutManager;
    private TopArticleAdapter topArticleAdapter;

    List<Article> adapterArticleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_article_activity);

        super.getAppComponent().inject(this);
        ButterKnife.bind(this);
        presenter.bindView(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Top Articles");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapterArticleList = new ArrayList<>();
        topArticleAdapter = new TopArticleAdapter(this, adapterArticleList);
        presenter.loadArticles();

        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadArticles();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //todo: store the data for the list on Orientation changed
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unbindView();
    }

    @Override
    public void updateArticles(List<Article> articleList) {

        if(articleList != null && articleList.size() > 0) {
            adapterArticleList.clear();
            adapterArticleList.addAll(articleList);

            //delaying the setting of our Adapter because if an recyclerview is set with an empty adapter
            //the views will not have been instantiated meaning they cannot be updated...
            if(recyclerView.getAdapter() == null) {
                recyclerView.setAdapter(topArticleAdapter);
            } else {
                topArticleAdapter.notifyDataSetChanged();
            }

            pleaseWaitText.setVisibility(View.GONE);
        }
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

}