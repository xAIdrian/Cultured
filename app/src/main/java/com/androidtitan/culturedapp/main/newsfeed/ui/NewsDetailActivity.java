package com.androidtitan.culturedapp.main.newsfeed.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.model.newyorktimes.Article;

public class NewsDetailActivity extends AppCompatActivity {
    private static final String TAG = NewsDetailActivity.class.getSimpleName();

    private final static String SAVED_STATE_ARTICLE = "newsdetailactivity.savedstatearticle";

    private Article focusedArticle = null;

    //todo: null checks for our focusedArticle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (savedInstanceState != null) {
            focusedArticle = savedInstanceState.getParcelable(SAVED_STATE_ARTICLE);
        } else {
            if (extras != null) {
                focusedArticle = (Article) extras.getParcelable(NewsActivity.ARTICLE_EXTRA);
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
