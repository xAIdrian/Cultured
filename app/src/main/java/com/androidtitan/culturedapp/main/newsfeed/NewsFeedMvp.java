package com.androidtitan.culturedapp.main.newsfeed;

import com.androidtitan.culturedapp.common.structure.MvpPresenter;
import com.androidtitan.culturedapp.common.structure.MvpView;
import com.androidtitan.culturedapp.model.ApiError;
import com.androidtitan.culturedapp.model.newyorktimes.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by amohnacs on 8/29/16.
 */

public interface NewsFeedMvp {

    interface Provider {

        List<Article> fetchArticles(String section, int limit, CallbackListener listener);
        void fetchAdditionalArticles(String section, int limit, int offset, CallbackListener listener);
        void refreshForAdditionalArticlesToInsert(String section, List<Article> articlesList, CallbackListener listener);

        interface CallbackListener {
            void appendArticleToAdapter(Article article);
            void insertArticleIntoAdapter(int index, Article article);
            void insertArticlesIntoAdapter(int index, ArrayList<Article> articles);

            void responseFailed(ApiError error);
            void onCompleted();
        }
    }

    interface Presenter {

        List<Article> loadArticles(int limit);
        void loadOffsetArticles(int limit, int offset);
        void newsArticlesRefresh(List<Article> articles, int limit);

    }

    interface View extends MvpView {

        void appendAdapterItem(Article article);
        void insertAdapterItem(int index, Article article);
        void insertAdapterItems(int index, ArrayList<Article> articles);

        List<Article> getArticles();

        void displayError(String message, Map<String, Object> additionalProperties);
    }

}
