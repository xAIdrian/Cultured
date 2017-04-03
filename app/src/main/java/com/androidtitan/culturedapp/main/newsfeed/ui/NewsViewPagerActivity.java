package com.androidtitan.culturedapp.main.newsfeed.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.Toast;


import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.structure.BaseActivity;
import com.androidtitan.culturedapp.main.firebase.PreferenceStore;
import com.androidtitan.culturedapp.main.newsfeed.NewsFeedPresenter;
import com.androidtitan.culturedapp.main.preferences.PreferencesActivity;
import com.androidtitan.culturedapp.main.provider.FCMRegistrationTask;
import com.androidtitan.culturedapp.main.toparticle.ui.TopArticleActivity;
import com.androidtitan.culturedapp.main.provider.DatabaseContract;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.androidtitan.culturedapp.common.Constants.PREFERENCES_APP_FIRST_RUN;
import static com.androidtitan.culturedapp.common.Constants.PREFERENCES_SYNCING_PERIODICALLY;

public class NewsViewPagerActivity extends BaseActivity implements ActivityUserInterfaceInteractor,
        FCMRegistrationTask.FCMRegistratorCallback {
    private final String TAG = getClass().getSimpleName();

    public static final String ARTICLE_GEO_FACETS = "newsActivity.article_geo_facets";

    public static final String ARTICLE_BOOKMARKED = "newsActivity.article_bookmarked";


    public static final String ACCOUNT_TYPE = "com.androidtitan";

    public static final String ACCOUNT = "dummyaccount";


//todo: move this to the offline frgment
    private static final String SAVED_STATE_ARTICLE_LIST = "newsactivity.savedstatearticles";

    public static final String ARTICLE_EXTRA = "newsactivity.articleextra";

    private static final int LOADING_ANIM_TIME = 700;

    NewsFeedFragment newsFeedFragment;

    @Inject
    NewsFeedPresenter presenter;

    private Account account;

    Toolbar supportActionBar;

    ActionBarDrawerToggle drawerToggle;

    @Bind(R.id.colorBgView)
    View bgView;

    @Bind(R.id.loadingTextView)
    TextView loadingTitleText;

    @Bind(R.id.welcomeTextView)
    TextView welcomeText;

    @Bind(R.id.refreshFloatingActionButton)
    FloatingActionButton refreshFab;

    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @Bind(R.id.drawer_navigation_view)
    NavigationView navigationView;

    @Bind(R.id.navigation_icon)
    ImageView navImage;

    @Bind(R.id.main_content)
    CoordinatorLayout baseLayout;

    SharedPreferences sharedPreferences;

    private AppBarLayout appBarLayout;

    private boolean isSyncingPeriodically;

    private boolean firstLoad = true; //used for animation

    private int devConsoleCount = 0;

    int screenSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeTransitionsAndAnimations();
        //initialize dummy account
        account = createSyncAccount(this);

        sharedPreferencesSetup();
        initFCM();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsfeed_activity);

        ButterKnife.bind(this);
        super.getAppComponent().inject(this);

        setupViewPager();

        setupUserPreferences();


        loadingTitleText.setVisibility(View.VISIBLE);
        loadingTitleText.setContentDescription(this.getResources().getString(R.string.accessability_loading));

        if (savedInstanceState != null) {
            //
        }
        if (getIntent() != null) {
            Intent in = getIntent();
            Uri deepLinkData = in.getData();

            //receives 'http://www.cultured.com because that is the URL(URI) data required to launch our app from search
        }
        setUpActionBar();

        screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        navImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
            }
        });

        navigationView.getHeaderView(0).setOnClickListener((header) -> {

            if(devConsoleCount == 6) {
                if(!navigationView.getMenu().getItem(3).isVisible()) {
                    Toast.makeText(this, R.string.prime_dev_console_text, Toast.LENGTH_SHORT).show();
                    navigationView.getMenu().getItem(3).setVisible(true);
                } else {
                    Toast.makeText(this, R.string.bye_dev_console, Toast.LENGTH_SHORT).show();
                    navigationView.getMenu().getItem(3).setVisible(false);
                }

                devConsoleCount = 0;
            } else {
                devConsoleCount ++;
            }
        });

        navigationView.setNavigationItemSelectedListener((item) -> {
            drawerLayout.closeDrawers();

            switch (item.getItemId()) {

                case R.id.onboarding_card_generator:

                    if (!sharedPreferences.getBoolean(PREFERENCES_APP_FIRST_RUN, false)
                        && newsFeedFragment.getAboutCardStatus() == false) {
                        newsFeedFragment.resetOnboardingCard();
                    }

                    break;

                case R.id.about_card_generator:

                    if (!sharedPreferences.getBoolean(PREFERENCES_APP_FIRST_RUN, false)
                        && newsFeedFragment.getAboutCardStatus() == false) {
                        newsFeedFragment.showAboutCard();
                    }

                    break;

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

                    case R.id.settings:

                        startActivity(new Intent(this, PreferencesActivity.class));

                        break;

                    case R.id.devConsole:
                        //display a dialog fragment
                        showDialog();

                    default:

                    Log.e(TAG, "Incorrect navigation drawer item selected");
            }
            return true;
        });




        refreshFab.setOnClickListener(v -> {
            presenter.newsArticlesRefresh(newsFeedFragment.getArticles(), 5);
            newsFeedFragment.setLoadingStatus(true);
            showColoredSnackbar();
        });

    }

    private void setupViewPager() {
        //todo: Instantiate fragments and set the ViewPager adapter
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setUpActionBar() {
        // Attaching the layout to the toolbar object
        supportActionBar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        setSupportActionBar(supportActionBar);
        getSupportActionBar().setTitle("");
        drawerToggle = new ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.drawer_open,
            R.string.drawer_closed
        ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        // Set the drawer toggle as the DrawerListener
        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setWelcomeText();
    }

    private void showDialog() {
        DialogFragment newFragment = DevConsoleDialogFragment.newInstance();
        newFragment.show(getSupportFragmentManager(), "DevConsoleDialogFragment");
    }

    private void sharedPreferencesSetup() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        isSyncingPeriodically = sharedPreferences.getBoolean(PREFERENCES_SYNCING_PERIODICALLY, false);
    }

    private void setupUserPreferences() {

        setWelcomeText();

        String continentString = getString(R.string.pref_key_continent);
        switch (sharedPreferences.getString(continentString, "")) {
            case "North America":

                break;

            case "South America":

                break;

            default:
//                throw new IllegalArgumentException("Invalid case for switch statement");
        }
    }

    private void setWelcomeText() {
        String userName = sharedPreferences.getString(getString(R.string.pref_key_name), "");
        welcomeText.setText("Welcome " + userName);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (isSyncingPeriodically) {
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
        inflater.inflate(R.menu.news_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_toparticle:

                startActivity(new Intent(this, TopArticleActivity.class));

                break;

            default:
                throw new IllegalArgumentException("Invalid options item: " + item.getItemId());

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Save all appropriate fragment states.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //todo: making a database call on rotation change is bad practice.  Let's save this data using a parcible G
        //Let's decide if we want to keep it or not
//        outState.putParcelableArrayList(SAVED_STATE_ARTICLE_LIST,
//                (ArrayList<? extends Parcelable>) articles);
    }

    private Account createSyncAccount(Context context) {

        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);

        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
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
        return newAccount;

    }

    @Override
    public void onLoadComplete() {

        if (firstLoad) {
            firstLoadCompleteAnimation();
        } else {
            newsFeedFragment.setLoadingStatus(true);
        }

    }

    @Override
    public void scrollViewParallax(int dy) { // divided by three to scroll slower
        bgView.setTranslationY(bgView.getTranslationY() - dy / 3);
    }

    public void firstLoadCompleteAnimation() {

        Handler handler = new Handler();
        Animation fadeAnim = AnimationUtils.loadAnimation(this, R.anim.fade_out);

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

                newsFeedFragment.notifyDataSetChanged();
                firstLoad = false;
            }
        }, LOADING_ANIM_TIME * 2);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initializeTransitionsAndAnimations() {
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

    @Override
    public void showToolbarBy(int dy) {

        if (cannotShowMore(dy)) {
            appBarLayout.setTranslationY(0);

        } else {
            appBarLayout.setTranslationY(appBarLayout.getTranslationY() - dy);

            if (dy < 0) {
                appBarLayout.setElevation(8);
            } else {
                appBarLayout.setElevation(0);
            }
        }

    }

    @Override
    public void hideToolbarBy(int dy) {
        if (cannotHideMore(dy)) {
            appBarLayout.setTranslationY(-appBarLayout.getBottom());
        } else {
            appBarLayout.setTranslationY(appBarLayout.getTranslationY() - dy);
        }
    }

    @Override
    public boolean cannotHideMore(int dy) {
        return Math.abs(appBarLayout.getTranslationY() - dy) > appBarLayout.getBottom();
    }

    @Override
    public boolean cannotShowMore(int dy) {
        return appBarLayout.getTranslationY() - dy > 0;
    }

    private void initFCM() {
        PreferenceStore preferenceStore = PreferenceStore.get(this);
        String currentToken = preferenceStore.getFcmToken();

        if (currentToken == null) {
            new FCMRegistrationTask(this, sharedPreferences, account, isSyncingPeriodically).execute();
        } else {
            Log.d(TAG, "Have token: " + currentToken);

            if (!isSyncingPeriodically) {
                new FCMRegistrationTask(this, sharedPreferences, account, isSyncingPeriodically).setupPeriodicSync();
            }
        }
    }

    @Override
    public void showColoredSnackbar() {
        Snackbar loadingSnackbar = Snackbar.make(baseLayout, //this param is view todo: make this the fragments parent view
            getResources().getString(R.string.simple_loading),
            Snackbar.LENGTH_LONG);
        View snackbarView = loadingSnackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
        loadingSnackbar.show();
    }

    @Override
    public void setAppBarElevation(int ele) {
        appBarLayout.setElevation(ele);
    }

    public NewsFeedPresenter getPresenter() {
        return presenter;
    }


    @Override
    public void updateSyncingStatus(boolean syncStatus) {
        isSyncingPeriodically = syncStatus;
    }

    public int getScreenSize() {
        return screenSize;
    }
}
