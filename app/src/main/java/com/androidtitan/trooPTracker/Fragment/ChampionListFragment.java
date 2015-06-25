package com.androidtitan.trooptracker.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooptracker.Adapter.ChampionAdapter;
import com.androidtitan.trooptracker.Data.DatabaseHelper;
import com.androidtitan.trooptracker.Data.Division;
import com.androidtitan.trooptracker.Data.Soldier;
import com.androidtitan.trooptracker.Interface.ChampionDataPullInterface;
import com.androidtitan.trooptracker.Interface.ChampionInterface;

import java.util.ArrayList;
import java.util.List;

public class ChampionListFragment extends Fragment {
    DatabaseHelper databaseHelper;
    ChampionInterface championInterface;
    ChampionDataPullInterface pullInterface;

    ImageView deleter;
    ImageView editer;
    ImageView adder;
    TextView proceedBtn; //this will "slide" the fragment to

    TextView championHeader;
    ChampionAdapter adapter;
    ListView listView;

    Division focusDivision;
    Soldier focusSoldier;
    ArrayList<Soldier> soldierItems;
    List<Soldier> troops;

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

        soldierItems = new ArrayList<Soldier>();

        troops = databaseHelper.getAllSoldiersByDivision(databaseHelper.getAllDivisions().get(receivedIndex));
        soldierItems.addAll(troops);


        Runnable runnable = new Runnable() {
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

        deleter = (ImageView) getActivity().findViewById(R.id.deleteBtn);
        editer = (ImageView)  getActivity().findViewById(R.id.editBtn);
        adder = (ImageView) getActivity().findViewById(R.id.addBtn);

        championHeader = (TextView) v.findViewById(R.id.championHeader);
        championHeader.setText(databaseHelper.getAllDivisions().get(receivedIndex).getName());

        listView = (ListView) v.findViewById(R.id.championList);
        adapter = new ChampionAdapter(getActivity(), getListItems());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!troops.get(position).isSelected()) {

                    troops.get(position).setIsSelected(true);

                    if (selection != -1) {
                        troops.get(selection).setIsSelected(false);
                    }

                    selection = position;
                    championInterface.setListViewSelection(selection);

                }
                else {

                    troops.get(position).setIsSelected(false);

                    selection = -1;
                    listView.invalidateViews();

                    championInterface.setListViewSelection(-1);
                }

                for (int i = 0; i <= listView.getLastVisiblePosition() - listView.getFirstVisiblePosition(); i++) {

                    View item = listView.getChildAt(i);
                    item.setBackgroundColor(0xFFFFFFFF);
                }
                view.setBackgroundColor(0xCC448AFF);

                Log.e("CLFonItemClick", troops.get(position).getfName() + " " + troops.get(position).isSelected());

            }
        });

        deleter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selection != -1) {

                    focusDivision = databaseHelper.getAllDivisions().get(receivedIndex);
                    focusSoldier = databaseHelper.getAllSoldiersByDivision(focusDivision).get(selection);

                    for(Soldier s : troops) {
                        s.setIsSelected(false);
                    }

                    databaseHelper.deleteSoldier(focusSoldier);

                    adapter.removeItem(selection);

                    selection = -1;
                    Log.e("CLFdeleter", "selection: " + String.valueOf(selection));

                    championInterface.setListViewSelection(-1);

                }

            }
        });

        editer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CFLediter", "selection: " + String.valueOf(selection));

                if(selection != -1) {
                    //this will populate our Adder Fragment
                    focusSoldier = soldierItems.get(selection);

                    for(Soldier s : troops) {
                        s.setIsSelected(false);
                    }
                    championInterface.soldierPasser(selection, receivedIndex,
                            focusSoldier.getfName(),
                            focusSoldier.getlName());
                }
            }
        });

        adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selection != -1) {
                    focusSoldier = soldierItems.get(selection);
                    focusSoldier.setIsSelected(false);
                }

                //when we receive our divIndex then that is what we will pass into this method
                championInterface.soldierPasser(selection, receivedIndex, null, null);
            }
        });

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        championInterface = null;
    }

    private List<Soldier> getListItems() {
        ArrayList<Soldier> soldierItems = new ArrayList<Soldier>();

        if (receivedIndex == -1) {
            soldierItems.addAll(databaseHelper.getAllSoldiers());

        } else {
            soldierItems.addAll(databaseHelper.getAllSoldiersByDivision(databaseHelper.getAllDivisions().get(receivedIndex)));
        }
        return soldierItems;
    }


}
