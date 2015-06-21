package com.androidtitan.trooPTracker.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooPTracker.Activity.LandingActivity;
import com.androidtitan.trooPTracker.Data.DatabaseHelper;
import com.androidtitan.trooPTracker.Data.Division;

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
public class LandingAdapter extends BaseAdapter {

    private Activity context;
    DatabaseHelper databaseHelper;

    private List<Division> adapterData;
    private LayoutInflater layoutInflater;

    public LandingAdapter(Activity acontext, List<Division> soldiers) {
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
        View v = convertView;
        ListViewHolder viewHolder;


        if(convertView == null) {
            //inflate the listview_item_row.xml
            LayoutInflater li = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.expandablelistview_group_item, null);

            viewHolder = new ListViewHolder(v);
            v.setTag(viewHolder);
        }
        else {
            viewHolder = (ListViewHolder) v.getTag();
        }

        viewHolder.firstItem.setText(adapterData.get(position).getName());

        //here we are attaching the position to the TextView and passing it down to the click event
        viewHolder.viewBtn.setTag(position);

        viewHolder.viewBtn.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                ((LandingActivity) context).upTick(adapterData.get(position));
                ((LandingActivity) context).soldierListOpener(position);
            }
        });

        return v;
    }

    class ListViewHolder {
        public TextView firstItem;
        public Button viewBtn;

        public ListViewHolder(View view) {
            firstItem = (TextView) view.findViewById(R.id.divGroupListItem);
            viewBtn = (Button) view.findViewById(R.id.viewDivBtn);
        }
    }

    public void removeItem(int itemPosition) {
        //Remove the according group. Dont forget to remove the children aswell!

        adapterData.remove(itemPosition);
        notifyDataSetChanged();
        notifyDataSetInvalidated();
    }
}
