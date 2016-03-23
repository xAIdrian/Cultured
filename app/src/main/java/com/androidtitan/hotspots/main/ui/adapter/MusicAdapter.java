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
import com.androidtitan.hotspots.main.model.spotify.Item;
import com.androidtitan.hotspots.main.util.ImageDownloader;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by amohnacs on 3/3/16.
 */
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private final String TAG = getClass().getSimpleName();

    ImageDownloader imageDownloader;

    private Context context;
    private List<Item> trackList;


    @Inject
    public MusicAdapter(Context context, List<Item> adapterTrackList) {

        this.context = context;
        this.trackList = adapterTrackList;

        imageDownloader = new ImageDownloader();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView albumImage;
        public RelativeLayout relativeLayout;
        public TextView trackText;
        public TextView artistText;
        public TextView albumText;

        public ViewHolder(View itemView) {
            super(itemView);

            albumImage = (ImageView) itemView.findViewById(R.id.albumImageView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.infoRelativeLayout);
            trackText = (TextView) itemView.findViewById(R.id.trackTextView);
            artistText = (TextView) itemView.findViewById(R.id.artistTextView);
            albumText = (TextView) itemView.findViewById(R.id.albumTextView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.trackText.setText(trackList.get(position).getName());
        holder.albumText.setText(trackList.get(position).getAlbum().getName());

        StringBuilder artistsStringBuilder = new StringBuilder();
        for(int i = 0; i < trackList.get(position).getArtists().size(); i++) {
            if(i == 0) {
                artistsStringBuilder.append(
                        trackList.get(position).getArtists().get(i).getName());
            } else {
                artistsStringBuilder.append(", ");
                artistsStringBuilder.append(
                        trackList.get(position).getArtists().get(i).getName());
            }
        }
        holder.artistText.setText(artistsStringBuilder.toString());

        imageDownloader.imageDownload(trackList.get(position).getAlbum().getImages().get(0).getUrl(),
                holder.albumImage);


    }

    @Override
    public int getItemCount() {
     return trackList.size();
    }


}
