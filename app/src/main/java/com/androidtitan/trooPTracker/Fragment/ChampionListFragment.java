package com.androidtitan.trooptracker.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooptracker.Adapter.ChampionCursorAdapter;
import com.androidtitan.trooptracker.Data.DatabaseHelper;
import com.androidtitan.trooptracker.Data.Soldier;
import com.androidtitan.trooptracker.Interface.ChampionDataPullInterface;
import com.androidtitan.trooptracker.Interface.ChampionInterface;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirections;

import java.util.List;

public class ChampionListFragment extends Fragment {
    DatabaseHelper databaseHelper;
    ChampionInterface championInterface;
    ChampionDataPullInterface pullInterface;

    ImageView adder;
    TextView proceedBtn; //this will "slide" the fragment to

    TextView championHeader;
    SwipeActionAdapter swipeAdapter;
    ChampionCursorAdapter adapter;
    ListView listView;

    Soldier focusSoldier;
    List<Soldier> troops;

    int position = -1;
    public int receivedIndex = -1;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            championInterface = (ChampionInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        try {
            pullInterface = (ChampionDataPullInterface) activity;
        } catch(ClassCastException e) {

            throw new ClassCastException(activity.toString()
                    + " must implement MainDataPullInterface");
        }

        receivedIndex = pullInterface.getDivisionIndex();
    }

