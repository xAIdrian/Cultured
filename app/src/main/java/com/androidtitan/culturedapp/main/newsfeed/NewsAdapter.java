package com.androidtitan.culturedapp.main.newsfeed;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.view.NewsHeaderLayout;
import com.androidtitan.culturedapp.main.TrendingActivity;
import com.androidtitan.culturedapp.main.newsfeed.ui.NewsActivity;
import com.androidtitan.culturedapp.main.util.ScreenUtils;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.androidtitan.culturedapp.common.Constants.CULTURED_PREFERENCES;
import static com.androidtitan.culturedapp.common.Constants.PREFERENCES_APP_FIRST_RUN;

/**
 * Created by amohnacs on 3/23/16.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = getClass().getSimpleName();

    private int ANIMATED_ITEMS_COUNT;

    private static final int SIMPLE_LAYOUT = 0;
    private static final int LARGE_IMAGE_LAYOUT = 1;
    private static final int MEDIUM_IMAGE_LAYOUT = 2;
    private static final int ONBOARDING_LAYOUT = 3;
    private static final int ABOUT_LAYOUT = 4;

    private Context context;
    private List<Article> articleList;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private int lastAnimatedPosition = -1;
    private boolean shouldShowAboutCard = false;

    @Inject
    public NewsAdapter(Context context, List<Article> adapterTrackList) {

        this.context = context;
        this.articleList = adapterTrackList;

        sharedPreferences = context.getSharedPreferences(CULTURED_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (!sharedPreferences.contains(PREFERENCES_APP_FIRST_RUN)) {
            editor.putBoolean(PREFERENCES_APP_FIRST_RUN, true);
            editor.commit();

            articleList.add(0, new Article());
        }

        ANIMATED_ITEMS_COUNT = animationCount();

    }

    private int animationCount() {
        int screenSize = context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            return 8;
        } else {
            return 3;
        }
    }

    /**
     * Animation for sliding the cards "up" when the entire list is initially loaded
     *
     * @param view
     * @param position
     */
    private void runEnterAnimation(View view, int position) {

        if (position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {

            lastAnimatedPosition = position;

            view.setTranslationY(ScreenUtils.getScreenHeight());
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }

    }

    public void appendToAdapter(Article article) {
        articleList.add(article);
        notifyItemInserted(articleList.size() - 1);
    }

    public void insertIntoAdapter(int position, Article article) {
        articleList.add(position, article);
        notifyItemChanged(position);
    }

    public void insertIntoAdapter(int position, ArrayList<Article> articles) {
        articleList.addAll(position, articles);
        notifyItemRangeChanged(0, articles.size());
    }

    public void clearList() {
        articleList.clear();
    }

    public void wipe() {
        articleList.clear();
        notifyDataSetChanged();
    }

    public void resetOnboardingCard() {

        editor.putBoolean(PREFERENCES_APP_FIRST_RUN, true);
        editor.commit();
        articleList.add(0, new Article());
        notifyDataSetChanged();
    }

    public void showAboutCard() {
        shouldShowAboutCard = true;
        articleList.add(0, new Article());
        notifyDataSetChanged();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public boolean getAboutStatus() {
        return shouldShowAboutCard;
    }

    @Override
    public int getItemViewType(int position) {

        if(position == 0 && shouldShowAboutCard) {
            return ABOUT_LAYOUT;

        } else if (position == 0 && sharedPreferences.getBoolean(PREFERENCES_APP_FIRST_RUN, false)) {
            return ONBOARDING_LAYOUT;

        } else if (articleList.get(position).getMultimedia().size() > 3) {
            return LARGE_IMAGE_LAYOUT;

        } else if (articleList.get(position).getMultimedia().size() == 3) {
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
                View v1 = inflater.inflate(R.layout.news_row_layout_simple, parent, false);
                viewHolder = new SimpleViewHolder(v1);
                break;

            case LARGE_IMAGE_LAYOUT:
                View v2 = inflater.inflate(R.layout.news_row_layout_large, parent, false);
                viewHolder = new LargeImageViewHolder(v2);
                break;

            case MEDIUM_IMAGE_LAYOUT:
                View v3 = inflater.inflate(R.layout.news_row_layout_medium, parent, false);
                viewHolder = new MediumImageViewHolder(v3);
                break;

            case ONBOARDING_LAYOUT:
                View v4 = inflater.inflate(R.layout.onboarding_card, parent, false);
                viewHolder = new OnboardingViewHolder(v4);
                break;

            case ABOUT_LAYOUT:
                View v5 = LayoutInflater.from(parent.getContext()).inflate(R.layout.about_card, parent, false);
                viewHolder = new AboutViewHolder(v5);
                break;

            default:
                View v6 = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row_layout_simple, parent, false);
                viewHolder = new SimpleViewHolder(v6);
                break;
        }

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        runEnterAnimation(holder.itemView, position);

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
                break;

            case ONBOARDING_LAYOUT:
                OnboardingViewHolder onboardingViewHolder = (OnboardingViewHolder) holder;

                onboardingViewHolder.gotitText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editor.putBoolean(PREFERENCES_APP_FIRST_RUN, false);
                        editor.commit();

                        articleList.remove(0);
                        notifyItemRemoved(0);
                        //notifyDataSetChanged();
                    }
                });
                break;

            case ABOUT_LAYOUT:
                AboutViewHolder aboutViewHolder = (AboutViewHolder) holder;

                aboutViewHolder.gotitText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shouldShowAboutCard = false;

                        articleList.remove(0);
                        notifyItemRemoved(0);
                        //notifyDataSetChanged();
                    }
                });
                break;

            default:
                //
                break;
        }

    }

    private void initViewholderSimple(final SimpleViewHolder holder,  int position) {

        if(sharedPreferences.getBoolean(PREFERENCES_APP_FIRST_RUN, false) || shouldShowAboutCard) {

            position ++;

        }

        holder.titleText.setText(articleList.get(position).getTitle());
        holder.abstractText.setText(articleList.get(position).getAbstract());
        holder.globalText.setText(articleList.get(position).getGeoFacet().get(0).getFacetText());

        int finalPosition = position;
        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDetailActivity(articleList.get(finalPosition), holder.articleImage);

                Intent tempTrendingIntent = new Intent(context, TrendingActivity.class);

                // this adds all of {@link NewsDetailActivity}'s parents to the stack followed by the Activity itself
                PendingIntent pendingIntent =
                        TaskStackBuilder.create(context)
                                .addNextIntentWithParentStack(tempTrendingIntent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                try {
                    pendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void initLargeViewholderImage(final LargeImageViewHolder holder, final int position) {

        DateFormat formatter = new SimpleDateFormat("MMM dd h:mm a");
        final String dateFormatted = formatter.format(articleList.get(position).getCreatedDate());


        try {
            Glide.with(context)
                    .load(articleList.get(position).getMultimedia().get(3).getUrl())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .placeholder(R.drawable.im_placeholder)
                    .into(new BitmapImageViewTarget(((LargeImageViewHolder) holder).articleImage) {
                        @Override
                        public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            super.onResourceReady(resource, glideAnimation);

                            holder.newsHeaderLayout.setGradientViewHeight(holder.articleImage);
                            holder.newsHeaderLayout.setTitleText(articleList.get(position).getTitle());
                            holder.newsHeaderLayout.setSectionText(articleList.get(position).getGeoFacet().get(0).getFacetText());
                            holder.newsHeaderLayout.setDateText(dateFormatted);

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();

            holder.newsHeaderLayout.setGradientViewHeight(holder.articleImage);
            holder.newsHeaderLayout.setTitleText(articleList.get(position).getTitle());
            holder.newsHeaderLayout.setSectionText(articleList.get(position).getGeoFacet().get(0).getFacetText());
            holder.newsHeaderLayout.setDateText(dateFormatted);
        }

        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDetailActivity(articleList.get(position), holder.articleImage);

            }
        });

    }

    private void initMediumViewholderImage(final MediumImageViewHolder holder, final int position) {

        DateFormat formatter = new SimpleDateFormat("MMM dd h:mm a");
        final String dateFormatted = formatter.format(articleList.get(position).getCreatedDate());

        try {
            Glide.with(context)
                    .load(articleList.get(position).getMultimedia().get(2).getUrl())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .animate(R.anim.fade_in)
                    .placeholder(R.drawable.im_placeholder)
                    .into(new BitmapImageViewTarget(((MediumImageViewHolder) holder).articleImage) {
                        @Override
                        public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            super.onResourceReady(resource, glideAnimation);

                            if (holder.newsHeaderLayout != null) {
                                holder.newsHeaderLayout.setGradientViewHeight(holder.articleImage);
                            }
                            holder.newsHeaderLayout.setTitleText(articleList.get(position).getTitle());
                            holder.newsHeaderLayout.setSectionText(articleList.get(position).getGeoFacet().get(0).getFacetText());
                            holder.newsHeaderLayout.setDateText(dateFormatted);

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();

            if (holder.newsHeaderLayout != null) {
                holder.newsHeaderLayout.setGradientViewHeight(holder.articleImage);
            }
            holder.newsHeaderLayout.setTitleText(articleList.get(position).getTitle());
            holder.newsHeaderLayout.setSectionText(articleList.get(position).getGeoFacet().get(0).getFacetText());
            holder.newsHeaderLayout.setDateText(dateFormatted);
        }

        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDetailActivity(articleList.get(position), holder.articleImage);
            }
        });
    }


    public static class LargeImageViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @Bind(R.id.rippleForeground)
        RelativeLayout clickLayout;
        @Nullable
        @Bind(R.id.articleImageView)
        ImageView articleImage;
        @Nullable
        @Bind(R.id.newsHeaderLayout)
        NewsHeaderLayout newsHeaderLayout;

        public LargeImageViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static class MediumImageViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @Bind(R.id.rippleForeground)
        RelativeLayout clickLayout;
        @Nullable
        @Bind(R.id.articleImageView)
        ImageView articleImage;
        @Nullable
        @Bind(R.id.newsHeaderLayout)
        NewsHeaderLayout newsHeaderLayout;


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

        @Nullable
        @Bind(R.id.rippleForeground)
        RelativeLayout clickLayout;
        @Nullable
        @Bind(R.id.articleImageView)
        ImageView articleImage;
        @Nullable
        @Bind(R.id.titleTextView)
        TextView titleText;
        @Nullable
        @Bind(R.id.abstractTextView)
        TextView abstractText;
        @Nullable
        @Bind(R.id.globalTextView)
        TextView globalText;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static class OnboardingViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @Bind(R.id.gotitTextView)
        TextView gotitText;

        public OnboardingViewHolder(View itemView) {
            super(itemView);

            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class AboutViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @Bind(R.id.gotitTextView)
        TextView gotitText;

        public AboutViewHolder(View itemView) {
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

    private void sendDetailActivity(Article article, ImageView imageView) {
        ((NewsActivity) context).startDetailActivity(
            article, imageView);
    }

}
