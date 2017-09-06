package com.androidtitan.culturedapp.main.no_internet;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.databinding.NoInternetListitemBinding;
import com.androidtitan.culturedapp.model.newyorktimes.Article;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by amohnacs on 9/6/17.
 */

public class NoInternetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = NoInternetAdapter.class.getSimpleName();

    private Context context;
    private List<Article> offlineArticles;

    public NoInternetAdapter(Context context, List<Article> offlineArticles) {
        this.context = context;
        this.offlineArticles = offlineArticles;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        NoInternetListitemBinding listBinding = DataBindingUtil.inflate(layoutInflater, R.layout.no_internet_listitem, parent, false);
// TODO: 9/6/17 the rest is the same as our regular binding. we are going to need to include an EventHandler

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return offlineArticles.size();
    }

    // TODO: 9/6/17 your viewholder class goes here
}
