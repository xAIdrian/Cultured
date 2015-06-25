package com.androidtitan.trooptracker.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooptracker.Activity.ChampionActivity;
import com.androidtitan.trooptracker.Data.DatabaseHelper;
import com.androidtitan.trooptracker.Data.Soldier;

import java.util.List;

/**
 * Created by amohnacs on 6/19/15.
 */

/*
todo:   we need to take the implementation of highlighting our listview items in the adapter form ADDERFRAGMENT
todo:   and implement them in the getView here.
todo:   We might have to deal with passing the 'oldDivision' and 'divSelected' variables
todo:   maybe create some headers.
 */
public class ChampionAdapter extends BaseAdapter {

    private Activity context;
    DatabaseHelper databaseHelper;

    private List<Soldier> adapterData;
    private LayoutInflater layoutInflater;

    private int selection = -1;

    public ChampionAdapter(Activity acontext, List<Soldier> soldiers) {
        this.context = acontext;
        this.adapterData = soldiers;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        databaseHelper = DatabaseHelper.getInstance(context);

    }

    @Override
    public int getCount() {
        return adapterData.size();
    }

    @Override
    public Object getItem(int position) {
        return adapterData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return adapterData.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewHolder viewHolder;

        selection = ((ChampionActivity) context).getListViewSelection();

        if(convertView == null) {
            //inflate the listview_item_row.xml
            LayoutInflater li = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.listview_champion_item, null);

            viewHolder = new ListViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.firstItem = (TextView) convertView.findViewById(R.id.champ_text);

        }
        else {
            viewHolder = (ListViewHolder) convertView.getTag();
        }

        //ListViewSelection
        if (position == selection) {
            convertView.setBackgroundColor(0xCC448AFF);
        }
        else {
            convertView.setBackgroundColor(0xFFFFFFFF);
        }

        viewHolder.firstItem.setText(adapterData.get(position).getfName() + " "
                + adapterData.get(position).getlName());

        Log.e("CLFonItemClick", adapterData.get(position).getfName() + " " + adapterData.get(position).isSelected());

        return convertView;
    }

    class ListViewHolder {
        public TextView firstItem;
    }

    public void removeItem(int itemPosition) {
        //Remove the according group. Dont forget to remove the children aswell!

        adapterData.remove(itemPosition);
        notifyDataSetChanged();
        notifyDataSetInvalidated();

    }

}
