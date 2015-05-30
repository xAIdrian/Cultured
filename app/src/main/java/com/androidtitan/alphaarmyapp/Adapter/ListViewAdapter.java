package com.androidtitan.alphaarmyapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.Data.Soldier;
import com.androidtitan.alphaarmyapp.R;

import java.util.List;

/**
 * Created by A. Mohnacs on 5/15/2015.
 */
public class ListViewAdapter extends BaseAdapter {

    private Activity context;
    private List<Soldier> adapterData;
    private LayoutInflater layoutInflater;

    public ListViewAdapter(Activity acontext, List<Soldier> soldiers) {
        this.context = acontext;
        this.adapterData = soldiers;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View v = convertView;
        ListViewHolder viewHolder;
        LayoutInflater li = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {
            //inflate the listview_item_row.xml
            v = li.inflate(R.layout.listview_item, null);

            viewHolder = new ListViewHolder(v);
            v.setTag(viewHolder);
        }
        else {
            viewHolder = (ListViewHolder) v.getTag();
        }

        try {
            viewHolder.firstItem.setText(adapterData.get(position).getfName() + " " +adapterData.get(position).getlName());
            viewHolder.lastItem.setText(adapterData.get(position).getSpecialty());

        } catch (NullPointerException e) {
            Log.e("LVAgetView", e.toString());
            v = li.inflate(R.layout.listview_item, null);

            viewHolder = new ListViewHolder(v);
            //v.setTag(viewHolder);

            viewHolder.firstItem.setText(adapterData.get(position).getfName() + " " +adapterData.get(position).getlName());
            viewHolder.lastItem.setText(adapterData.get(position).getSpecialty());
        }



        return v;
    }

    class ListViewHolder {
        public TextView firstItem;
        public TextView lastItem;

        public ListViewHolder(View view) {
            firstItem = (TextView) view.findViewById(R.id.text_1);
            lastItem = (TextView) view.findViewById(R.id.text_2);
        }
    }
}
