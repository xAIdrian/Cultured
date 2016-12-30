package com.androidtitan.culturedapp.main.toparticle;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.view.TopArticleHeaderLayout;
import com.androidtitan.culturedapp.main.newsfeed.NewsAdapter;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Multimedium;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Adrian Mohnacs on 12/10/16.
 */

public class TopArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = getClass().getSimpleName();

    private Context context;
    public List<Article> articleList;

    public TopArticleAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v1 = inflater.inflate(R.layout.top_article_row_layout_xlarge, parent, false);
        viewHolder = new XLargeArticleViewHolder(v1);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        XLargeArticleViewHolder holder2 = (XLargeArticleViewHolder) holder;
        initViewHolder(holder2, position, articleList.get(position));
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    private void initViewHolder(final XLargeArticleViewHolder holder, int position, Article article) {

        DateFormat formatter = new SimpleDateFormat("MMM dd h:mm a");
        final String dateFormatted = formatter.format(article.getCreatedDate());

        try {
            Glide.with(context)
                    .load(article.getMultimedia().get(0).getUrl())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .animate(R.anim.fade_in)
                    .placeholder(R.drawable.im_placeholder)
                    .into(new BitmapImageViewTarget(((TopArticleAdapter.XLargeArticleViewHolder) holder).articleImage) {
                        @Override
                        public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            super.onResourceReady(resource, glideAnimation);

                            holder.topArticleHeaderLayout.setGradientViewHeight(holder.articleImage);
                            holder.topArticleHeaderLayout.setTitleText(article.getTitle());
                            holder.topArticleHeaderLayout.setAbstractText(article.getAbstract());
                            holder.topArticleHeaderLayout.setDateText(dateFormatted);

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();

            if (holder.topArticleHeaderLayout != null) {
                holder.topArticleHeaderLayout.setGradientViewHeight(holder.articleImage);
            }
            holder.topArticleHeaderLayout.setTitleText(article.getTitle());
            holder.topArticleHeaderLayout.setAbstractText(article.getAbstract());
            //holder.topArticleHeaderLayout.setSectionText(articleList.get(position).getGeoFacet().get(0));
            holder.topArticleHeaderLayout.setDateText(dateFormatted);

        }

        if(article.getGeoFacet().size() > 0) {
            holder.facetTextView.setText(article.getGeoFacet().get(0).getFacetText());
        }


    }

    public static class XLargeArticleViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @Bind(R.id.rippleForeground)
        RelativeLayout clickLayout;
        @Bind(R.id.facetTitleTextView)
        TextView facetTextView;
        @Nullable
        @Bind(R.id.articleImageView)
        ImageView articleImage;
        @Nullable
        @Bind(R.id.newsHeaderLayout)
        TopArticleHeaderLayout topArticleHeaderLayout;

        public XLargeArticleViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
