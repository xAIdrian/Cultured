package com.androidtitan.trooptracker.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooptracker.Activity.MapsActivity;
import com.androidtitan.trooptracker.Adapter.ChampionCursorAdapter;
import com.androidtitan.trooptracker.Data.DatabaseHelper;
import com.androidtitan.trooptracker.Data.Soldier;
import com.androidtitan.trooptracker.Interface.ChampionDataPullInterface;
import com.androidtitan.trooptracker.Interface.ChampionInterface;

import java.util.List;

public class ChampionListFragment extends Fragment {

    //MapFragment mapFrag;

    DatabaseHelper databaseHelper;
    ChampionInterface championInterface;
    ChampionDataPullInterface pullInterface;

    ImageView editer;
    ImageView adder;
    TextView proceedBtn; //this will "slide" the fragment to

    TextView championHeader;
    ChampionCursorAdapter adapter;
    ListView listView;

    Animation slideIn;
    Animation slideOut;

    Soldier focusSoldier;
    List<Soldier> troops;

    int position = -1;
    int selection = -1;
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

        slideIn = AnimationUtils.loadAnimation(getActivity(), R.anim.icon_slidein);
        slideOut = AnimationUtils.loadAnimation(getActivity(), R.anim.icon_slideout);

        Runnable run = new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                listView.invalidateViews();
            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listview_champion, container, false);

        editer = (ImageView) getActivity().findViewById(R.id.editBtn);
        editer.setVisibility(View.GONE);
        adder = (ImageView) getActivity().findViewById(R.id.addBtn);
        proceedBtn = (TextView) getActivity().findViewById(R.id.proceedBtn);

        championHeader = (TextView) v.findViewById(R.id.championHeader);
        championHeader.setText(databaseHelper.getAllDivisions().get(receivedIndex).getName());

        listView = (ListView) v.findViewById(R.id.championList);

        adapter = new ChampionCursorAdapter(getActivity(), getListItems());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Handler handler = new Handler();

                for (int i = 0; i <= listView.getLastVisiblePosition() - listView.getFirstVisiblePosition(); i++) {
                    View item = listView.getChildAt(i);
                    item.setBackgroundColor(0xFFFFFFFF);
                }

                //ITEM HIGHLIGHT

                //Deselection of an item
                if (selection == position) {
                    view.setBackgroundColor(0xFFFFFFFF);

                    selection = -1;

                    editer.startAnimation(slideOut);
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            //Extra work goes here
                            editer.setVisibility(View.GONE);

                            adder.setVisibility(View.VISIBLE);
                            adder.startAnimation(slideIn);
                        }
                    }, slideOut.getDuration());

                }
                //Selection of an item
                else {
                    view.setBackgroundColor(0xCCFFCD38);

                    //slidein animation
                    if (selection == -1) {

                        adder.startAnimation(slideOut);
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                //  .clearAnimation();
                                //Extra work goes here
                                adder.setVisibility(View.GONE);

                                editer.setVisibility(View.VISIBLE);
                                editer.startAnimation(slideIn);
                            }
                        }, slideOut.getDuration());
                    }

                    selection = position;

                }

                championInterface.setListViewSelection(position);

            }
        });



/*
        DELETION

                            cursorUpdate REQUIRED

                            Division focusDivision = databaseHelper.getAllDivisions().get(receivedIndex);
                            Soldier focusSoldier = databaseHelper.getAllSoldiersByDivision(focusDivision)
                                    .get(position);
                            int focusId = (int)focusSoldier.getId();


                            Log.e("onSwipe", String.valueOf(position));
                            databaseHelper.deleteSoldier(focusSoldier); //positions

                            swipeAdapter.notifyDataSetChanged();
                            listView.invalidateViews();

                            break;
                        */

                            //this will populate our Adder Fragment




        editer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("CLFediter", "Position: " + position + " Selection: " + selection);

                focusSoldier = troops.get(selection);

                championInterface.soldierPasser(selection, receivedIndex,
                        focusSoldier.getfName(),
                        focusSoldier.getlName());
            }
        });

        adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position != -1) {
                    focusSoldier = troops.get(position);
                }

                //when we receive our divIndex then that is what we will pass into this method
                championInterface.soldierPasser(position, receivedIndex, null, null);
            }
        });

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*for (Soldier s : troops) {
                    Log.e("troopChecker", "ID: " + s.getId() + "  Name: " + s.getfName() + " " + s.getlName());
                }*/

                if(selection != -1) {
                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                    startActivity(intent);
                }

                /*
                This is requesting an application that can handle the action.
                In this case it is our Maps by Google app.

                Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(mapIntent);
                }*/


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


}


