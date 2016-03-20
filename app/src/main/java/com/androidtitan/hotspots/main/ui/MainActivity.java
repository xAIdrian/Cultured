package com.androidtitan.hotspots.main.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.common.BaseActivity;
import com.androidtitan.hotspots.main.model.Item;
import com.androidtitan.hotspots.main.presenter.DaggerPresenterComponent;
import com.androidtitan.hotspots.main.presenter.MainPresenter;
import com.androidtitan.hotspots.main.presenter.MainPresenterModule;
import com.androidtitan.hotspots.main.presenter.MainView;
import com.androidtitan.hotspots.main.presenter.PresenterComponent;
import com.androidtitan.hotspots.main.ui.adapter.ImageAdapter;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainView{
    private final String TAG = getClass().getSimpleName();

    private static PresenterComponent presenterComponent;

    @Inject
    MainPresenter presenter;

    private RecyclerView recyclerView;
    public ImageAdapter adapter;

    private List<Item> trackItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        implementComponents();
        //todo: this is going to be moved at some point as it will be Triggered
        trackItems = presenter.querySpotifyTracks("a", 20);

        initializeRecyclerView();
    }


    private void initializeRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ImageAdapter(this, trackItems);
        recyclerView.setAdapter(adapter);
    }

    public void implementComponents() {
        presenterComponent = DaggerPresenterComponent.builder()
                .mainPresenterModule(new MainPresenterModule(this, this)) //this can be removed
                .build();
        presenterComponent.inject(this);
    }

    public PresenterComponent getPresenterComponent() {
        return presenterComponent;
    }

    @Override
    public void updateImageAdapter() {
        adapter.notifyDataSetChanged();
    }
}
