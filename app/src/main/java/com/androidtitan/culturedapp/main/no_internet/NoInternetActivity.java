package com.androidtitan.culturedapp.main.no_internet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.FileManager;
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




    }

    //how are we going to feed into our custom setter
    //how are we going to populate our listview
}
