package com.androidtitan.hotspots.main.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.common.BaseActivity;
import com.androidtitan.hotspots.main.model.newyorktimes.Article;
import com.androidtitan.hotspots.main.presenter.news.DaggerNewsPresenterComponent;
import com.androidtitan.hotspots.main.presenter.news.NewsPresenter;
import com.androidtitan.hotspots.main.presenter.news.NewsPresenterComponent;
import com.androidtitan.hotspots.main.presenter.news.NewsPresenterModule;
import com.androidtitan.hotspots.main.presenter.news.NewsView;
import com.androidtitan.hotspots.main.ui.adapter.NewsAdapter;

import java.util.List;

import javax.inject.Inject;

public class NewsActivity extends BaseActivity implements NewsView{
    private final String TAG = getClass().getSimpleName();

    private static NewsPresenterComponent newsPresenterComponent;

    @Inject NewsPresenter presenter;

    RecyclerView recyclerView;
    NewsAdapter adapter;

    List<Article> articles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        implementComponents();

        articles = presenter.queryNews("world", 20);

        initializeRecyclerView();

    }

    public void implementComponents() {
        newsPresenterComponent = DaggerNewsPresenterComponent.builder()
                .newsPresenterModule(new NewsPresenterModule(this, this)) //this can be removed
                .build();
        newsPresenterComponent.inject(this);
    }

    private void initializeRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NewsAdapter(this, articles);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void updateNewsAdapter() {
        adapter.notifyDataSetChanged();
    }
}
