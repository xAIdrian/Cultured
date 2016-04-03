package com.androidtitan.hotspots.main.ui.activities;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.main.model.newyorktimes.Article;
import com.androidtitan.hotspots.main.presenter.newsdetail.DaggerNewsDetailPresenterComponent;
import com.androidtitan.hotspots.main.presenter.newsdetail.NewsDetailModule;
import com.androidtitan.hotspots.main.presenter.newsdetail.NewsDetailPresenter;
import com.androidtitan.hotspots.main.presenter.newsdetail.NewsDetailPresenterComponent;
import com.androidtitan.hotspots.main.presenter.newsdetail.NewsDetailView;
import com.androidtitan.hotspots.main.ui.WikiFragment;
import com.androidtitan.hotspots.main.ui.ViewPagerAdapter;
import com.androidtitan.hotspots.main.util.HelperMethods;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsDetailActivity extends AppCompatActivity implements NewsDetailView{
    private final String TAG = getClass().getSimpleName();

    private final static String SAVED_STATE_ARTICLE = "newsdetailactivity.savedstatearticle";
    public final static String NEWS_DETAIL_MUSIC_SEARCHER = "newsdetailactivity.newsdetailmusicsearcher";
    public final static String NEWS_DETAIL_WIKI_URL = "newsdetailactivity.newsdetailwikiurl";

    public static NewsDetailPresenterComponent newsDetailPresenterComponent;
    @Inject
    NewsDetailPresenter presenter;

    @Bind(R.id.collapse_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.bgheader) KenBurnsView articleImageView;
    @Bind(R.id.articleTitleTextView) TextView articleTitleText;
    @Bind(R.id.tab_layout) TabLayout tabs;
    @Bind(R.id.nestedLayout)NestedScrollView scrollView;
    @Bind(R.id.pager) ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Bind(R.id.championFab) FloatingActionButton championFab;

    private Handler handler;
    private Animation scale;

    private Article article;

    private int[] dimensionArray = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeTranstions();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        //todo: we need to persist our WEBVIEW
        if (savedInstanceState != null) {
            article = savedInstanceState.getParcelable(SAVED_STATE_ARTICLE);

        } else {
            if (getIntent().getExtras() != null) {
                Bundle extras = getIntent().getExtras();

                article = (Article) extras.getParcelable(NewsActivity.ARTICLE_EXTRA);
            }
        }

        implementComponents();
        initializeToolbar();
        initializeViewElements();
        initializeViewPager();
        initializeAnimations();

        championFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        GoogleApiClient client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Save all appropriate fragment state.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(SAVED_STATE_ARTICLE, article);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.article_action:
                //todo:use implicit intent to send user to web browser
                break;

            default:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public NewsDetailPresenter getPresenter() {
        return presenter;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initializeTranstions() {
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            // inside your activity (if you did not enable transitions in your theme)
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            // set an exit transition
            getWindow().setEnterTransition(new Fade());
            getWindow().setExitTransition(new Slide());

        } else {
            // do something for phones running an API before lollipop
        }
    }

    public void implementComponents() {

        newsDetailPresenterComponent = DaggerNewsDetailPresenterComponent.builder()
                .newsDetailModule(new NewsDetailModule(this, this))
                .build();
        newsDetailPresenterComponent.inject(this);
    }

    private void initializeToolbar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        championFab.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        collapsingToolbar.setTitle(article.getGeoFacet().get(0));
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.transparent));


    }

    private void initializeViewElements() {

        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) championFab.getLayoutParams();
        p.setAnchorId(R.id.collapse_toolbar);
        championFab.setLayoutParams(p);
        championFab.setVisibility(View.GONE);

        getHeaderImage(articleImageView);
        RandomTransitionGenerator generator = new RandomTransitionGenerator(25000, new LinearInterpolator());
        articleImageView.setTransitionGenerator(generator);

        articleTitleText.setText(article.getAbstract());

    }

    private void initializeViewPager() {
        scrollView.setFillViewport(true);
        adapter = new ViewPagerAdapter(this, getSupportFragmentManager(), buildFragments(), buildTitles());
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
    }

    private ArrayList<WikiFragment> buildFragments() {

        ArrayList<WikiFragment> frags = new ArrayList<>();

        if(!article.getDesFacet().get(0).equals("")) {
            WikiFragment frag1 = new WikiFragment();
            Bundle bundle1 = new Bundle();
            bundle1.putString(NEWS_DETAIL_WIKI_URL,
                    article.getDesFacet().get(0));
            frag1.setArguments(bundle1);
            frags.add(new WikiFragment());
        }
        if(!article.getPerFacet().get(0).equals("")) {
            WikiFragment frag2 = new WikiFragment();
            Bundle bundle2 = new Bundle();
            String temp = presenter.formatPERUrl(article.getPerFacet().get(0));
            bundle2.putString(NEWS_DETAIL_WIKI_URL, temp);
            frag2.setArguments(bundle2);
            frags.add(new WikiFragment());
        }
        if(!article.getOrgFacet().get(0).equals("")) {
            WikiFragment frag3 = new WikiFragment();
            Bundle bundle3 = new Bundle();
            bundle3.putString(NEWS_DETAIL_WIKI_URL, article.getOrgFacet().get(0));
            frag3.setArguments(bundle3);
            frags.add(new WikiFragment());
        }
        if(!article.getGeoFacet().get(0).equals("")) {
            WikiFragment frag4 = new WikiFragment();
            Bundle bundle4 = new Bundle();
            bundle4.putString(NEWS_DETAIL_WIKI_URL,
                    presenter.formatGEOUrl(article.getGeoFacet().get(0)));
            frag4.setArguments(bundle4);
            frags.add(new WikiFragment());
        }

        return frags;
    }

    private ArrayList<String> buildTitles() {

        ArrayList<String> titles = new ArrayList<>();

        if(!article.getDesFacet().get(0).equals("")) {
            titles.add(article.getDesFacet().get(0));
        }
        if(!article.getPerFacet().get(0).equals("")) {
            titles.add(article.getPerFacet().get(0));
        }
        if(!article.getOrgFacet().get(0).equals("")) {
            titles.add(article.getOrgFacet().get(0));
        }
        if(!article.getGeoFacet().get(0).equals("")) {
            titles.add(article.getGeoFacet().get(0));
        }

        return titles;
    }

    private void initializeAnimations() {

        handler = new Handler();
        scale = AnimationUtils.loadAnimation(this, R.anim.scale);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void enterReveal(View view) {
        // previously invisible view

        // get the center for the clipping circle
        int cx = view.getMeasuredWidth() / 2;
        int cy = view.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(view.getWidth(), view.getHeight()) / 2;

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    private void getHeaderImage(final ImageView imageview) {

        ViewTreeObserver vto = imageview.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                //imageview.getViewTreeObserver().removeOnPreDrawListener(this);

                dimensionArray[0] = HelperMethods.convertPixtoDip(imageview.getMeasuredWidth());
                dimensionArray[1] = HelperMethods.convertPixtoDip(imageview.getMeasuredHeight());

                imageview.getViewTreeObserver().removeOnPreDrawListener(this);

                presenter.getHeaderImage(article.getMultimedia(), articleImageView,
                        dimensionArray[0], dimensionArray[1]);

                return true;
            }
        });
    }

    @Override
    public void onImageDownload(Palette palette) {
        //todo: you know...we could use BUTTERKNIFE to get our color resource...

        int vibrantColor = palette.getVibrantColor(
                ContextCompat.getColor(NewsDetailActivity.this, R.color.colorAccent));
        int vibrantDarkColor = palette.getDarkVibrantColor(
                ContextCompat.getColor(NewsDetailActivity.this, R.color.colorAccent));
        int mutedColor = palette.getMutedColor(
                ContextCompat.getColor(NewsDetailActivity.this, R.color.colorAccent));

        collapsingToolbar.setContentScrimColor(mutedColor);
        championFab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
        articleTitleText.setBackgroundColor(mutedColor);
        tabs.setBackgroundColor(mutedColor);

        championFab.setVisibility(View.VISIBLE);
        championFab.startAnimation(scale);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void startMusicActivity(String searcher) {
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            //Pair<View, String> pair = Pair.create((View) articleImage, getString(R.string.transition_news_image));
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);

            Intent intent = new Intent(this, MusicActivity.class);
            intent.putExtra(NEWS_DETAIL_MUSIC_SEARCHER, article.getGeoFacet().get(0));
            startActivity(intent, options.toBundle());
        } else {
            Intent intent = new Intent(this, MusicActivity.class);
            intent.putExtra(NEWS_DETAIL_MUSIC_SEARCHER, article.getGeoFacet().get(0));
            startActivity(intent);
        }
    }


}
