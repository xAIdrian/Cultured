package com.androidtitan.hotspots.main.ui.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.common.BaseActivity;
import com.androidtitan.hotspots.main.CulturedApp;
import com.androidtitan.hotspots.main.model.spotify.Item;
import com.androidtitan.hotspots.main.presenter.music.MusicPresenter;
import com.androidtitan.hotspots.main.ui.adapter.MusicAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MusicActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();

    @Inject
    MusicPresenter presenter;

    @Bind(R.id.list) RecyclerView recyclerView;
    public MusicAdapter adapter;

    @Bind(R.id.toolbarContainer) RelativeLayout toolbarContainer;
    @Bind(R.id.wikiInfoBar) TextView wikiInfoBar;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.colorBgView) View bgView;

    private List<Item> trackItems;
    private String geoString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeTranstions();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);
        CulturedApp.getAppComponent().inject(this);
        presenter.takeActivity(this);

        if(getIntent().getExtras() != null) {
            geoString = getIntent().getStringExtra(NewsDetailActivity.NEWS_DETAIL_MUSIC_SEARCHER);
        }

        trackItems = presenter.querySpotifyTracks(
                geoString,
                20);

        setUpToolbar();
        initializeRecyclerView();

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (Math.abs(toolbarContainer.getTranslationY()) > toolbar.getHeight()) {
                        hideToolbar();
                    } else {
                        showToolbar();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrollViewParallax(dy);

                if (dy > 0) {
                    hideToolBarBy(dy);
                } else {
                    showToolBarBy(dy);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Log.e(TAG, "home");
                setResult(Activity.RESULT_OK, new Intent()
                        .putExtra(NewsDetailActivity.NEWS_DETAIL_MUSIC_SEARCHER, geoString));
                this.finish();
                //NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showToolbar() {
        toolbarContainer
                .animate()
                .translationY(0)
                .start();
    }

    private void hideToolbar() {
        toolbarContainer
                .animate()
                .translationY((-wikiInfoBar.getBottom()))
                .start();
    }

    private void scrollViewParallax(int dy) { // divided by three to scroll slower
        bgView.setTranslationY(bgView.getTranslationY() - dy / 3);
    }


    private void hideToolBarBy(int dy) {
       if(cannotHideMore(dy)) {
            toolbarContainer.setTranslationY(-wikiInfoBar.getBottom());
        } else {
            toolbarContainer.setTranslationY(toolbarContainer.getTranslationY() - dy);
        }
    }

    private boolean cannotHideMore(int dy) {
        return Math.abs(toolbarContainer.getTranslationY() - dy) > wikiInfoBar.getBottom();
    }

    private void showToolBarBy(int dy) {
        if(cannotShowMore(dy)) {
            toolbarContainer.setTranslationY(0);
        } else {
            toolbarContainer.setTranslationY(toolbarContainer.getTranslationY() - dy);
        }
    }

    private boolean cannotShowMore(int dy) {
        return toolbarContainer.getTranslationY() - dy > 0;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initializeTranstions() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
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

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setTitle(getIntent().getStringExtra(geoString));
    }

    private void initializeRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MusicAdapter(this, trackItems);
        recyclerView.setAdapter(adapter);
    }

    public void updateImageAdapter() {
        adapter.notifyDataSetChanged();
    }


}
