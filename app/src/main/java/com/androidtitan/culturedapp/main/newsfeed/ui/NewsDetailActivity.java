package com.androidtitan.culturedapp.main.newsfeed.ui;

import com.google.gson.Gson;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.Constants;
import com.androidtitan.culturedapp.common.FileManager;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Multimedium;
import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.androidtitan.culturedapp.common.Constants.ARTICLE_BOOKMARKED;
import static com.androidtitan.culturedapp.common.Constants.ARTICLE_EXTRA;
import static com.androidtitan.culturedapp.common.Constants.ARTICLE_GEO_FACETS;

public class NewsDetailActivity extends AppCompatActivity implements FileManager.FileCallback {

    private static final String TAG = NewsDetailActivity.class.getSimpleName();

    private final static String SAVED_STATE_ARTICLE = "newsdetailactivity.savedstatearticle";
    private final static String SAVED_STATE_FACETS = "newsdetailactivity.savedstatefacets";
    private final static String SAVED_BOOKMARK_STATUS = "newsdetailactivity.savedbookmarkstatus";
    public final static String SAVED_MULTIMEDIA = "newsdetailactivity.savedmultimedia";


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

    @Bind(R.id.fab)
    FloatingActionButton fab;

    private FileManager fileManager;

    private Animation fadeIn;

    private Article focusedArticle = null;

    private Multimedium focusedImage;

    private ArrayList<String> focusedGeoFacets;

    private View rootViewGroup;

    private boolean isBookmarked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_activity);

        ButterKnife.bind(this);

        fileManager = new FileManager(this);

        rootViewGroup = ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Bundle extras = getIntent().getExtras();
        if (savedInstanceState != null) {
            focusedArticle = savedInstanceState.getParcelable(SAVED_STATE_ARTICLE);
            focusedGeoFacets = savedInstanceState.getStringArrayList(SAVED_STATE_FACETS);

            if (savedInstanceState.getBoolean(SAVED_BOOKMARK_STATUS)) {
                isBookmarked = true;
            }

        } else {
            if (extras != null) {
                buildArticleObject(extras);
            }
        }

        fab.setOnClickListener(view -> {

            fileManager.writeArticleToFile(this, focusedArticle);

        });

    }

    private void buildArticleObject(Bundle extras) {
        focusedArticle = extras.getParcelable(ARTICLE_EXTRA);
        focusedGeoFacets = extras.getStringArrayList(ARTICLE_GEO_FACETS);

        Gson gson = new Gson();
        focusedImage = gson.fromJson(extras.getString(SAVED_MULTIMEDIA), Multimedium.class);
        addImageToArticle(focusedImage);

        if (extras.getBoolean(ARTICLE_BOOKMARKED)) {
            isBookmarked = true;
        }
    }

    private void addImageToArticle(Multimedium focusedImage) {

        List<Multimedium> arr = new ArrayList<>();
        arr.add(focusedImage);
        focusedArticle.setMultimedia(arr);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(SAVED_STATE_ARTICLE, focusedArticle);
        outState.putStringArrayList(SAVED_STATE_FACETS, focusedGeoFacets);
        outState.putBoolean(SAVED_BOOKMARK_STATUS, isBookmarked);
    }

    @Override
    protected void onResume() {

        if (isBookmarked) {
            fab.setImageDrawable(getDrawable(R.drawable.ic_bookmark));
        }

        setUpArticleView(focusedArticle, focusedGeoFacets);

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setUpArticleView(Article article, ArrayList<String> geoFacets) {

        if (article != null) {
            String focusedString = article.getUrl();

            if (focusedString != null && focusedString.length() > 0) {
                new ParseUrl().execute(focusedString);
            }

            if (focusedImage != null && !focusedImage.getUrl().isEmpty()) {

                Glide.with(this).load(focusedImage.getUrl())
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
                if (geoFacets.size() > 1) {

                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < geoFacets.size(); i++) {
                        if (i == 0) {
                            sb.append(geoFacets.get(i));
                        } else {
                            sb.append(", " + geoFacets.get(i));
                        }
                    }
                    geoFacetString = sb.toString();

                } else if (geoFacets.size() == 1) {
                    geoFacetString = geoFacets.get(0);
                }

                geoFacetTitle.setText(geoFacetString);
            }

        } else {
            Log.e(TAG, "There is not a viable Article for supply");
        }

    }

    @Override
    public void onFileWriteComplete(String response, boolean hasError) {

        if (hasError) {
            //response is going to be an error message
            Snackbar.make(rootViewGroup, getResources().getString(R.string.file_write_error), Snackbar.LENGTH_SHORT).show();

        } else {
            //response is going to be the directory where the file is located
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark));
            Snackbar.make(rootViewGroup, getResources().getString(R.string.file_write_success), Snackbar.LENGTH_SHORT).show();
        }
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
                Elements storyBodies = article.getElementsByTag("div").select("div.story-body-supplemental");

                for (int i = 1; i <= storyBodies.size(); i++) {

                    org.jsoup.nodes.Element storyBody = storyBodies.get(i - 1).getElementsByTag("div").get(1);
                    Elements storyBodyText = storyBody.select("p[class=story-body-text story-content]");

                    for (org.jsoup.nodes.Element ele : storyBodyText) {
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
}
