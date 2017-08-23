package com.androidtitan.culturedapp.main.newsfeed.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.databinding.OfflineActivityBinding;

public class OfflineActivity extends AppCompatActivity {

    //todo: get our list of offline articles from {@link FileManager} and then tie it to our data binding
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_activity);
        OfflineActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.offline_activity);
        //binding.setArticle_one();
        //binding.setArticle_two();
        //binding.setArticle_three();
    }
}
