package com.androidtitan.hotspots.main.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsActivity extends BaseActivity implements NewsView,
        RecyclerView.OnItemTouchListener{
    private final String TAG = getClass().getSimpleName();

    private static final String ARTICLE_IMAGE_PALETTE_EXTRA = "newsactivity.articleimagepaletteextra";
    private static final String ARTICLE_URL_EXTRA = "newsactivity.articleurlextra";

    public static NewsPresenterComponent newsPresenterComponent;
    @Inject NewsPresenter presenter;

    private GestureDetectorCompat gestureDetector;

    @Bind(R.id.toolbarContainer) RelativeLayout toolbarContainer;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.colorBgView) View bgView;
    @Bind(R.id.refreshImageView) ImageView refreshImageView;
    @Bind(R.id.swipeRefresh) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.list) RecyclerView recyclerView;
    private NewsAdapter adapter;

    List<Article> articles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeTranstions();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ButterKnife.bind(this);
        gestureDetector = new GestureDetectorCompat(this, new RecyclerViewGestureListener());

        implementComponents();

        //presenter.startMusicActivity("nothing for right now");
        articles = presenter.firstQueryNews("world", 20);

        setUpToolbar();
        initializeRecyclerView();

        refreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setRefreshing(true);
                presenter.refreshQueryNews("world", 20);
            }
        });

        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRecyclerView();
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrollViewParallax(dy);

            }
        });

    }

    private void scrollViewParallax(int dy) { // divided by three to scroll slower
        bgView.setTranslationY(bgView.getTranslationY() + dy / 5);
    }


    private void showToolbar() {
        toolbar
                .animate()
                .translationY(0)
                .start();
    }

    private void hideToolbar() {
        toolbar
                .animate()
                .translationY((toolbar.getBottom()))
                .start();
    }

    public void implementComponents() {
        newsPresenterComponent = DaggerNewsPresenterComponent.builder()
                .newsPresenterModule(new NewsPresenterModule(this, this)) //this can be removed
                .build();
        newsPresenterComponent.inject(this);
    }

    public NewsPresenter getNewsPresenter() {
        return presenter;
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        final ActionBar actionBar = getSupportActionBar();

    }

    private void initializeRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(this);

        adapter = new NewsAdapter(this, articles);
        recyclerView.setAdapter(adapter);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initializeTranstions() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // inside your activity (if you did not enable transitions in your theme)
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            // set an exit transition
            Transition plode = new Explode();
            getWindow().setEnterTransition(plode);
            getWindow().setExitTransition(plode);

        } else {
            // do something for phones running an SDK before lollipop
        }
    }

    public void refreshRecyclerView() {
        presenter.refreshQueryNews("world", 20);
    }

    @Override
    public void updateNewsAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateSpecificNewsAdapter(int position) {
        adapter.notifyItemChanged(position);
    }


    public void startDetailActivity(String url, int paletteColor) {
        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putExtra(ARTICLE_URL_EXTRA, url);
        intent.putExtra(ARTICLE_IMAGE_PALETTE_EXTRA, paletteColor);
        startActivity(intent);
    }


    @Override
    public void refreshCompleted() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class RecyclerViewGestureListener extends GestureDetector.SimpleOnGestureListener {

        Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            int position = recyclerView.getChildAdapterPosition(view);

            // Vibrate for 500 milliseconds
            v.vibrate(100);
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            int position = recyclerView.getChildAdapterPosition(view);

            // Vibrate for 500 milliseconds
            //v.vibrate(50);
            return super.onDown(e);
        }
    }
}
