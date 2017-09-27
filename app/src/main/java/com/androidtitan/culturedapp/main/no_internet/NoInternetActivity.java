package com.androidtitan.culturedapp.main.no_internet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.FileManager;
import com.androidtitan.culturedapp.model.newyorktimes.Article;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NoInternetActivity extends AppCompatActivity {
    private static final String TAG = NoInternetActivity.class.getSimpleName();

    private FileManager fileManager;

    @Bind(R.id.binding_recyclerview)
    RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;
    private NoInternetAdapter noInternetAdapter;

    private ArrayList<Article> offlineArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_internet_activity);
        ButterKnife.bind(this);

        fileManager = FileManager.getInstance(this);
        offlineArticles = fileManager.getInternalArticles();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        noInternetAdapter = new NoInternetAdapter(this, offlineArticles);
        recyclerView.setAdapter(noInternetAdapter);
    }
}
