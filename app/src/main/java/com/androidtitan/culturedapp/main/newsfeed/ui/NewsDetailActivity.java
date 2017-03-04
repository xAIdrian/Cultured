package com.androidtitan.culturedapp.main.newsfeed.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsDetailActivity extends AppCompatActivity {
    private static final String TAG = NewsDetailActivity.class.getSimpleName();

    private final static String SAVED_STATE_ARTICLE = "newsdetailactivity.savedstatearticle";
    private final static String SAVED_STATE_FACETS = "newsdetailactivity.savedstatefacets";

    @Bind(R.id.backgroundImageView)
    ImageView backgroundImage;
    @Bind(R.id.detailTitleTextView)
    TextView detailTitle;
    @Bind(R.id.geoFacetTitleTextView)
    TextView geoFacetTitle;
    @Bind(R.id.dateTextView)
    TextView dateText;
    @Bind(R.id.sourTextViewTextView)
    TextView sourText;
    @Bind(R.id.storyTextView)
    TextView storyText;

    private Animation fadeIn;

    private Article focusedArticle = null;
    private ArrayList focusedGeoFacets;

    //todo: null checks for our focusedArticle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_activity);

        ButterKnife.bind(this);

        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Bundle extras = getIntent().getExtras();
        if (savedInstanceState != null) {
            focusedArticle = savedInstanceState.getParcelable(SAVED_STATE_ARTICLE);
            focusedGeoFacets = savedInstanceState.getStringArrayList(SAVED_STATE_FACETS);
        } else {
            if (extras != null) {
                focusedArticle = extras.getParcelable(NewsActivity.ARTICLE_EXTRA);
                focusedGeoFacets = extras.getStringArrayList(NewsActivity.ARTICLE_GEO_FACETS);
            }
        }

        setUpArticleView(focusedArticle, focusedGeoFacets);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
        });

    }

    private void setUpArticleView(Article article, ArrayList<String> geoFacets) {

        if(article != null) {
            String focusedString = article.getUrl();

            if(focusedString != null && focusedString.length() > 0) {
                new ParseUrl().execute(focusedString);
            }

            int mediaSize = article.getMultimedia().size();
            if(mediaSize > 0) {
                String focusedImageUrl = article.getMultimedia().get(mediaSize - 1).getUrl();

                Glide.with(this).load(focusedImageUrl)
                    .crossFade()
                    .centerCrop()
                    .into(backgroundImage);
            }

            detailTitle.setText(article.getTitle());
            sourText.setText(article.getSource());

            DateFormat formatter = new SimpleDateFormat("MMM dd h:mm a");
            final String dateFormatted = formatter.format(article.getCreatedDate());
            dateText.setText(dateFormatted);

            String geoFacetString = "";

            if (geoFacets != null) {
                if(geoFacets.size() > 1) {

                    StringBuilder sb = new StringBuilder();

                    for(int i = 0; i < geoFacets.size(); i++) {
                        if(i == 0) {
                            sb.append(geoFacets.get(i));
                        } else {
                            sb.append(", " + geoFacets.get(i));
                        }
                    }
                    geoFacetString = sb.toString();

                } else if(geoFacets.size() == 1) {
                    geoFacetString = geoFacets.get(0);
                }

                geoFacetTitle.setText(geoFacetString);
            }

        } else {
            Log.e(TAG, "There is not a viable Article for supply");
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //todo: store focuedGeoFacets and focused article. Easy.  The code is already in place to retrieve them.
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

                org.jsoup.nodes.Element page = jsoupDocument.getElementsByTag("div").select("div#page").first();
                org.jsoup.nodes.Element article = page.select("main#main").first().child(0);
                Elements storyBodies =  article.getElementsByTag("div").select("div.story-body-supplemental");

                for(int i = 1; i <= storyBodies.size(); i++) {

                    org.jsoup.nodes.Element storyBody = storyBodies.get(i - 1).getElementsByTag("div").get(1);
                    Elements storyBodyText =  storyBody.select("p[class=story-body-text story-content]");

                    for(org.jsoup.nodes.Element ele : storyBodyText) {
                        sb.append("\n    " + ele.text() + "\n");
                    }
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

            Log.e(TAG, "passing results to ui thread");

            storyText.setText(s);
            storyText.startAnimation(fadeIn);
        }
    }

    private void blurDrawable() {


    }
}
