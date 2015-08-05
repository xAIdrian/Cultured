package com.androidtitan.trooptracker.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooptracker.Data.DatabaseHelper;
import com.androidtitan.trooptracker.Data.Soldier;
import com.androidtitan.trooptracker.Interface.ChampionDataPullInterface;
import com.androidtitan.trooptracker.Interface.ChampionInterface;

public class ChampionListFragment extends Fragment {
    private static final String TAG = "ChampionListFragment";
            //we are using a 'LoaderCallback' because Loaders should always be called from the main thread...your Activity
    private static final String[] PROJECTION = new String[] {"_id", "first", "last"};
    private static final int LOADER_ID = 1;
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;
    private SimpleCursorAdapter cursorAdapter;

    //we should find out whether these are PUBLIC, PRIVATE, STATIC
    DatabaseHelper databaseHelper;
    ChampionInterface championInterface;
    ChampionDataPullInterface pullInterface;

    private ImageView editer;
    private ImageView adder;
    private TextView proceedBtn;

    private TextView welcomeHeader;
    private ListView listView;

    private Animation slideIn;
    private Animation slideOut;
    private Animation rightSlideIn;
    private Animation rightSlideOut;

    private Soldier focusSoldier;

    private int position = -1;
    private int selection = -1;
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

        Cursor cursor = getListItems();
        if (cursor != null)
            cursor.moveToFirst();

        getActivity().startManagingCursor(cursor);  //this is deprecated.  Do we want to eventually consider using a 'Content Provider' and 'CursorLoader'?
        //Cursor Loader Implementation
        String[] dataColumns = new String[] {"first", "last"};
        int[] viewIDs = { R.id.champ_text };
        cursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.listview_champion_item,
                cursor, dataColumns, viewIDs, 0);


        //Animations
        slideIn = AnimationUtils.loadAnimation(getActivity(), R.anim.icon_slidein);
        slideOut = AnimationUtils.loadAnimation(getActivity(), R.anim.icon_slideout);
        rightSlideIn = AnimationUtils.loadAnimation(getActivity(), R.anim.slidein_right);
        rightSlideOut = AnimationUtils.loadAnimation(getActivity(), R.anim.slideout_right);

        //runnable placeholder

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listview_champion, container, false);

        editer = (ImageView) getActivity().findViewById(R.id.editBtn);
        editer.setVisibility(View.GONE);
        adder = (ImageView) getActivity().findViewById(R.id.addBtn);
        proceedBtn = (TextView) getActivity().findViewById(R.id.proceedBtn);
        proceedBtn.setVisibility(View.GONE);

        welcomeHeader = (TextView) v.findViewById(R.id.welcome_text);
        //welcomeHeader.setText(databaseHelper.getAllDivisions().get(receivedIndex).getName());

        listView = (ListView) v.findViewById(R.id.champion_list);
        listView.setAdapter(cursorAdapter);
        View emptyView = v.findViewById(R.id.empty);
        listView.setEmptyView(emptyView);

        //adapter = new ChampionCursorAdapter(getActivity(), getListItems());
        //listView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        //listView.invalidateViews();

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
                    proceedBtn.startAnimation(rightSlideOut);
                    listView.setEnabled(false);

                    handler.postDelayed(new Runnable() {
                        public void run() {
                            editer.setVisibility(View.GONE);
                            proceedBtn.setVisibility(View.GONE);

                            adder.setVisibility(View.VISIBLE);
                            adder.startAnimation(slideIn);

                            listView.setEnabled(true);
                        }
                    }, slideOut.getDuration());



                }
                //Selection of an item
                else {
                    view.setBackgroundColor(0xCCFFCD38);

                    //slidein animation
                    if (selection == -1) {

                        adder.startAnimation(slideOut);
                        listView.setEnabled(false);

                        handler.postDelayed(new Runnable() {
                            public void run() {
                                adder.setVisibility(View.GONE);

                                editer.setVisibility(View.VISIBLE);
                                editer.startAnimation(slideIn);

                                proceedBtn.setVisibility(View.VISIBLE);
                                proceedBtn.startAnimation(rightSlideIn);

                                listView.setEnabled(true);
                            }
                        }, slideOut.getDuration());
                    }
                    selection = position;

                }

                championInterface.setListViewSelection(position);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(selection == -1) {
                    //focusSoldier = troops.get(position);
                    for (Soldier s : databaseHelper.getAllSoldiers()) {
                        Log.e("troopChecker", "ID: " + s.getId() + "  Name: " + s.getfName() + " " + s.getlName());
                    }

                    //todo: our duplicate problem is not in the database
                    //todo:     the problem is in the adapter or what is populating our adapter
                    databaseHelper.printSoldierTable();
                }
                return false;
            }
        });

        editer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("CLFediter", "Position: " + position + " Selection: " + selection);
//todo
/*                championInterface.soldierPasser(selection, receivedIndex,
                        focusSoldier.getfName(),
                        focusSoldier.getlName());*/
            }
        });

        adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //when we receive our divIndex then that is what we will pass into this method
//todo
//                championInterface.soldierPasser(position, receivedIndex, null, null);
            }
        });

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final LocationManager manager = (LocationManager) getActivity()
                        .getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else {
                    championInterface.selectionToMap(selection);
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

        /*String selectQuery = "SELECT * FROM soldiers ts, divisions td, command tc WHERE td."
                + "name = '" + databaseHelper.getAllDivisions().get(receivedIndex).getName() + "' AND td."
                + "_id = tc.division_id AND ts._id = tc.soldier_id";*/

        Cursor cursor;

        if (receivedIndex == -1) {
            cursor = db.rawQuery("SELECT * FROM soldiers", null);
        } else {
            cursor = db.rawQuery("SELECT * FROM soldiers", null);
        }

        //todo
        databaseHelper.printSoldierTable();

        Log.e(TAG, String.valueOf(cursor));

        return cursor;
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


}


