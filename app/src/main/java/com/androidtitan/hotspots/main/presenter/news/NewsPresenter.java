package com.androidtitan.hotspots.main.presenter.news;

import com.androidtitan.hotspots.main.model.newyorktimes.Article;

import java.util.List;

/**
 * Created by amohnacs on 3/21/16.
 */
public interface NewsPresenter {

    List<Article> queryNews(String section, int limit);
}
