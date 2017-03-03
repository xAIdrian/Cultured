package com.androidtitan.culturedapp.main.newsfeed.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.model.newyorktimes.Article;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsDetailActivity extends AppCompatActivity {
    private static final String TAG = NewsDetailActivity.class.getSimpleName();

    private final static String SAVED_STATE_ARTICLE = "newsdetailactivity.savedstatearticle";

    @Bind(R.id.storyTextView)
    TextView storyText;

    private Article focusedArticle = null;

    //todo: null checks for our focusedArticle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_activity);

        ButterKnife.bind(this);

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

        if(focusedArticle != null) {
            String focusedString = focusedArticle.getUrl();
            if(focusedString != null && focusedString.length() > 0) {
                new ParseUrl().execute(focusedString); //.execute(new String[] {focusedString})  ???
            }
        } else {
            Log.e(TAG, "There is not a viable Article to supply a url");
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

    // handling our web scraping on a background thread
    private class ParseUrl extends AsyncTask<String, Integer, String> {

        StringBuilder sb;

        @Override
        protected void onPreExecute() {

            sb = new StringBuilder();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String urlParam = params[0];

            try {
                Document jsoupDocument = Jsoup.connect(urlParam).get();
                Log.d(TAG, "JSOUP established connection to " + urlParam);

                Element page = jsoupDocument.getElementsByTag("div").select("div#page").first();
                Element article = page.select("main#main").first().child(0);
                Elements storyBodies = article.getElementsByTag("div").select("div.story-body-supplemental");

                for(int i = 1; i <= storyBodies.size(); i++) {

                    Element storyBody = storyBodies.get(i - 1).getElementsByTag("div").get(1);
                    Elements storyBodyText = storyBody.select("p[class=story-body-text story-content]");

                    sb.append(storyBodyText.text());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return sb.toString();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {

            Log.e(TAG, "passing results to string");
            storyText.setText(s);
        }
    }
}
