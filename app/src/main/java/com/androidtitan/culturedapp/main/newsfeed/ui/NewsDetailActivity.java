package com.androidtitan.culturedapp.main.newsfeed.ui;

import com.google.gson.Gson;

import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtitan.culturedapp.R;
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

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.androidtitan.culturedapp.common.Constants.ARTICLE_BOOKMARKED;
import static com.androidtitan.culturedapp.common.Constants.ARTICLE_EXTRA;
import static com.androidtitan.culturedapp.common.Constants.ARTICLE_GEO_FACETS;

public class NewsDetailActivity extends AppCompatActivity implements FileManager.FileCallback {

    private static final String TAG = NewsDetailActivity.class.getSimpleName();

    private final static String SAVED_STATE_ARTICLE = "newsdetailactivity.savedstatearticle";
    private final static String SAVED_STATE_FACETS = "newsdetailactivity.savedstatefacets";
    private final static String SAVED_BOOKMARK_STATUS = "newsdetailactivity.savedbookmarkstatus";
    private final static String SAVED_MULTIMEDIA_URL = "newsdetailactivity.savedmultimediaurl";
    public final static String SAVED_MULTIMEDIA = "newsdetailactivity.savedmultimedia";


    @BindView(R.id.backgroundImageView)
    ImageView backgroundImage;

    @BindView(R.id.detailTitleTextView)
    TextView detailTitle;

    @BindView(R.id.geoFacetTitleTextView)
    TextView geoFacetTitle;

    @BindView(R.id.dateTextView)
    TextView dateText;

    @BindView(R.id.sourTextViewTextView)
    TextView sourText;

    @BindView(R.id.storyTextView)
    TextView storyText;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private FileManager fileManager;

    private Animation fadeIn;

    private Article focusedArticle = null;

    private Multimedium focusedImage;

    private ArrayList<String> focusedGeoFacets;

    private View rootViewGroup;

    private String multimediaUrl;

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
            multimediaUrl = savedInstanceState.getString(SAVED_MULTIMEDIA_URL);

            if (savedInstanceState.getBoolean(SAVED_BOOKMARK_STATUS)) {
                isBookmarked = true;
            }

        } else {
            if (extras != null) {
                buildArticleObject(extras);
            }
        }

        fab.setOnClickListener(view -> fileManager.writeArticleToFile(this, focusedArticle));

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
        outState.putString(SAVED_MULTIMEDIA_URL, multimediaUrl);
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

            if (multimediaUrl == null || multimediaUrl.isEmpty()) {
                multimediaUrl = focusedImage.getUrl();
            }
            Glide.with(this).load(multimediaUrl)
                    .crossFade()
                    .centerCrop()
                    .into(backgroundImage);

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

    @Override
    public void onFileDeleteComplete() {
        //no op
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
