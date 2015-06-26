package com.androidtitan.trooptracker.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.R;

/**
 * Created by amohnacs on 6/25/15.
 */
public class ChampionCursorAdapter extends CursorAdapter {


    public ChampionCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listview_champion_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView champTextView = (TextView) view.findViewById(R.id.champ_text);

        //extract the string from cursor
        String firstText = cursor.getString(cursor.getColumnIndexOrThrow("first"));
        String lastText = cursor.getString(cursor.getColumnIndexOrThrow("last"));

        //populate fields
        champTextView.setText(firstText + " " + lastText);

    }
}
