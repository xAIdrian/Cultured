package com.androidtitan.culturedapp.main.newsfeed.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.FileManager;
import com.androidtitan.culturedapp.databinding.OfflineActivityBinding;
import com.androidtitan.culturedapp.model.newyorktimes.Article;

import java.util.ArrayList;

public class OfflineActivity extends AppCompatActivity {

    private FileManager fileManager;

    private ArrayList<Article> offlineArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_activity);

        fileManager = FileManager.getInstance(this);

        offlineArticles = fileManager.getInternalArticles();

        OfflineActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.offline_activity);
        //OfflineActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.offline_activity);

        binding.setArticle_one(offlineArticles.get(0));
        binding.setArticle_two(offlineArticles.get(1));
        binding.setArticle_three(offlineArticles.get(2));

    }
}
