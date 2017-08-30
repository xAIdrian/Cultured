package com.androidtitan.culturedapp.main.offline;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.CollectionUtils;
import com.androidtitan.culturedapp.common.FileManager;
import com.androidtitan.culturedapp.databinding.ContentNoInternetBinding;
import com.androidtitan.culturedapp.model.newyorktimes.Article;

import java.util.ArrayList;

public class NoInternetActivity extends AppCompatActivity {
    private static final String TAG = NoInternetActivity.class.getSimpleName();

    private FileManager fileManager;

    private ArrayList<Article> offlineArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_internet_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fileManager = FileManager.getInstance(this);

        offlineArticles = fileManager.getInternalArticles();

        bindViews(offlineArticles);

    }

    /**
     * databinding
     */
    private void bindViews(ArrayList<Article> articles) {
        ContentNoInternetBinding superBinding = DataBindingUtil.setContentView(this, R.layout.content_no_internet);

        Log.d(TAG, "bindViews: " + articles.size());

        if(!CollectionUtils.isEmpty(articles)) {
            superBinding.setTitle(articles.get(0));
        }
    }
}
