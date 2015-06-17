package com.androidtitan.trooPTracker.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooPTracker.Data.DatabaseHelper;
import com.androidtitan.trooPTracker.Data.Division;
import com.androidtitan.trooPTracker.Data.Soldier;
import com.androidtitan.trooPTracker.Interface.ChampionDataPullInterface;
import com.androidtitan.trooPTracker.Interface.ChampionInterface;

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

        databaseHelper = DatabaseHelper.getInstance(getActivity());

        soldierItems = new ArrayList<Soldier>();
        soldierItems.addAll(databaseHelper.
                getAllSoldiersByDivision(databaseHelper.getAllDivisions().get(receivedIndex)));

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
        adapter = new ChampionAdapter((ArrayList) getListItems());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < adapter.getCount(); i++) {
                    View item = listView.getChildAt(i);
                    item.setBackgroundColor(0xFFFFFFFF);
                }
                if(selection == position) {
                    view.setBackgroundColor(0xFFFFFFFF);
                    selection = -1;
                }
                else {
                    view.setBackgroundColor(0xCC448AFF);
                    selection = position;
                }

                selection = position;
            }
        });

        deleter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selection != -1) {

                    focusDivision = databaseHelper.getAllDivisions().get(receivedIndex);
                    focusSoldier = databaseHelper.getAllSoldiersByDivision(focusDivision).get(selection);

                    databaseHelper.deleteSoldier(focusSoldier);

                    //The built in clear and add items automatically call notifyDataSetChanged();
                    adapter.clear();
                    adapter.addAll(getListItems());

                    selection = -1;

                }

            }
        });

        editer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selection != -1) {
                    //this will populate our Adder Fragment
                    focusSoldier = soldierItems.get(selection);
                    championInterface.soldierPasser(selection, receivedIndex,
                            focusSoldier.getfName(),
                            focusSoldier.getlName());
                }
            }
        });

        adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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



    //ADAPTER CLASS

    private class ChampionAdapter extends ArrayAdapter<Soldier> {

        ArrayList<Soldier> soldierItems = new ArrayList<Soldier>();

        public ChampionAdapter(ArrayList<Soldier> troops) {
            super(getActivity(), 0, troops); // 0 is our resource
            soldierItems = troops;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //if you weren't given a view inflate one
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.listview_champion_item, null);
            }

            Soldier soldier = soldierItems.get(position);
            final TextView checkedText = (TextView) convertView.findViewById(R.id.champ_text);
            checkedText.setText(soldier.getlName() + ", " + soldier.getfName());

            return convertView;
        }
    }

}
