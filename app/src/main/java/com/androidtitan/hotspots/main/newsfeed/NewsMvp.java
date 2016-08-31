package com.androidtitan.hotspots.main.newsfeed;

import com.androidtitan.hotspots.common.MvpView;
import com.androidtitan.hotspots.main.model.newyorktimes.Article;

import java.util.List;

/**
 * Created by amohnacs on 8/29/16.
 */

public interface NewsMvp {

    interface Provider {

        List<Article> fetchArticles(String section, int limit);
        void fetchAdditionalArticles(String section, int limit, int offset, CallbackListener listener);
        void fetchAdditionalArticlesToInsert(String section, List<Article> articlesList, CallbackListener listener);

        interface CallbackListener {
            void appendArticleToAdapter(Article article);
            void insertArticleInAdapter(int index, Article article);

            void onCompleted();
        }
    }

    interface Presenter {

        void initalArticleStore(int limit);
        void loadNextNewsArticles(int limit, int offset);
        void newArticleRefresh();

        List<Article> getArticles();
    }

    interface View extends MvpView{

        void updateNewsAdapter();
        void refreshCompleted();

        void appendAdapterItem(Article article);
        void insertAdapterItem(int index, Article article);

    }

}
