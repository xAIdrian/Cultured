package com.androidtitan.trooptracker.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooptracker.Activity.ChampionActivity;

/**
 * Created by amohnacs on 6/25/15.
 */
public class ChampionCursorAdapter extends CursorAdapter {

    int selection;
    int divSelection;

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
    // bindView constructs each individual row. Similar to an adapter getView()
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        selection = ((ChampionActivity) context).getListViewSelection();
        divSelection = ((ChampionActivity) context).getDivisionIndex();

        TextView champTextView = (TextView) view.findViewById(R.id.champ_text);

        //extract the string from cursor
        String firstText = cursor.getString(cursor.getColumnIndexOrThrow("first"));
        String lastText = cursor.getString(cursor.getColumnIndexOrThrow("last"));

        //populate fields
        champTextView.setText(firstText + " " + lastText);


       //ListViewSelection
        //and establishing persistance of highlight when scrolled
        if (selection != -1) {

            for (int i = 0; i < cursor.getCount(); i++)

                if (cursor.getPosition() == selection) {
                    view.setBackgroundColor(0xCCFFCD38);
                } else {

                    view.setBackgroundColor(0xFFFFFFFF);
                }
        }
        Log.e("CCAbindView", "Selection: " + selection + ", Position: " + cursor.getPosition()
            + ", " + cursor.getString(cursor.getColumnIndexOrThrow("first")) + " "
                + cursor.getString(cursor.getColumnIndexOrThrow("last")));

    }
}
