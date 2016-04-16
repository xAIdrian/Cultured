package com.androidtitan.hotspots.main.ui.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class NewsActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();

    private static final String SAVED_STATE_ARTICLE_LIST = "newsactivity.savedstatearticles";
    public static final String ARTICLE_EXTRA = "newsactivity.articleextra";

    private static final int LOADING_ANIM_TIME = 700;

    @Inject
    NewsPresenter presenter;

    private Handler handler;
    private Animation rotateAnim;
    private Animation fadeAnim;

    @Bind(R.id.colorBgView)
    View bgView;
    @Bind(R.id.culturedTitleTextView)
    TextView loadingTitleText;

    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.list)
    RecyclerView recyclerView;
    @Bind(R.id.refreshFab)
    FloatingActionButton refreshFab;

    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.drawer_navigation_view)
    NavigationView navigationView;

    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager staggeredLayoutManager;
    private NewsAdapter adapter;

    private boolean firstLoad = true; //used for animation
    private boolean loading = true;
    private int pastVisibleItems;
    private int visibleItemCount;
    private int totalItemCount;
    public int adapterLoadOffset = 6;

    int screenSize;
    public List<Article> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeTranstionsAndAnimations();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ButterKnife.bind(this);
        CulturedApp.getAppComponent().inject(this);
        presenter.takeActivity(this);

        refreshFab.hide();
        loadingTitleText.setVisibility(View.VISIBLE);
        initializeAnimation();

        //saveInstanceState to handle rotations
        if (savedInstanceState != null) {
            //
        }

        screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            articles = presenter.initialNewsQuery("world", 10);
        } else {
            articles = presenter.initialNewsQuery("world", 5);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();

                switch (item.getItemId()) {

                    case R.id.onboarding_card_generator:

                        if(!adapter.getSharedPreferences().getBoolean(adapter.PREFERENCES_SHOULD_ONBOARD , false)
                                && adapter.getAboutStatus() == false) {
                            adapter.resetOnboardingCard();
                        }


                        break;

                    case R.id.about_card_generator:

                        if(!adapter.getSharedPreferences().getBoolean(adapter.PREFERENCES_SHOULD_ONBOARD , false)
                                && adapter.getAboutStatus() == false) {
                            adapter.showAboutCard();
                        }

                        break;

                    default:

                        Log.e(TAG, "Incorrect navigation drawer item selected");
                }
                return true;
            }
        });

        initializeRecyclerView();

        refreshFab.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!adapter.getSharedPreferences().getBoolean(adapter.PREFERENCES_SHOULD_ONBOARD , false)
                        && adapter.getAboutStatus() == false) {
                    refreshFab.startAnimation(rotateAnim);
                    refreshForNewArticles();

                } else {

                    refreshCompleted();
                }
            }
        });


        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorCaribbean, R.color.colorCrush);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(!adapter.getSharedPreferences().getBoolean(adapter.PREFERENCES_SHOULD_ONBOARD , false)
                        && adapter.getAboutStatus() == false) {

                    refreshForNewArticles();

                } else {

                    refreshCompleted();
                }


            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrollViewParallax(dy);

                if (dy > 0) { //check for scroll

                    if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE ||
                            getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                        visibleItemCount = staggeredLayoutManager.getChildCount();
                        totalItemCount = staggeredLayoutManager.getItemCount();
                        int[] firstVisibleItems = null;
                        firstVisibleItems = staggeredLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);

                        if(firstVisibleItems != null && firstVisibleItems.length > 0) {
                            pastVisibleItems = firstVisibleItems[0];
                        }

                        if (loading) {

                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                loading = false;

                                swipeRefreshLayout.setRefreshing(true);
                                presenter.appendNewsQuery("world", 10, adapterLoadOffset);
                                adapterLoadOffset += 10;
                            }
                        }
                    } else {

                        visibleItemCount = linearLayoutManager.getChildCount();
                        totalItemCount = linearLayoutManager.getItemCount();
                        pastVisibleItems = linearLayoutManager.findFirstCompletelyVisibleItemPosition();

                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                            if (loading) {

                                if ((visibleItemCount + pastVisibleItems + 1) >= totalItemCount) {
                                    //the +1 accounts for having one less card visible to add

                                    refreshFab.setClickable(true);
                                    loading = false;
                                    Log.d(TAG, "appending data...");

                                    swipeRefreshLayout.setRefreshing(true);
                                    presenter.appendNewsQuery("world", 5, adapterLoadOffset);
                                    adapterLoadOffset += 5;
                                }
                            }

                        } else {

                            if (loading) {

                                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {

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

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {

            staggeredLayoutManager = new StaggeredGridLayoutManager(3, 1);
            recyclerView.setLayoutManager(staggeredLayoutManager);

        } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE ||
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            staggeredLayoutManager = new StaggeredGridLayoutManager(2, 1);
            recyclerView.setLayoutManager(staggeredLayoutManager);

        } else {

            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        adapter = new NewsAdapter(this, articles);
        recyclerView.setAdapter(adapter);
    }

    private void scrollViewParallax(int dy) { // divided by three to scroll slower
        bgView.setTranslationY(bgView.getTranslationY() - dy / 3);
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

    public void refreshForNewArticles() {

        presenter.newArticleRefresh("world");

    }

    public void updateNewsAdapter() {

        if (firstLoad) {

            firstLoadCompleteAnimation();
        } else {

            adapter.notifyDataSetChanged();
        }
    }

    public void appendAdapterItem(Article article) {
        adapter.appendToAdapter(article);
    }

    public void insertAdapterItem(int position, Article article) {
        adapter.insertToAdapter(position, article);
    }

    public void firstLoadCompleteAnimation() {

        final ScaleAnimation scale = new ScaleAnimation((float) 1.0, (float) 1.0, (float) 1.0, (float) 0.33);
        scale.setFillAfter(true);
        scale.setDuration(LOADING_ANIM_TIME);

        loadingTitleText.setVisibility(View.GONE);
        loadingTitleText.startAnimation(fadeAnim);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                bgView.startAnimation(scale);

            }
        }, LOADING_ANIM_TIME);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();
                firstLoad = false;
                refreshFab.show();
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

}
