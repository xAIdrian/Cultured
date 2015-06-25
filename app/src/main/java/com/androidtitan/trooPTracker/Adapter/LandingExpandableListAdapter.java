package com.androidtitan.trooptracker.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooptracker.Activity.LandingActivity;
import com.androidtitan.trooptracker.Data.DatabaseHelper;
import com.androidtitan.trooptracker.Data.Division;

import java.util.List;

/**
 * Created by amohnacs on 6/5/15.
 */
public class LandingExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private DatabaseHelper databaseHelper;

    private LayoutInflater Linflater;

    private List<Division> adapterData;
    private String[] childItems;

    private int soldierCount;

    /*
    Children titles, these will be pulls from the database
      columns will need to be added for # of views

      private String soldierCount = group.getAllSoldiers().size()
      privte String groupSaves = group.getAllSaves()
      private String groupViews = groups.get(i).getViews()

    */

    private int index = 0;

    public LandingExpandableListAdapter(Activity actContext, List<Division> groups) {
        databaseHelper = databaseHelper.getInstance(actContext);

        this.context = actContext;
        this.adapterData = groups;
        childItems = new String[] {"Troop Count: " + soldierCount, "Saved Places: ", "Times Viewed: "};
        Linflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return adapterData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childItems.length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return adapterData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childItems[childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return adapterData.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, final View convertView, ViewGroup parent) {
        View v = convertView;
        ExpandableListViewHolder viewHolder;

        if (convertView == null) {/*
            LayoutInflater li = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
            v = Linflater.inflate(R.layout.expandablelistview_group_item, null);
            viewHolder = new ExpandableListViewHolder(v);
            v.setTag(viewHolder);
        }
        else {
            viewHolder = (ExpandableListViewHolder) v.getTag();
        }

        viewHolder.divisionTitle.setText(adapterData.get(groupPosition).getName());

        viewHolder.viewBtn.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starting Main Activity. soldierListOpener will set the divisionIndex
                ((LandingActivity)context).upTick(adapterData.get(groupPosition));
                ((LandingActivity)context).soldierListOpener(groupPosition);
            }
        });

        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        childItems[0] = "Troop Count:   " + databaseHelper.getAllSoldiersByDivision(adapterData.get(groupPosition)).size();
        childItems[2] = "Times Viewd:   " + adapterData.get(groupPosition).getVisits();

        //index is used to step through our String array as a means to populate
        if(index > 2) {
            index = 0;
        }

        final String child = childItems[index];
        TextView text;

        if(convertView == null) {
            convertView = Linflater.inflate(R.layout.expandablelistview_child_item, null);
        }
        text = (TextView) convertView.findViewById(R.id.divListItem);
        text.setText(child);
        index++;

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }



    private class ExpandableListViewHolder {
        public TextView divisionTitle;
        public TextView viewBtn;

        public ExpandableListViewHolder(View view) {
            divisionTitle = (TextView) view.findViewById(R.id.divGroupListItem);
            viewBtn = (TextView) view.findViewById(R.id.viewDivBtn);

        }
    }



    public void removeGroup(int group) {
        //Remove the according group. Dont forget to remove the children aswell!

        adapterData.remove(group);
        notifyDataSetChanged();
    }
}
