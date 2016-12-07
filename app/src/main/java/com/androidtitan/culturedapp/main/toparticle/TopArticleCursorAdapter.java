
package com.androidtitan.culturedapp.main.toparticle;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.androidtitan.culturedapp.common.view.CursorRecyclerViewAdapter;



import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.androidtitan.culturedapp.common.view.CursorRecyclerViewAdapter;

/**
 * Created by amohnacs on 10/1/16.
 */


public class TopArticleCursorAdapter extends CursorRecyclerViewAdapter<TopArticleCursorAdapter.ViewHolder> {
    private final String TAG = getClass().getSimpleName();

    private Context context;
    private Cursor cursor;

    public TopArticleCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);

        this.context = context;
        this.cursor = cursor;


    }

    @Override
    public TopArticleCursorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TopArticleCursorAdapter.ViewHolder viewHolder, Cursor cursor) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

