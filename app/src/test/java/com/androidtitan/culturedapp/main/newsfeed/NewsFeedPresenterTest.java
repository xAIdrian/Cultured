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
public class NewsFeedPresenterTest {
    private final String TAG = getClass().getSimpleName();

    @Mock Context mockContext;
    @Mock
    NewsFeedProvider mockNewsFeedProvider;
    @Mock NewsFeedMvp.View mockViewCallback;
    @Mock  NewsFeedMvp.Provider.CallbackListener mockCallback;

    private NewsFeedPresenter newsFeedPresenter;

    private Article testArticle = new Article();
    private ApiError testApiError = new ApiError(404, "Error");


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        newsFeedPresenter = spy(new NewsFeedPresenter(mockContext));
    }

    @Test
    public void loadArticles() throws Exception {

        when(mockNewsFeedProvider.fetchArticles(TEST_SECTION, TEST_LIMIT, mockCallback))
                .thenReturn(new ArrayList<Article>());

        List<Article> expectedArray = newsFeedPresenter.loadArticles(TEST_LIMIT);

        assertNotNull(expectedArray);
    }

    @Test
    public void loadOffsetArticles() throws Exception {

        newsFeedPresenter.newsFeedProvider = mockNewsFeedProvider;

        newsFeedPresenter.loadOffsetArticles(TEST_LIMIT, TEST_OFFSET);

        verify(mockNewsFeedProvider).fetchAdditionalArticles(
                TEST_SECTION, TEST_LIMIT, TEST_OFFSET, newsFeedPresenter);
    }

    @Test
    public void newArticleRefresh() throws Exception {

        newsFeedPresenter.newsFeedProvider = mockNewsFeedProvider;
        when(newsFeedPresenter.getMvpView()).thenReturn(mockViewCallback);
        when(mockViewCallback.getArticles()).thenReturn(new ArrayList<Article>());

        newsFeedPresenter.newsArticlesRefresh();

        verify(mockNewsFeedProvider).refreshForAdditionalArticlesToInsert(
                TEST_SECTION, new ArrayList<Article>(), newsFeedPresenter);
    }

    @Test
    public void appendArticleToAdapter() throws Exception {

        when(newsFeedPresenter.getMvpView()).thenReturn(mockViewCallback);
        doNothing().when(mockViewCallback).appendAdapterItem(testArticle);
        doNothing().when(mockViewCallback).updateNewsAdapter();

        newsFeedPresenter.appendArticleToAdapter(testArticle);

        verify(mockViewCallback).appendAdapterItem(testArticle);
        verify(mockViewCallback).updateNewsAdapter();
    }

    @Test
    public void insertArticleInAdapter() throws Exception {
        when(newsFeedPresenter.getMvpView()).thenReturn(mockViewCallback);
        doNothing().when(mockViewCallback).insertAdapterItem(0, testArticle);

        newsFeedPresenter.insertArticleIntoAdapter(0, testArticle);

        verify(mockViewCallback).insertAdapterItem(0, testArticle);
    }

    @Test
    public void onCompleted() throws Exception {
        when(newsFeedPresenter.getMvpView()).thenReturn(mockViewCallback);
        doNothing().when(mockViewCallback).onLoadComplete();

        newsFeedPresenter.onCompleted();

        verify(mockViewCallback).onLoadComplete();
    }

    @Test
    public void responseFailed() throws Exception {
        when(newsFeedPresenter.getMvpView()).thenReturn(mockViewCallback);
        doNothing().when(mockViewCallback).displayError(testApiError.getMessage(), new HashMap<String, Object>());

        newsFeedPresenter.responseFailed(testApiError);

        verify(mockViewCallback).displayError(testApiError.getMessage(), new HashMap<String, Object>());
    }

    @After
    public void tearDown() throws Exception {
        newsFeedPresenter = null;
    }

}