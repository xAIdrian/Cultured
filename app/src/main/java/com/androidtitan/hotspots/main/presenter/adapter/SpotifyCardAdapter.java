package com.androidtitan.hotspots.main.presenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.main.application.App;
import com.androidtitan.hotspots.main.model.Item;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by amohnacs on 3/3/16.
 */
public class SpotifyCardAdapter extends RecyclerView.Adapter<SpotifyCardAdapter.ViewHolder> {

    // @Inject private ImageDownloader imageDownloader;

    @Inject Context context;
    private List<Item> trackList;

    public SpotifyCardAdapter(Context context, List<Item> adapterTrackList) {
        //this.imageDownloader = new ImageDownloader();

        App.getMainPresenterComponent().inject(this);
        this.trackList = adapterTrackList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //public ImageView albumImage;
        public TextView titleText;

        public ViewHolder(View itemView) {
            super(itemView);

            //albumImage = (ImageView) itemView.findViewById(R.id.albumImageView);
            titleText = (TextView) itemView.findViewById(R.id.titleTextView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*imageDownloader.imageDownload(trackList.get(position).getImage300Link(),
                holder.albumImage);*/
        holder.titleText.setText(trackList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }


}
