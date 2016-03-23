package com.androidtitan.hotspots.main.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        implementComponents();
        //todo: this is going to be moved at some point as it will be Triggered
        trackItems = presenter.querySpotifyTracks("a", 20);

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

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_wikipedia_48);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setTitle(getResources().getString(R.string.tracks));
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
