package com.androidtitan.culturedapp.main.newsfeed;

import com.androidtitan.culturedapp.common.MvpView;
import com.androidtitan.culturedapp.model.ApiError;
import com.androidtitan.culturedapp.model.newyorktimes.Article;

import java.util.List;
import java.util.Map;

/**
 * Created by amohnacs on 8/29/16.
 */

public interface NewsMvp {

    interface Provider {

        List<Article> fetchArticles(String section, int limit, CallbackListener listener);
        void fetchAdditionalArticles(String section, int limit, int offset, CallbackListener listener);
        void fetchAdditionalArticlesToInsert(String section, List<Article> articlesList, CallbackListener listener);

        interface CallbackListener {
            void appendArticleToAdapter(Article article);
            void insertArticleInAdapter(int index, Article article);

            void responseFailed(ApiError error);

            void onCompleted();
        }
    }

    interface Presenter {

        List<Article> loadArticles(int limit);
        void loadOffsetArticles(int limit, int offset);
        void newArticleRefresh();

    }

    interface View extends MvpView {

        void updateNewsAdapter();
        void onLoadComplete();

        void appendAdapterItem(Article article);
        void insertAdapterItem(int index, Article article);

        List<Article> getArticles();

        void displayError(String message, Map<String, Object> additionalProperties);
    }

}
