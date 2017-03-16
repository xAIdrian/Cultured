package com.androidtitan.culturedapp.common.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtitan.culturedapp.R;

/**
 * Created by Adrian Mohnacs on 3/1/17.
 */

public class ContinentPreferenceAdapter extends BaseAdapter {

    private final Context context;
    private CharSequence[] entries;
    private CharSequence[ ] entryValues;

    private final LayoutInflater inflater;
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    private final RowInteractionInterface rowInterface;

    public ContinentPreferenceAdapter(RowInteractionInterface rowInterface, Context context,
                                      CharSequence[] entryValues, CharSequence[] entries) {
        this.rowInterface = rowInterface;

        this.entryValues = entryValues;
        this.entries = entries;
        this.context = context;

        this.inflater = LayoutInflater.from(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    @Override
    public int getCount() {
        return entries.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        SimplePreferenceViewHolder viewHolder = null;

        if(row == null) {
            row = inflater.inflate(R.layout.continent_preference_row, parent, false);
            viewHolder = new SimplePreferenceViewHolder(row, position);
            row.setTag(viewHolder);
        }

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rowInterface.onRowClick(position);

//                int index = position;
//                int value = Integer.valueOf((String) entryValues[index]);
//                editor.putInt("PREFERENCES_LISTVIEW", value);

                //todo: passed in interface to from parent preference to dismiss the dialog
            }
        });

        return row;
    }

    class SimplePreferenceViewHolder {

        private TextView primaryText;
        private TextView secondaryText;

        public SimplePreferenceViewHolder(View view, int position) {

            primaryText = (TextView) view.findViewById(R.id.primaryTextView);
            primaryText.setText(entries[position]);

        }
    }

    public interface RowInteractionInterface {

        void onRowClick(int position);
    }
}
