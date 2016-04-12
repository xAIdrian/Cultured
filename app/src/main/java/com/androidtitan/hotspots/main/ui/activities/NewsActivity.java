package com.androidtitan.hotspots.main.ui.activities;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.common.BaseActivity;
import com.androidtitan.hotspots.main.CulturedApp;
import com.androidtitan.hotspots.main.model.newyorktimes.Article;
import com.androidtitan.hotspots.main.presenter.news.NewsPresenter;
import com.androidtitan.hotspots.main.ui.adapter.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsActivity extends BaseActivity implements RecyclerView.OnItemTouchListener{
    private final String TAG = getClass().getSimpleName();

    private static final String SAVED_STATE_ARTICLE_LIST = "newsactivity.savedstatearticles";
    public static final String ARTICLE_EXTRA = "newsactivity.articleextra";

    private static final int LOADING_ANIM_TIME = 700;

    @Inject NewsPresenter presenter;

    private GestureDetectorCompat gestureDetector;
    private Handler handler;
    private Animation rotateAnim;
    private Animation fadeAnim;

    @Bind(R.id.colorBgView) View bgView;
    @Bind(R.id.culturedTitleTextView) TextView loadingTitleText;

    @Bind(R.id.swipeRefresh) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.list) RecyclerView recyclerView;
    @Bind(R.id.refreshFab) FloatingActionButton refreshFab;

    private LinearLayoutManager layoutManager;
    private NewsAdapter adapter;

    private boolean firstLoad = true; //used for animation
    private boolean onRestartChecker = false;
    private boolean loading = true;
    private int pastVisibleItems;
    private int visibleItemCount;
    private int totalItemCount;
    public int adapterLoadOffset = 6;

    private List<Article> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeTranstionsAndAnimations();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ButterKnife.bind(this);
        CulturedApp.getAppComponent().inject(this);
        presenter.takeActivity(this);

        gestureDetector = new GestureDetectorCompat(this, new RecyclerViewGestureListener());
        initializeAnimation();

        //saveInstanceState to handle rotations
        if(savedInstanceState != null) {
            //articles = savedInstanceState.getParcelableArrayList(SAVED_STATE_ARTICLE_LIST);
        }

        articles = presenter.initialNewsQuery("world", 5);
        initializeRecyclerView();

        refreshFab.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshFab.startAnimation(rotateAnim);
                refreshEntireRecyclerView();
            }
        });

        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshEntireRecyclerView();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrollViewParallax(dy);

                if(dy > 0) { //check for scroll down
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstCompletelyVisibleItemPosition();

                    if(loading) {

                        if((visibleItemCount + pastVisibleItems) >= totalItemCount) {

                            refreshFab.setClickable(true);
                            loading = false;
                            Log.d(TAG, "appending data...");

                            swipeRefreshLayout.setRefreshing(true);
                            presenter.appendNewsQuery("world", 5, adapterLoadOffset);
                            adapterLoadOffset += 5;
                        }
                    }
                }
            }
        });

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

    private void initializeRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(this);
        //todo: recyclerView.setItemAnimator(new ());

        adapter = new NewsAdapter(this, articles);
        recyclerView.setAdapter(adapter);
    }

    private void scrollViewParallax(int dy) { // divided by three to scroll slower
        bgView.setTranslationY(bgView.getTranslationY() - dy / 4);
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
    }

    public void initializeAnimation() {

        handler = new Handler();
        rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise);
        fadeAnim = AnimationUtils.loadAnimation(this, R.anim.fade_out);

    }

    public void refreshEntireRecyclerView() {
        //todo:
    }

    public void  updateNewsAdapter() {

        if(firstLoad) {

            firstLoadCompleteAnimation();
        } else {

            adapter.notifyDataSetChanged();
        }
    }

    public void appendAdapterItemInserted(Article article) {
        adapter.appendToAdapter(article);
    }

    public void firstLoadCompleteAnimation() {

        final ScaleAnimation scale = new ScaleAnimation((float) 1.0, (float) 1.0, (float) 1.0, (float) 0.33);
        scale.setFillAfter(true);
        scale.setDuration(LOADING_ANIM_TIME);

        ///loadingTitleText.setVisibility(View.VISIBLE);
        loadingTitleText.startAnimation(fadeAnim);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                loadingTitleText.setVisibility(View.INVISIBLE);
                bgView.startAnimation(scale);
            }
        }, LOADING_ANIM_TIME);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();
                firstLoad = false;
            }
        }, LOADING_ANIM_TIME * 2);
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

    public void refreshCompleted() {
        swipeRefreshLayout.setRefreshing(false);
        refreshFab.clearAnimation();
        loading = true;
        refreshFab.setClickable(true);
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
