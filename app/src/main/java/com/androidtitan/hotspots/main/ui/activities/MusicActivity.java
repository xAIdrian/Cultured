package com.androidtitan.hotspots.main.ui.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.common.BaseActivity;
import com.androidtitan.hotspots.main.model.spotify.Item;
import com.androidtitan.hotspots.main.presenter.music.DaggerMusicPresenterComponent;
import com.androidtitan.hotspots.main.presenter.music.MusicPresenter;
import com.androidtitan.hotspots.main.presenter.music.MusicPresenterComponent;
import com.androidtitan.hotspots.main.presenter.music.MusicPresenterModule;
import com.androidtitan.hotspots.main.presenter.music.MusicView;
import com.androidtitan.hotspots.main.ui.adapter.MusicAdapter;

import java.util.List;

import javax.inject.Inject;

public class MusicActivity extends BaseActivity implements MusicView {
    private final String TAG = getClass().getSimpleName();

    private static MusicPresenterComponent musicPresenterComponent;

    @Inject
    MusicPresenter presenter;

    private RecyclerView recyclerView;
    public MusicAdapter adapter;

    private RelativeLayout toolbarContainer;
    private TextView wikiInfoBar;
    private Toolbar toolbar;
    private View bgView;

    private List<Item> trackItems;
    private String geoString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeTranstions();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        if(getIntent().getExtras() != null) {
            geoString = getIntent().getStringExtra(NewsDetailActivity.NEWS_DETAIL_MUSIC_SEARCHER);
        }

        implementComponents();
        //todo: this is going to be moved at some point as it will be Triggered
        trackItems = presenter.querySpotifyTracks(
                geoString,
                20);

        setUpToolbar();
        initializeRecyclerView();

        toolbarContainer = (RelativeLayout) findViewById(R.id.toolbarContainer);
        wikiInfoBar = (TextView) findViewById(R.id.wikiInfoBar);
        bgView = (View) findViewById(R.id.colorBgView);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if(Math.abs(toolbarContainer.getTranslationY()) > toolbar.getHeight()) {
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
            getWindow().setEnterTransition(new Slide());
            getWindow().setExitTransition(new Slide());

        } else {
            // do something for phones running an API before lollipop
        }
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setTitle(getIntent().getStringExtra(geoString));
    }


    private void initializeRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MusicAdapter(this, trackItems);
        recyclerView.setAdapter(adapter);
    }

    public void implementComponents() {
        musicPresenterComponent = DaggerMusicPresenterComponent.builder()
                .musicPresenterModule(new MusicPresenterModule(this, this)) //this can be removed
                .build();
        musicPresenterComponent.inject(this);
    }

    public MusicPresenterComponent getPresenterComponent() {
        return musicPresenterComponent;
    }

    @Override
    public void updateImageAdapter() {
        adapter.notifyDataSetChanged();
    }
}
