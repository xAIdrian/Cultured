package com.androidtitan.culturedapp.main.newsfeed;

import android.content.Context;

import com.androidtitan.culturedapp.BuildConfig;
import com.androidtitan.culturedapp.model.ApiError;
import com.androidtitan.culturedapp.model.newyorktimes.Article;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.androidtitan.culturedapp.common.Constants.TEST_LIMIT;
import static com.androidtitan.culturedapp.common.Constants.TEST_OFFSET;
import static com.androidtitan.culturedapp.common.Constants.TEST_SECTION;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by amohnacs on 9/6/16.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class NewsPresenterTest {
    private final String TAG = getClass().getSimpleName();

    @Mock Context mockContext;
    @Mock NewsProvider mockNewsProvider;
    @Mock NewsMvp.View mockViewCallback;
    @Mock  NewsMvp.Provider.CallbackListener mockCallback;

    private NewsPresenter newsPresenter;

    private Article testArticle = new Article();
    private ApiError testApiError = new ApiError(404, "Error");


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        newsPresenter = spy(new NewsPresenter(mockContext));
    }

    @Test
    public void loadArticles() throws Exception {

        when(mockNewsProvider.fetchArticles(TEST_SECTION, TEST_LIMIT, mockCallback))
                .thenReturn(new ArrayList<Article>());

        List<Article> expectedArray = newsPresenter.loadArticles(TEST_LIMIT);

        assertNotNull(expectedArray);
    }

    @Test
    public void loadOffsetArticles() throws Exception {

        newsPresenter.newsProvider = mockNewsProvider;

        newsPresenter.loadOffsetArticles(TEST_LIMIT, TEST_OFFSET);

        verify(mockNewsProvider).fetchAdditionalArticles(
                TEST_SECTION, TEST_LIMIT, TEST_OFFSET, newsPresenter);
    }

    @Test
    public void newArticleRefresh() throws Exception {

        newsPresenter.newsProvider = mockNewsProvider;
        when(newsPresenter.getMvpView()).thenReturn(mockViewCallback);
        when(mockViewCallback.getArticles()).thenReturn(new ArrayList<Article>());

        newsPresenter.newsArticlesRefresh();

        verify(mockNewsProvider).fetchAdditionalArticlesToInsert(
                TEST_SECTION, new ArrayList<Article>(), newsPresenter);
    }

    @Test
    public void appendArticleToAdapter() throws Exception {

        when(newsPresenter.getMvpView()).thenReturn(mockViewCallback);
        doNothing().when(mockViewCallback).appendAdapterItem(testArticle);
        doNothing().when(mockViewCallback).updateNewsAdapter();

        newsPresenter.appendArticleToAdapter(testArticle);

        verify(mockViewCallback).appendAdapterItem(testArticle);
        verify(mockViewCallback).updateNewsAdapter();
    }

    @Test
    public void insertArticleInAdapter() throws Exception {
        when(newsPresenter.getMvpView()).thenReturn(mockViewCallback);
        doNothing().when(mockViewCallback).insertAdapterItem(0, testArticle);

        newsPresenter.insertArticleIntoAdapter(0, testArticle);

        verify(mockViewCallback).insertAdapterItem(0, testArticle);
    }

    @Test
    public void onCompleted() throws Exception {
        when(newsPresenter.getMvpView()).thenReturn(mockViewCallback);
        doNothing().when(mockViewCallback).onLoadComplete();

        newsPresenter.onCompleted();

        verify(mockViewCallback).onLoadComplete();
    }

    @Test
    public void responseFailed() throws Exception {
        when(newsPresenter.getMvpView()).thenReturn(mockViewCallback);
        doNothing().when(mockViewCallback).displayError(testApiError.getMessage(), new HashMap<String, Object>());

        newsPresenter.responseFailed(testApiError);

        verify(mockViewCallback).displayError(testApiError.getMessage(), new HashMap<String, Object>());
    }

    @After
    public void tearDown() throws Exception {
        newsPresenter = null;
    }

}