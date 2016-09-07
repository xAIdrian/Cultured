package com.androidtitan.culturedapp.main.newsfeed;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.main.newsfeed.ui.NewsActivity;
import com.androidtitan.culturedapp.main.util.Utils;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by amohnacs on 3/23/16.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = getClass().getSimpleName();

    public static String CULTURED_PREFERENCES = "com.androidtitan.hotspots.main.culturedpreferences";
    public static String PREFERENCES_SHOULD_ONBOARD = "com.androidtitan.hotspots.main.preferencesshouldonboard";
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
    private int itemCount = 0;
    private boolean shouldShowAboutCard = false;

    @Inject
    public NewsAdapter(Context context, List<Article> adapterTrackList) {

        this.context = context;
        this.articleList = adapterTrackList;

        sharedPreferences = context.getSharedPreferences(CULTURED_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //todo: if sharedPreferences says it is our first time using the app
        if (!sharedPreferences.contains(PREFERENCES_SHOULD_ONBOARD)) {
            editor.putBoolean(PREFERENCES_SHOULD_ONBOARD, true);
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

            view.setTranslationY(Utils.getScreenHeight());
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

    public void insertToAdapter(int position, Article article) {
        articleList.add(position, article);
        notifyItemChanged(position);
    }

    public void clearList() {
        articleList.clear();
    }

    public void wipe() {
        articleList.clear();
        notifyDataSetChanged();
    }

    public void resetOnboardingCard() {

        editor.putBoolean(PREFERENCES_SHOULD_ONBOARD, true);
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

        } else if (position == 0 && sharedPreferences.getBoolean(PREFERENCES_SHOULD_ONBOARD, false)) {
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

            case ONBOARDING_LAYOUT:
                View v4 = LayoutInflater.from(parent.getContext()).inflate(R.layout.onboarding_card, parent, false);
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
                        editor.putBoolean(PREFERENCES_SHOULD_ONBOARD, false);
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

        if(sharedPreferences.getBoolean(PREFERENCES_SHOULD_ONBOARD, false) || shouldShowAboutCard) {

            position ++;

        }

        final int finalPosition = position;

        holder.titleText.setText(articleList.get(position).getTitle());
        holder.abstractText.setText(articleList.get(position).getAbstract());
        holder.globalText.setText(articleList.get(position).getGeoFacet().get(0));

//        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((NewsActivity) context).startDetailActivity(
//                        articleList.get(finalPosition), null);
//            }
//        });

    }

    private void initLargeViewholderImage(final LargeImageViewHolder holder, final int position) {

        holder.titleText.setText(articleList.get(position).getTitle());
        holder.abstractText.setText(articleList.get(position).getAbstract());

        DateFormat formatter = new SimpleDateFormat("MMM dd h:mm a");
        String dateFormatted = formatter.format(articleList.get(position).getCreatedDate());
        holder.dateText.setText(dateFormatted);

        holder.sectionView.setText(articleList.get(position).getGeoFacet().get(0));

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
                            Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    // Here's your generated palette

                                    int bgColor = palette.from(resource).generate().getVibrantColor(
                                            ContextCompat.getColor(context, R.color.colorAccent));
                                    //holder.paletteView.setBackgroundColor(bgColor);

                                    //todo: make changes to the drawable to shade it with Palette
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
//                ((NewsActivity) context).startDetailActivity(
//                        articleList.get(position), holder.articleImage);
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
                    .animate(R.anim.fade_in)
                    .placeholder(R.drawable.im_placeholder)
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
//                ((NewsActivity) context).startDetailActivity(
//                        articleList.get(position), holder.articleImage);
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
        @Bind(R.id.titleTextView)
        TextView titleText;
        @Nullable
        @Bind(R.id.abstractTextView)
        TextView abstractText;
        @Nullable
        @Bind(R.id.globalTextView)
        TextView globalText;

        @Bind(R.id.dateTextView)
        TextView dateText;
        @Bind(R.id.sectionTextView)
        TextView sectionView;


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
        @Bind(R.id.titleTextView)
        TextView titleText;
        @Nullable
        @Bind(R.id.abstractTextView)
        TextView abstractText;
        @Nullable
        @Bind(R.id.globalTextView)
        TextView globalText;



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
        itemCount = articleList.size();
        return itemCount;
    }

}
