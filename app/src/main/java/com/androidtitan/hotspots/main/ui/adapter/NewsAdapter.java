package com.androidtitan.hotspots.main.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.main.model.newyorktimes.Article;
import com.androidtitan.hotspots.main.ui.NewsActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by amohnacs on 3/23/16.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = getClass().getSimpleName();

    private static final int SIMPLE_LAYOUT = 0;
    private static final int LARGE_IMAGE_LAYOUT = 1;
    private static final int MEDIUM_IMAGE_LAYOUT = 2;


    private Context context;
    private List<Article> articleList;
    private List<Integer> paletteList;


    @Inject
    public NewsAdapter(Context context, List<Article> adapterTrackList) {

        this.context = context;
        this.articleList = adapterTrackList;
        this.paletteList = new ArrayList<Integer>();

    }

    @Override
    public int getItemViewType(int position) {

        if(articleList.get(position).getMultimedia().size() > 3) {
            return LARGE_IMAGE_LAYOUT;

        } else if(articleList.get(position).getMultimedia().size() == 3) {
            return MEDIUM_IMAGE_LAYOUT;

        } else {
            return SIMPLE_LAYOUT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case SIMPLE_LAYOUT:
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row_layout_simple, parent, false);
                viewHolder = new SimpleViewHolder(v1);
                break;

            case LARGE_IMAGE_LAYOUT:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row_layout_large, parent, false);
                viewHolder = new LargeImageViewHolder(v2);
                break;

            case MEDIUM_IMAGE_LAYOUT:
                View v3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row_layout_medium, parent, false);
                viewHolder = new MediumImageViewHolder(v3);
                break;

            default:
                View def = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row_layout_simple, parent, false);
                viewHolder = new SimpleViewHolder(def);
                break;
        }

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case SIMPLE_LAYOUT:
                SimpleViewHolder simpleViewHolder = (SimpleViewHolder) holder;
                initViewholderSimple(simpleViewHolder, position);
                break;

            case LARGE_IMAGE_LAYOUT:
                LargeImageViewHolder largeImageViewHolder = (LargeImageViewHolder) holder;
                initLargeViewholderImage(largeImageViewHolder, position);
                break;

            case MEDIUM_IMAGE_LAYOUT:
                MediumImageViewHolder mediumImageViewHolder = (MediumImageViewHolder) holder;
                initMediumViewholderImage(mediumImageViewHolder, position);

            default:


                break;
        }

    }

    private void initViewholderSimple(final SimpleViewHolder holder, final int position) {
        holder.titleText.setText(articleList.get(position).getTitle());
        holder.abstractText.setText(articleList.get(position).getAbstract());
        holder.globalText.setText(articleList.get(position).getGeoFacet().get(0));


        paletteList.add(position, ContextCompat.getColor(context, R.color.colorPrimary));


        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewsActivity)context).startDetailActivity(
                        articleList.get(position),
                        paletteList.get(position));
            }
        });
    }

    private void initLargeViewholderImage(final LargeImageViewHolder holder, final int position) {

            holder.titleText.setText(articleList.get(position).getTitle());
            holder.abstractText.setText(articleList.get(position).getAbstract());
            holder.globalText.setText(articleList.get(position).getGeoFacet().get(0));

            try {
                Glide.with(context)
                        .load(articleList.get(position).getMultimedia().get(3).getUrl())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                        .into(new BitmapImageViewTarget(((LargeImageViewHolder) holder).articleImage) {
                            @Override
                            public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                super.onResourceReady(resource, glideAnimation);
                                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        // Here's your generated palette

                                        int bgColor = palette.from(resource).generate().getVibrantColor(
                                                ContextCompat.getColor(context, R.color.colorAccent));
                                        paletteList.add(position, bgColor);
                                        holder.paletteView.setBackgroundColor(bgColor);
                                    }
                                });
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.clickLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((NewsActivity)context).startDetailActivity(
                            articleList.get(position),
                            paletteList.get(position));
                }
            });
    }

    private void initMediumViewholderImage(final MediumImageViewHolder holder, final int position) {
        holder.titleText.setText(articleList.get(position).getTitle());
        holder.abstractText.setText(articleList.get(position).getAbstract());
        holder.globalText.setText(articleList.get(position).getGeoFacet().get(0));

        try {
            Glide.with(context)
                    .load(articleList.get(position).getMultimedia().get(2).getUrl())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(new BitmapImageViewTarget(((MediumImageViewHolder) holder).articleImage) {
                        @Override
                        public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            super.onResourceReady(resource, glideAnimation);
                            Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    // Here's your generated palette
                                    int bgColor = palette.from(resource).generate().getVibrantColor(
                                            ContextCompat.getColor(context, R.color.colorAccent));
                                    paletteList.add(bgColor);
                                    holder.paletteView.setBackgroundColor(bgColor);
                                }
                            });
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewsActivity)context).startDetailActivity(
                        articleList.get(position),
                        paletteList.get(position));
            }
        });
    }


    public static class LargeImageViewHolder extends RecyclerView.ViewHolder{

        @Nullable @Bind(R.id.rippleForeground) RelativeLayout clickLayout;
        @Nullable @Bind(R.id.articleImageView) ImageView articleImage;
        @Nullable @Bind(R.id.titleTextView) TextView titleText;
        @Nullable @Bind(R.id.abstractTextView) TextView abstractText;
        @Nullable @Bind(R.id.globalTextView) TextView globalText;

        @Nullable @Bind(R.id.paletteView) View paletteView;

        public LargeImageViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static class MediumImageViewHolder extends RecyclerView.ViewHolder{

        @Nullable @Bind(R.id.rippleForeground) RelativeLayout clickLayout;
        @Nullable @Bind(R.id.articleImageView) ImageView articleImage;
        @Nullable @Bind(R.id.titleTextView) TextView titleText;
        @Nullable @Bind(R.id.abstractTextView) TextView abstractText;
        @Nullable @Bind(R.id.globalTextView) TextView globalText;

        @Nullable @Bind(R.id.paletteView) View paletteView;


        public MediumImageViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {

        @Nullable @Bind(R.id.rippleForeground) RelativeLayout clickLayout;
        @Nullable @Bind(R.id.articleImageView) ImageView articleImage;
        @Nullable @Bind(R.id.titleTextView) TextView titleText;
        @Nullable @Bind(R.id.abstractTextView) TextView abstractText;
        @Nullable @Bind(R.id.globalTextView) TextView globalText;
        @Nullable @Bind(R.id.paletteView) View paletteView;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

}
