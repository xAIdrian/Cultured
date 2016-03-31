package com.androidtitan.hotspots.main.presenter.newsdetail;

import android.widget.ImageView;

import com.androidtitan.hotspots.main.model.newyorktimes.Multimedium;

import java.util.List;

/**
 * Created by amohnacs on 3/26/16.
 */
public interface NewsDetailPresenter {

    void getHeaderImage(List<Multimedium> mediaList, ImageView image);
    String formattedWikiUrl(String geofacet);
}
