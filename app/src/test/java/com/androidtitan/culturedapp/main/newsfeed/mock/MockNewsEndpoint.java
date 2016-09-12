package com.androidtitan.culturedapp.main.newsfeed.mock;

import com.androidtitan.culturedapp.main.domain.retrofit.NewsEndpoint;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Multimedium;
import com.androidtitan.culturedapp.model.newyorktimes.NewsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.mock.BehaviorDelegate;

/**
 * Created by amohnacs on 9/7/16.
 */

public class MockNewsEndpoint implements NewsEndpoint {
    private final String TAG = getClass().getSimpleName();

    public static final String TEST_STRING = "testString";
    public static final String TEST_URL = "http://test.com";
    public static final int TEST_INT = 1;
    public static final Integer TEST_INTEGER = 200;

    public ArrayList<String> TEST_FACET;

    private static final String TEST_SECTION = "testsection";
    private static final int TEST_LIMIT = 1;
    private static final int TEST_OFFSET = 0;
    private static final String TEST_API_KEY = "testapikey";

    private final BehaviorDelegate<NewsEndpoint> delegate;

    public MockNewsEndpoint(BehaviorDelegate<NewsEndpoint> service) {
        this.delegate = service;

        TEST_FACET = new ArrayList<>();
        TEST_FACET.add("testFacet");
    }

    @Override
    public Call<NewsResponse> articles(@Path("section") String string, @Query("limit") int count, @Query("offset") int offset, @Query("api-key") String yourKey) {

        ArrayList<Article> articles = new ArrayList<>();

        Article articleOne = new Article();
        articleOne.setSection(TEST_STRING);
        articleOne.setTitle(TEST_STRING);
        articleOne.setAbstract(TEST_STRING);
        articleOne.setUrl(TEST_URL);
        articleOne.setDesFacet(TEST_FACET);
        articleOne.setOrgFacet(TEST_FACET);
        articleOne.setPerFacet(TEST_FACET);
        articleOne.setGeoFacet(TEST_FACET);

        ArrayList<Multimedium> multimedia = new ArrayList<>();
        Multimedium multimedium = new Multimedium();
        multimedium.setUrl(TEST_URL);
        multimedium.setHeight(TEST_INTEGER);
        multimedium.setWidth(TEST_INTEGER);

        articleOne.setMultimedia(multimedia);

        articles.add(articleOne);

        NewsResponse newsResponse = new NewsResponse();
        newsResponse.setStatus(TEST_STRING);
        newsResponse.setCopyright(TEST_STRING);
        newsResponse.setNumResults(TEST_INT);
        newsResponse.setArticles(articles);

        return delegate.returningResponse(newsResponse)
                .articles(TEST_SECTION, TEST_LIMIT, TEST_OFFSET, TEST_API_KEY);


    }
}