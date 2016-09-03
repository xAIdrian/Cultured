package com.androidtitan.hotspots.main.newsdetail;

import android.widget.ImageView;

import com.androidtitan.hotspots.model.newyorktimes.Multimedium;

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
