package com.androidtitan.culturedapp.main.newsfeed.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
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
import android.widget.TextView;


import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.structure.BaseActivity;
import com.androidtitan.culturedapp.main.firebase.PreferenceStore;
import com.androidtitan.culturedapp.main.newsfeed.NewsAdapter;
import com.androidtitan.culturedapp.main.newsfeed.NewsMvp;
import com.androidtitan.culturedapp.main.newsfeed.NewsPresenter;
import com.androidtitan.culturedapp.main.toparticle.TopArticleActivity;
import com.androidtitan.culturedapp.model.provider.wrappers.ArticleCursorWrapper;
import com.androidtitan.culturedapp.model.provider.DatabaseContract;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.iid.InstanceID;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.androidtitan.culturedapp.common.Constants.PREFERENCES_SYNCING_PERIODICALLY;

public class NewsActivity extends BaseActivity implements NewsMvp.View {
    private final String TAG = getClass().getSimpleName();

    private static final String SENDER_ID = "612691836045";
    public static final String ACCOUNT_TYPE = "com.androidtitan";
    public static final String ACCOUNT = "dummyaccount";
    // Sync interval constants
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 180L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE;

    private static final String SAVED_STATE_ARTICLE_LIST = "newsactivity.savedstatearticles";
    public static final String ARTICLE_EXTRA = "newsactivity.articleextra";

    private static final int LOADING_ANIM_TIME = 700;
    public static final String ERROR_MESSAGE = "errorfragment.errormessage";
    public static final String ERROR_MAP = "errorfragment.errormap";

    @Inject
    NewsPresenter presenter;

    private Handler handler;
    private Animation fadeAnim;
    private Account account;

    Toolbar supportActionBar;

    @Bind(R.id.colorBgView)
    View bgView;
    @Bind(R.id.culturedTitleTextView)
    TextView loadingTitleText;

    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.newsList)
    RecyclerView recyclerView;

    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.drawer_navigation_view)
    NavigationView navigationView;

    SharedPreferences sharedPreferences;

    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager staggeredLayoutManager;
    private NewsAdapter adapter;

    private AppBarLayout appBarLayout;

    List<Article> articles;

    private boolean isSyncingPeriodically;
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
        //initialize dummy account
        account = createSyncAccount(this);

        sharedPreferencesSetup();
        initFCM();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ButterKnife.bind(this);
        super.getAppComponent().inject(this);
        presenter.bindView(this);

        articles = new ArrayList<>();

        loadingTitleText.setVisibility(View.VISIBLE);
        loadingTitleText.setContentDescription(this.getResources().getString(R.string.accessability_loading));

        initializeAnimation();

        if (savedInstanceState != null) {
            //
        }
        // Attaching the layout to the toolbar object
        supportActionBar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        setSupportActionBar(supportActionBar);
        getSupportActionBar().setTitle("");


        screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            articles = presenter.loadArticles(10);
        } else {
            articles = presenter.loadArticles(5);
        }

        navigationView.setNavigationItemSelectedListener((item) -> {
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

                    //todo: Implicit intent with picker to send email to adrian.mohnacs@gmail.com
                    case R.id.support_mail:

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

    private void sharedPreferencesSetup() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        isSyncingPeriodically = sharedPreferences.getBoolean(PREFERENCES_SYNCING_PERIODICALLY, false);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(isSyncingPeriodically) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PREFERENCES_SYNCING_PERIODICALLY, false).apply();

            ContentResolver.removePeriodicSync(account, DatabaseContract.AUTHORITY, Bundle.EMPTY);
        }
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

        switch (item.getItemId()) {
            case R.id.menu_item_toparticle:

                startActivity(new Intent(this, TopArticleActivity.class));

                break;

            case R.id.menu_item_graph:

                break;

            case R.id.menu_item_facets:

//                Used to print what is held in our content provider

                Cursor articleCursor = getContentResolver().query(
                        DatabaseContract.ArticleTable.CONTENT_URI, null, null, null, null
                );
                ArticleCursorWrapper wrapper = new ArticleCursorWrapper(articleCursor);
                wrapper.moveToFirst();

                List<Article> articles = new ArrayList<Article>();

                Log.e(TAG, "retrieving from Content Provider...");
                while(!wrapper.isAfterLast()) {

                    Log.e(TAG, wrapper.getArticle().getTitle());
                    articles.add(wrapper.getArticle());

                    wrapper.moveToNext();
                }

                break;

            default:
                throw new IllegalArgumentException("Invalid options item: " + item.getItemId());

        }

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

        //Let's decide if we want to keep it or not
        outState.putParcelableArrayList(SAVED_STATE_ARTICLE_LIST,
                (ArrayList<? extends Parcelable>) articles);
    }

    private Account createSyncAccount(Context context) {

        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);

        if(accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
        return newAccount; //todo: is this the proper object to return?

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
                .add(R.id.newsList, errorFrag);

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

    /*
        Will this ever come back?

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startDetailActivity(ArticleTable article, ImageView articleImage) {
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

    private void initFCM() {
        PreferenceStore preferenceStore = PreferenceStore.get(this);
        String currentToken = preferenceStore.getFcmToken();

        if(currentToken == null) {
            new FCMRegistrationTask().execute();
        } else {
            Log.d(TAG, "Have token: " + currentToken);

            if(!isSyncingPeriodically) {
                setupPeriodicSync();
            }
        }
    }

    private class FCMRegistrationTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            if(NewsActivity.this == null) {
                return null;
            }

            int googleApiAvailable = GoogleApiAvailability.getInstance()
                    .isGooglePlayServicesAvailable(NewsActivity.this);
            if (googleApiAvailable != ConnectionResult.SUCCESS) {
                Log.e(TAG, "Play services not available, cannot register for GCM");
                return null;
            }

            InstanceID instanceID = InstanceID.getInstance(NewsActivity.this);

            try {
                String token = instanceID.getToken(SENDER_ID, FirebaseMessaging.INSTANCE_ID_SCOPE, null);
                Log.d(TAG, "Got token: " + token);
                return token;

            } catch (IOException e) {
                Log.e(TAG, "Failed to get token from InstanceID", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String token) {
            super.onPostExecute(token);

            if (token == null) {
                setupPeriodicSync();
            } else {
                PreferenceStore.get(NewsActivity.this).setFcmToken(token);
            }
        }
    }

    private void setupPeriodicSync() {
        isSyncingPeriodically = true;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFERENCES_SYNCING_PERIODICALLY, true).apply();

        ContentResolver.setIsSyncable(account, DatabaseContract.AUTHORITY, 1);
        ContentResolver.setSyncAutomatically(
                account, DatabaseContract.AUTHORITY, true);
        ContentResolver.addPeriodicSync(
                account, DatabaseContract.AUTHORITY, Bundle.EMPTY, 300);

        //ensure we have data for the initial viewing of pages
        ContentResolver.requestSync(account, DatabaseContract.AUTHORITY, Bundle.EMPTY);

    }


}
