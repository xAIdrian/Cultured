package com.androidtitan.hotspots.main.ui;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.main.model.newyorktimes.Article;
import com.androidtitan.hotspots.main.presenter.newsdetail.DaggerNewsDetailPresenterComponent;
import com.androidtitan.hotspots.main.presenter.newsdetail.NewsDetailModule;
import com.androidtitan.hotspots.main.presenter.newsdetail.NewsDetailPresenter;
import com.androidtitan.hotspots.main.presenter.newsdetail.NewsDetailPresenterComponent;
import com.androidtitan.hotspots.main.presenter.newsdetail.NewsDetailView;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsDetailActivity extends AppCompatActivity implements NewsDetailView{
    private final String TAG = getClass().getSimpleName();

    public static NewsDetailPresenterComponent newsDetailPresenterComponent;
    @Inject NewsDetailPresenter presenter;

    @Bind(R.id.collapse_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.bgheader) KenBurnsView articleImageView;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.webview) WebView articleWebView;

    private Article article;
    private Palette palette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeTranstions();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        if(getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();

            article = (Article) extras.getSerializable(NewsActivity.ARTICLE_EXTRA);
        }

        implementComponents();
        initializeToolbar();
        initializeViewElements();

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

        presenter.getHeaderImage(article.getMultimedia(), articleImageView);
        RandomTransitionGenerator generator = new RandomTransitionGenerator(20000, new LinearInterpolator());
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

    @Override
    public void onImageDownload(Palette palette) {
        //todo: you know...we could use BUTTERKNIFE to get our color resource...
        int vibrantColor = palette.getVibrantColor(
                ContextCompat.getColor(NewsDetailActivity.this, R.color.colorAccent));
        int vibrantDarkColor = palette.getDarkVibrantColor(
                ContextCompat.getColor(NewsDetailActivity.this, R.color.colorAccent));

        collapsingToolbar.setContentScrimColor(vibrantDarkColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
    }
}
