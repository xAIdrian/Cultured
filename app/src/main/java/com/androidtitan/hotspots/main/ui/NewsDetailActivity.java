package com.androidtitan.hotspots.main.ui;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.main.model.newyorktimes.Article;
import com.androidtitan.hotspots.main.presenter.newsdetail.DaggerNewsDetailPresenterComponent;
import com.androidtitan.hotspots.main.presenter.newsdetail.NewsDetailModule;
import com.androidtitan.hotspots.main.presenter.newsdetail.NewsDetailPresenter;
import com.androidtitan.hotspots.main.presenter.newsdetail.NewsDetailPresenterComponent;
import com.androidtitan.hotspots.main.presenter.newsdetail.NewsDetailView;
import com.androidtitan.hotspots.main.util.HelperMethods;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsDetailActivity extends AppCompatActivity implements NewsDetailView{
    private final String TAG = getClass().getSimpleName();

    private final static String SAVED_STATE_ARTICLE = "newsdetailactivity.savedstatearticle";

    public static NewsDetailPresenterComponent newsDetailPresenterComponent;
    @Inject NewsDetailPresenter presenter;

    @Bind(R.id.collapse_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.bgheader) KenBurnsView articleImageView;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.webview) WebView articleWebView;

    private Handler handler;
    private Animation scale;

    private Article article;
    private Palette palette;

    private int[] dimensionArray = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeTranstions();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        if(savedInstanceState != null) {
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
        initializeAnimations();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*// /w/api.php?action=query&format=json&prop=revisions&rvprop=content&titles=georgia%7CMain+PAge
                presenter.queryWikipediaPage("query", "json", "revisions", "content", "Main%20page");

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
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

        outState.putParcelable(SAVED_STATE_ARTICLE, article);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initializeTranstions() {
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

    public void implementComponents() {

        newsDetailPresenterComponent = DaggerNewsDetailPresenterComponent.builder()
                .newsDetailModule(new NewsDetailModule(this, this))
                .build();
        newsDetailPresenterComponent.inject(this);
    }

    private void initializeToolbar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_news_48);
        fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        collapsingToolbar.setTitle(article.getGeoFacet().get(0));
    }

    private void initializeViewElements() {

        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        p.setAnchorId(R.id.collapse_toolbar);
        fab.setLayoutParams(p);
        fab.setVisibility(View.GONE);

        getHeaderImage(articleImageView);
        RandomTransitionGenerator generator = new RandomTransitionGenerator(25000, new LinearInterpolator());
        articleImageView.setTransitionGenerator(generator);

        articleWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                articleWebView.loadUrl(url);
                return true;
            }
        });
        articleWebView.getSettings().setLoadsImagesAutomatically(true);
        articleWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        articleWebView.getSettings().setJavaScriptEnabled(true);
        articleWebView.getSettings().setBuiltInZoomControls(true);
        articleWebView.loadUrl(presenter.formattedWikiUrl(article.getGeoFacet().get(0)));
    }

    private void initializeAnimations() {

        handler = new Handler();
        scale = AnimationUtils.loadAnimation(this, R.anim.scale);
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

        collapsingToolbar.setContentScrimColor(vibrantDarkColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));

        fab.setVisibility(View.VISIBLE);
        fab.startAnimation(scale);
    }
}
