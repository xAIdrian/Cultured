package com.androidtitan.culturedapp.main.newsfeed.ui;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.BaseActivity;
import com.androidtitan.culturedapp.main.newsfeed.NewsAdapter;
import com.androidtitan.culturedapp.main.newsfeed.NewsMvp;
import com.androidtitan.culturedapp.main.newsfeed.NewsPresenter;
import com.androidtitan.culturedapp.model.newyorktimes.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsActivity extends BaseActivity implements NewsMvp.View {
    private final String TAG = getClass().getSimpleName();

    private static final String SAVED_STATE_ARTICLE_LIST = "newsactivity.savedstatearticles";
    public static final String ARTICLE_EXTRA = "newsactivity.articleextra";

    private static final int LOADING_ANIM_TIME = 700;
    public static final String ERROR_MESSAGE = "errorfragment.errormessage";
    public static final String ERROR_MAP = "errorfragment.errormap";

    @Inject
    NewsPresenter presenter;

    private Handler handler;
    private Animation fadeAnim;

    Toolbar supportActionBar;

    @Bind(R.id.colorBgView)
    View bgView;
    @Bind(R.id.culturedTitleTextView)
    TextView loadingTitleText;

    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.list)
    RecyclerView recyclerView;

    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.drawer_navigation_view)
    NavigationView navigationView;

    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager staggeredLayoutManager;
    private NewsAdapter adapter;

    private AppBarLayout appBarLayout;

    List<Article> articles;

    private boolean firstLoad = true; //used for animation
    private boolean loading = true;
    private int pastVisibleItems;
    private int visibleItemCount;
    private int totalItemCount;
    public int adapterLoadOffset = 6;

    int screenSize;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeTranstionsAndAnimations();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ButterKnife.bind(this);
        super.getAppComponent().inject(this);
        presenter.bindView(this);

        articles = new ArrayList<>();

        loadingTitleText.setVisibility(View.VISIBLE);
        initializeAnimation();

        //saveInstanceState to handle rotations
        if (savedInstanceState != null) {
            //
        }

        supportActionBar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        setSupportActionBar(supportActionBar);
        getSupportActionBar().setTitle("");


        screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            articles = presenter.loadArticles(10);
        } else {
            articles = presenter.loadArticles(5);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();

                switch (item.getItemId()) {

                    case R.id.onboarding_card_generator:

                        if (!adapter.getSharedPreferences().getBoolean(adapter.PREFERENCES_SHOULD_ONBOARD, false)
                                && adapter.getAboutStatus() == false) {
                            adapter.resetOnboardingCard();
                        }


                        break;

                    case R.id.about_card_generator:

                        if (!adapter.getSharedPreferences().getBoolean(adapter.PREFERENCES_SHOULD_ONBOARD, false)
                                && adapter.getAboutStatus() == false) {
                            adapter.showAboutCard();
                        }

                        break;

                    case R.id.support_mail:

                        //todo: Implicit intent with picker to send email to adrian.mohnacs@gmail.com

                        Intent feedbackIntent = new Intent(Intent.ACTION_SEND);
                        feedbackIntent.setType("plain/text");
                        feedbackIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"adrian.mohnacs@gmail.com"});
                        feedbackIntent.putExtra(Intent.EXTRA_SUBJECT, "Cultured Feedback");
                        feedbackIntent.putExtra(Intent.EXTRA_TEXT, "Hi Adrian,\nHere\'s what I think about Cultured...");
                        //chooser
                        String title = getResources().getString(R.string.chooser_text);
                        Intent chooser = Intent.createChooser(feedbackIntent, title);
                        if (feedbackIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(chooser);
                        }

                        break;

                    default:

                        Log.e(TAG, "Incorrect navigation drawer item selected");
                }
                return true;
            }
        });

        initializeRecyclerView();



        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorCaribbean, R.color.colorCrush);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (!adapter.getSharedPreferences().getBoolean(adapter.PREFERENCES_SHOULD_ONBOARD, false)
                        && adapter.getAboutStatus() == false) {

                    refreshForNewArticles();

                } else {

                    onLoadComplete();
                }


            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrollViewParallax(dy);

                if (dy > 0) {
                    hideToolbarBy(dy);
                } else {
                    showToolbarBy(dy);
                }

                if (dy > 0) { //check for scroll


                    if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE ||
                            getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                        visibleItemCount = staggeredLayoutManager.getChildCount();
                        totalItemCount = staggeredLayoutManager.getItemCount();
                        int[] firstVisibleItems = null;
                        firstVisibleItems = staggeredLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);

                        if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                            pastVisibleItems = firstVisibleItems[0];
                        }

                        if (loading) {

                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                loading = false;

                                swipeRefreshLayout.setRefreshing(true);
                                presenter.loadOffsetArticles(10, adapterLoadOffset);
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

                                    loading = false;
                                    Log.d(TAG, "appending data...");

                                    swipeRefreshLayout.setRefreshing(true);
                                    presenter.loadOffsetArticles(5, adapterLoadOffset);
                                    adapterLoadOffset += 5;
                                }
                            }

                        } else {

                            if (loading) {

                                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {

                                    loading = false;
                                    Log.d(TAG, "appending data...");

                                    swipeRefreshLayout.setRefreshing(true);
                                    presenter.loadOffsetArticles(5, adapterLoadOffset);
                                    adapterLoadOffset += 5;
                                }
                            }
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unbindView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Save all appropriate fragment state.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //todo :: Let's refigure out what this does and decide if we want to keep it or not
        outState.putParcelableArrayList(SAVED_STATE_ARTICLE_LIST,
                (ArrayList<? extends Parcelable>) articles);
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


    @Override
    public void updateNewsAdapter() {

        adapter.notifyDataSetChanged();

    }


    @Override
    public void onLoadComplete() {

        if (firstLoad) {
            firstLoadCompleteAnimation();
        } else {
            onRefreshComplete();
        }

    }

    @Override
    public void appendAdapterItem(Article article) {
        adapter.appendToAdapter(article);
    }


    @Override
    public void insertAdapterItem(int index, Article article) {
        adapter.insertToAdapter(index, article);
    }

    @Override
    public List<Article> getArticles() {
        return articles;
    }

    @Override
    public void displayError(String message, Map<String, Object> additionalProperties) {

        ErrorFragment errorFrag = ErrorFragment.newInstance(message, additionalProperties);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.list, errorFrag);

    }

    private void onRefreshComplete() {
        swipeRefreshLayout.setRefreshing(false);
        loading = true;
    }

    public void initializeAnimation() {

        handler = new Handler();
        fadeAnim = AnimationUtils.loadAnimation(this, R.anim.fade_out);

    }

    public void refreshForNewArticles() {
        presenter.newArticleRefresh();

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
            }
        }, LOADING_ANIM_TIME * 2);
    }

    /*@TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
    }*/

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

//parallax animation methods
    private void showToolbarBy(int dy) {

        if (cannotShowMore(dy)) {
            appBarLayout.setTranslationY(0);
        } else {
            appBarLayout.setTranslationY(appBarLayout.getTranslationY()-dy);
        }

    }

    private void hideToolbarBy(int dy) {
        if (cannotHideMore(dy)) {
            appBarLayout.setTranslationY(-appBarLayout.getBottom());
        } else {
            appBarLayout.setTranslationY(appBarLayout.getTranslationY()-dy);
        }
    }

    private boolean cannotHideMore(int dy) {
        return Math.abs(appBarLayout.getTranslationY() - dy) > appBarLayout.getBottom();
    }

    private boolean cannotShowMore(int dy) {
        return appBarLayout.getTranslationY() - dy > 0;
    }



}
