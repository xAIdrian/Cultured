package com.androidtitan.hotspots.main.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.main.model.newyorktimes.Article;
import com.androidtitan.hotspots.main.util.ImageDownloader;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by amohnacs on 3/23/16.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = getClass().getSimpleName();

    private static final int SIMPLE_LAYOUT = 0;
    private static final int IMAGE_LAYOUT = 1;

    ImageDownloader imageDownloader;

    private Context context;
    private List<Article> articleList;


    @Inject
    public NewsAdapter(Context context, List<Article> adapterTrackList) {

        this.context = context;
        this.articleList = adapterTrackList;

        imageDownloader = new ImageDownloader();
    }

    @Override
    public int getItemViewType(int position) {

        if(articleList.get(position).getMultimedia().size() <= 0) {
            return SIMPLE_LAYOUT;
        } else {
            return IMAGE_LAYOUT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case SIMPLE_LAYOUT:
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_role_layout_simple, parent, false);
                viewHolder = new SimpleViewHolder(v1);
                break;

            case IMAGE_LAYOUT:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row_layout, parent, false);
                viewHolder = new ImageViewHolder(v2);
                break;

            default:
                View v3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_role_layout_simple, parent, false);
                viewHolder = new SimpleViewHolder(v3);
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

            case IMAGE_LAYOUT:
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                initViewholderImage(imageViewHolder, position);

                break;

            default:


                break;
        }

    }

    private void initViewholderSimple(SimpleViewHolder holder, int position) {
        holder.titleText.setText(articleList.get(position).getTitle());
        holder.abstractText.setText(articleList.get(position).getAbstract());
        holder.globalText.setText(articleList.get(position).getGeoFacet().get(0));
    }

    private void initViewholderImage(ImageViewHolder holder, int position) {
        holder.titleText.setText(articleList.get(position).getTitle());
        holder.abstractText.setText(articleList.get(position).getAbstract());

        holder.globalText.setText(articleList.get(position).getGeoFacet().get(0));

        //Log.e(TAG, String.valueOf(articleList.get(position).getMultimedia().size()));
        try {
            imageDownloader.imageDownload(articleList.get(position).getMultimedia().get(3).getUrl(),
                    holder.articleImage);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView articleImage;
        public RelativeLayout relativeLayout;
        public TextView titleText;
        public TextView abstractText;
        public TextView globalText;

        public ImageViewHolder(View itemView) {
            super(itemView);

            articleImage = (ImageView) itemView.findViewById(R.id.articleImageView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.infoRelativeLayout);
            titleText = (TextView) itemView.findViewById(R.id.titleTextView);
            abstractText = (TextView) itemView.findViewById(R.id.abstractTextView);
            globalText = (TextView) itemView.findViewById(R.id.globalTextView);
        }

    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout relativeLayout;
        public TextView titleText;
        public TextView abstractText;
        public TextView globalText;

        public SimpleViewHolder(View itemView) {
            super(itemView);

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.infoRelativeLayout);
            titleText = (TextView) itemView.findViewById(R.id.titleTextView);
            abstractText = (TextView) itemView.findViewById(R.id.abstractTextView);
            globalText = (TextView) itemView.findViewById(R.id.globalTextView);
        }

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}
