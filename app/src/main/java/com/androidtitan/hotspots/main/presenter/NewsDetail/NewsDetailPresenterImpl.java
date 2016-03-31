package com.androidtitan.hotspots.main.presenter.newsdetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.widget.ImageView;

import com.androidtitan.hotspots.main.model.newyorktimes.Multimedium;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by amohnacs on 3/26/16.
 */
public class NewsDetailPresenterImpl implements NewsDetailPresenter{
    private final String TAG = getClass().getSimpleName();

    private Retrofit retrofit;
    private Context context;
    private NewsDetailView newsView;

    @Inject //todo:we are going to make this switch to an RSS Feed Version
    public NewsDetailPresenterImpl(Context context, NewsDetailView newsview) {

        this.context = context;
        this.newsView = newsview;
    }


    @Override
    public void getHeaderImage(List<Multimedium> mediaList, ImageView articleImageView) {

        try {
            Glide.with(context)
                    .load(mediaList.get(mediaList.size() - 1).getUrl())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(new BitmapImageViewTarget(articleImageView) {
                        @Override
                        public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            super.onResourceReady(resource, glideAnimation);
                            Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    // Here's your generated palette
                                    newsView.onImageDownload(palette);
                                }
                            });
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String formattedWikiUrl(String geofacet) {

        StringBuilder stringBuilder = new StringBuilder("https://en.m.wikipedia.org/wiki/");
        boolean shouldKeepAppending = true;

        for(int i = 0; i < geofacet.length(); i++) {
            if(shouldKeepAppending) {
                if (geofacet.charAt(i) == ' ') {
                    stringBuilder.append("_");

                } else if (geofacet.charAt(i) == ',') {
                    shouldKeepAppending = false;

                } else if (geofacet.charAt(i) == '(') {
                    Log.e(TAG, "glop");
                    shouldKeepAppending = false;
                } else {
                    stringBuilder.append(geofacet.charAt(i));
                }
            }

            //Log.e(TAG, "what was added :: " + stringBuilder.charAt(32 + i));
        }

        Log.e(TAG, stringBuilder.toString());

        return stringBuilder.toString();
    }

}
