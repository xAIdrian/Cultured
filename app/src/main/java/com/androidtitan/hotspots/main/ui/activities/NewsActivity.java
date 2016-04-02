package com.androidtitan.hotspots.main.ui.activities;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Transition;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.common.BaseActivity;
import com.androidtitan.hotspots.main.model.newyorktimes.Article;
import com.androidtitan.hotspots.main.presenter.news.DaggerNewsPresenterComponent;
import com.androidtitan.hotspots.main.presenter.news.NewsPresenter;
import com.androidtitan.hotspots.main.presenter.news.NewsPresenterComponent;
import com.androidtitan.hotspots.main.presenter.news.NewsPresenterModule;
import com.androidtitan.hotspots.main.presenter.news.NewsView;
import com.androidtitan.hotspots.main.ui.adapter.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

public class NewsActivity extends BaseActivity implements NewsView,
        RecyclerView.OnItemTouchListener{
    private final String TAG = getClass().getSimpleName();

    private static final String SAVED_STATE_ARTICLE_LIST = "newsactivity.savedstatearticles";
    public static final String ARTICLE_EXTRA = "newsactivity.articleextra";

    public static NewsPresenterComponent newsPresenterComponent;
    @Inject NewsPresenter presenter;

    private GestureDetectorCompat gestureDetector;
    private Animation rotateAnim;

    @Bind(R.id.colorBgView) View bgView;
    @Bind(R.id.swipeRefresh) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.list) RecyclerView recyclerView;
    @Bind(R.id.refreshFab) FloatingActionButton refreshFab;

    private NewsAdapter adapter;

    private List<Article> articles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeTranstionsAndAnimations();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ButterKnife.bind(this);
        gestureDetector = new GestureDetectorCompat(this, new RecyclerViewGestureListener());

        implementComponents();

        //saveInstanceState to handle rotations
        if(savedInstanceState != null) {
            //articles = savedInstanceState.getParcelableArrayList(SAVED_STATE_ARTICLE_LIST);
        }
        articles = presenter.queryNews("world", 20);
        initializeRecyclerView();

        refreshFab.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshFab.startAnimation(rotateAnim);
                refreshRecyclerView();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshRecyclerView();
    }

    /**
     * Save all appropriate fragment state.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(SAVED_STATE_ARTICLE_LIST, (ArrayList<? extends Parcelable>) articles);
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

    private void initializeRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(this);
        recyclerView.setItemAnimator(new SlideInDownAnimator());

        adapter = new NewsAdapter(this, articles);
        recyclerView.setAdapter(adapter);
    }

    private void scrollViewParallax(int dy) { // divided by three to scroll slower
        bgView.setTranslationY(bgView.getTranslationY() + dy / 5);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initializeTranstionsAndAnimations() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // inside your activity (if you did not enable transitions in your theme)
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            // set an exit transition
            Transition plode = new Explode();
            getWindow().setEnterTransition(plode);
            getWindow().setExitTransition(plode);

        } else {
            // do something for phones running an API before lollipop
        }
        rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise);
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


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startDetailActivity(Article article, ImageView articleImage) {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            //Pair<View, String> pair = Pair.create((View) articleImage, getString(R.string.transition_news_image));
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);

            Intent intent = new Intent(this, NewsDetailActivity.class);
            intent.putExtra(ARTICLE_EXTRA, article);
            startActivity(intent, options.toBundle());
        } else {
            Intent intent = new Intent(this, NewsDetailActivity.class);
            intent.putExtra(ARTICLE_EXTRA, article);
            startActivity(intent);
        }


    }


    @Override
    public void refreshCompleted() {
        swipeRefreshLayout.setRefreshing(false);
        refreshFab.clearAnimation();
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
            v.vibrate(50);
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
