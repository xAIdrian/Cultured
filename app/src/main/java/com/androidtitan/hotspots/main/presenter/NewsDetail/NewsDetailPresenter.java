package com.androidtitan.hotspots.main.presenter.newsdetail;

import android.widget.ImageView;

import com.androidtitan.hotspots.main.model.newyorktimes.Multimedium;
import com.androidtitan.hotspots.main.ui.activities.NewsDetailActivity;

import java.util.List;

/**
 * Created by amohnacs on 3/26/16.
 */
public interface NewsDetailPresenter {

    void takeActivity(NewsDetailActivity activity);
    void getHeaderImage(List<Multimedium> mediaList, ImageView image, int pixelWidth, int pixelHeight);
    String formatDESUrl(String facet);
    String formatPERUrl(String facet);
    String formatORgUrl(String facet);
    String formatGEOUrl(String facet);
}
