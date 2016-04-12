package com.androidtitan.hotspots.main.ui.activities;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.common.BaseActivity;
import com.androidtitan.hotspots.main.CulturedApp;
import com.androidtitan.hotspots.main.model.newyorktimes.Article;
import com.androidtitan.hotspots.main.presenter.newsdetail.NewsDetailPresenter;
import com.androidtitan.hotspots.main.ui.WikiFragScrollInterface;
import com.androidtitan.hotspots.main.ui.adapter.ViewPagerAdapter;
import com.androidtitan.hotspots.main.ui.fragments.WikiFragment;
import com.androidtitan.hotspots.main.util.Utils;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsDetailActivity extends BaseActivity implements WikiFragScrollInterface{
    private final String TAG = getClass().getSimpleName();

    private final static String SAVED_STATE_ARTICLE = "newsdetailactivity.savedstatearticle";
    public final static String NEWS_DETAIL_MUSIC_SEARCHER = "newsdetailactivity.newsdetailmusicsearcher";
    public final static String NEWS_DETAIL_WIKI_URL = "newsdetailactivity.newsdetailwikiurl";

    public static final int MUSIC_ACTIVITY_FOR_RESULT = 1;

    @Inject
    NewsDetailPresenter presenter;

    @Bind(R.id.collapse_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.bgheader) KenBurnsView articleImageView;
    @Bind(R.id.articleTitleTextView) TextView articleTitleText;
    @Bind(R.id.tab_layout) TabLayout tabs;
    @Bind(R.id.pager) ViewPager viewPager;
    private ViewPagerAdapter adapter;

    WikiFragment fragment;

    private Handler handler;
    private Animation scale;

    private Article article;

    private String geoFacet;
    private int[] dimensionArray = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeTranstions();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        CulturedApp.getAppComponent().inject(this);
        presenter.takeActivity(this);

        if (savedInstanceState != null) {
            article = savedInstanceState.getParcelable(SAVED_STATE_ARTICLE);

        } else {
            if (getIntent().getExtras() != null) {
                Bundle extras = getIntent().getExtras();

                article = (Article) extras.getParcelable(NewsActivity.ARTICLE_EXTRA);
            }
        }
        geoFacet = article.getGeoFacet().get(0);

        initializeToolbar();
        initializeViewElements();
        initializeViewPager();
        initializeAnimations();


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

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.article_action:
                //todo:use implicit intent to send user to web browser
                break;

            default:
                return super.onOptionsItemSelected(item);

        }

        return false;

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

    private void initializeToolbar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar.setTitle(geoFacet); //todo: start activity for result
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.transparent));


    }

    private void initializeViewElements() {

        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        getHeaderImage(articleImageView);
        RandomTransitionGenerator generator = new RandomTransitionGenerator(25000, new LinearInterpolator());
        articleImageView.setTransitionGenerator(generator);

        articleTitleText.setText(article.getAbstract());

    }

    private void initializeViewPager() {
        //scrollView.setFillViewport(true);

        adapter = new ViewPagerAdapter(this, getSupportFragmentManager(), buildFragments(), buildTitles());
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
    }

    private ArrayList<WikiFragment> buildFragments() {

        ArrayList<WikiFragment> frags = new ArrayList<>();

        if(!article.getDesFacet().get(0).equals("")) {
            frags.add(WikiFragment.newInstance(presenter.formatDESUrl(article.getDesFacet().get(0))));
        }
        if(!article.getPerFacet().get(0).equals("")) {
            frags.add(WikiFragment.newInstance(presenter.formatPERUrl(article.getPerFacet().get(0))));
        }
        if(!article.getOrgFacet().get(0).equals("")) {
            frags.add(WikiFragment.newInstance("https://en.m.wikipedia.org/wiki/" + article.getOrgFacet().get(0)));
        }
        if(!article.getGeoFacet().get(0).equals("")) {
            frags.add(WikiFragment.newInstance(presenter.formatGEOUrl(article.getGeoFacet().get(0))));
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
    }

    @Override
    public void scrollViewParallax(int dy) { // divided by three to scroll slower
        articleTitleText.setTranslationY(articleTitleText.getTranslationY() + dy / 5);
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

                dimensionArray[0] = Utils.convertPixtoDip(imageview.getMeasuredWidth());
                dimensionArray[1] = Utils.convertPixtoDip(imageview.getMeasuredHeight());

                imageview.getViewTreeObserver().removeOnPreDrawListener(this);

                presenter.getHeaderImage(article.getMultimedia(), articleImageView,
                        dimensionArray[0], dimensionArray[1]);

                return true;
            }
        });
    }

    public void onImageDownload(Palette palette) {
        //todo: you know...we could use BUTTERKNIFE to get our color resource...

        int vibrantColor = palette.getVibrantColor(
                ContextCompat.getColor(NewsDetailActivity.this, R.color.colorAccent));
        int vibrantDarkColor = palette.getDarkVibrantColor(
                ContextCompat.getColor(NewsDetailActivity.this, R.color.colorAccent));
        int mutedColor = palette.getMutedColor(
                ContextCompat.getColor(NewsDetailActivity.this, R.color.colorAccent));

        collapsingToolbar.setContentScrimColor(mutedColor);
        articleTitleText.setBackgroundColor(vibrantColor);
        tabs.setBackgroundColor(mutedColor);

    }
}
