package com.androidtitan.culturedapp.main.newsfeed.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Triforce on 1/29/17.
 */

public class DataViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = getClass().getSimpleName();

    private Context context;
    private List<Object> dataViewList;

    public DataViewAdapter(Context context, List<Object> objectList) {
        this.context = context;
        this.dataViewList = objectList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dataViewList.size();
    }
}