    public ChampionListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
        }

        databaseHelper = DatabaseHelper.getInstance(getActivity());

        troops = databaseHelper.getAllSoldiersByDivision(
                databaseHelper.getAllDivisions().get(receivedIndex));

        /*
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                adapter.notifyDataSetInvalidated();
                listView.invalidateViews();
            }
        };
        */

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listview_champion, container, false);

        adder = (ImageView) getActivity().findViewById(R.id.addBtn);
        proceedBtn = (TextView) getActivity().findViewById(R.id.proceedBtn);

        championHeader = (TextView) v.findViewById(R.id.championHeader);
        championHeader.setText(databaseHelper.getAllDivisions().get(receivedIndex).getName());

        listView = (ListView) v.findViewById(R.id.championList);

        //implementation of swipe view adapter
        adapter = new ChampionCursorAdapter(getActivity(), getListItems());
        swipeAdapter = new SwipeActionAdapter(adapter);
        swipeAdapter.setListView(listView);

        listView.setAdapter(swipeAdapter);

        swipeAdapter.addBackground(SwipeDirections.DIRECTION_NORMAL_LEFT, R.layout.champion_swipe_left)
                    .addBackground(SwipeDirections.DIRECTION_NORMAL_RIGHT, R.layout.champion_swipe_right)
                    .addBackground(SwipeDirections.DIRECTION_NORMAL_LEFT, R.layout.champion_swipe_left)
                    .addBackground(SwipeDirections.DIRECTION_FAR_RIGHT, R.layout.champion_swipe_right);

        swipeAdapter.setFixedBackgrounds(true);
        swipeAdapter.setNormalSwipeFraction(0); //every swipe will register
        swipeAdapter.setFarSwipeFraction(1);    //setting to 1 should disable action

        //todo
        // Listen to swipes
        swipeAdapter.setSwipeActionListener(new SwipeActionAdapter.SwipeActionListener() {
            @Override
            public boolean hasActions(int position) {
                // All items can be swiped
                return true;
            }

            @Override
            public boolean shouldDismiss(int position, int direction) {
                // Only dismiss an item when swiping normal left
                //direction == SwipeDirections.DIRECTION_NORMAL_LEFT;
                return true;
            }


            @Override
            public void onSwipe(int[] positionList, int[] directionList) {
                for (int i = 0; i < positionList.length; i++) {
                    int direction = directionList[i];
                    position = positionList[i];
                    String dir = "";

                    switch (direction) {

                        case SwipeDirections.DIRECTION_NORMAL_LEFT:
/*
                            cursorUpdate REQUIRED

                            Division focusDivision = databaseHelper.getAllDivisions().get(receivedIndex);
                            Soldier focusSoldier = databaseHelper.getAllSoldiersByDivision(focusDivision)
                                    .get(position);
                            int focusId = (int)focusSoldier.getId();

                            for (Soldier s : databaseHelper.getAllSoldiersByDivision(focusDivision)) {
                                s.setIsSelected(false);
                            }

                            Log.e("onSwipe", String.valueOf(position));
                            databaseHelper.deleteSoldier(focusSoldier); //positions

                            swipeAdapter.notifyDataSetChanged();
                            listView.invalidateViews();

                            break;
                        */
                        case SwipeDirections.DIRECTION_NORMAL_RIGHT:

                            //this will populate our Adder Fragment
                            focusSoldier = troops.get(position);

                            for (Soldier s : troops) {
                                s.setIsSelected(false);
                            }
                            championInterface.soldierPasser(position, receivedIndex,
                                    focusSoldier.getfName(),
                                    focusSoldier.getlName());

                            break;
                    }

                    //swipeAdapter.notifyDataSetChanged();
                    //listView.invalidateViews();
                }
            }
        });


        adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position != -1) {
                    focusSoldier = troops.get(position);
                    focusSoldier.setIsSelected(false);
                }

                //when we receive our divIndex then that is what we will pass into this method
                championInterface.soldierPasser(position, receivedIndex, null, null);
            }
        });

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Soldier s : troops) {
                    Log.e("troopChecker", "ID: " + s.getId() + "  Name: " + s.getfName() + " " + s.getlName());
                }
            }
        });

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        championInterface = null;
    }

    private Cursor getListItems() {

        //DatabaseHandler is a SQLiteOpenHelper class connecting to SQLite
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());
        // Get access to the underlying writeable database
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        // Query for items from the database and get a cursor back
        String selectQuery = "SELECT * FROM soldiers ts, divisions td, command tc WHERE td."
                + "name = '" + databaseHelper.getAllDivisions().get(receivedIndex).getName() + "' AND td."
                + "_id = tc.division_id AND ts._id = tc.soldier_id";

        Cursor cursor;
        if (receivedIndex == -1) {
            cursor = db.rawQuery("SELECT * FROM soldiers", null);
        } else {
            cursor = db.rawQuery(selectQuery, null);
        }
        return cursor;
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }


    public class FilterCursorWrapper extends CursorWrapper {
        private int[] index;
        private int count = 0;
        private int pos = 0;

        public boolean isHidden(String path) {

            // the logic to check where this item should be hidden

            //   if (some condintion)
            //      return false;
            //    else {
            //       return true;
            //   }

            return false;

        }

        /*
        CursorWrapper implements the Cursor interface, so you can pass your CursorWrapper to your
        CursorAdapter in place of your Cursor.

        CursorWrapper is a Wrapper class for Cursor that delegates all of its calls to the
        actual cursor object. As the documentation states, the primary use for this class is to extend
        a cursor while overriding only a subset of its methods.

        'this' refers to the FilterCursorWrapper, thus the Cursor

        for "column" parameter. we could pass a String[] and edit where a single column is referenced.

        http://developer.xamarin.com/api/type/Android.Database.CursorWrapper/
  h     http://stackoverflow.com/questions/5041499/how-to-hide-an-item-in-a-listview-in-android
         */
        public FilterCursorWrapper(Cursor cursor, boolean doFilter, int column) {
            super(cursor);

            if (doFilter) {

                this.count = super.getCount();
                this.index = new int[this.count];

                for (int i = 0; i < this.count; i++) {
                    super.moveToPosition(i);

                    if (!isHidden(this.getString(column)))
                        this.index[this.pos++] = i;
                }

                this.count = this.pos;
                this.pos = 0;

                super.moveToFirst();

            } else {

                this.count = super.getCount();
                this.index = new int[this.count];

                for (int i = 0; i < this.count; i++) {
                    this.index[i] = i;
                }
            }
        }

        @Override
        public boolean move(int offset) {
            return this.moveToPosition(this.pos + offset);
        }

        @Override
        public boolean moveToNext() {
            return this.moveToPosition(this.pos + 1);
        }

        @Override
        public boolean moveToPrevious() {
            return this.moveToPosition(this.pos - 1);
        }

        @Override
        public boolean moveToFirst() {
            return this.moveToPosition(0);
        }

        @Override
        public boolean moveToLast() {
            return this.moveToPosition(this.count - 1);
        }

        @Override
        public boolean moveToPosition(int position) {

            if (position >= this.count || position < 0)
                return false;

            return super.moveToPosition(this.index[position]);
        }

        @Override
        public int getCount() {
            return this.count;
        }

        @Override
        public int getPosition() {
            return this.pos;
        }
    }

}


