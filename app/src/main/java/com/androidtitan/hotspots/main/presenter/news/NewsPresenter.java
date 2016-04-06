package com.androidtitan.hotspots.main.presenter.news;

import com.androidtitan.hotspots.main.model.newyorktimes.Article;
import com.androidtitan.hotspots.main.ui.activities.NewsActivity;

import java.util.List;

/**
 * Created by amohnacs on 3/21/16.
 */
public interface NewsPresenter {

    void takeActivity(NewsActivity activity);
    List<Article> queryNews(String section, int limit);
    void refreshQueryNews(String section, int limit);
}
