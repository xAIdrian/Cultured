package com.androidtitan.alphaarmyapp.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.androidtitan.alphaarmyapp.Fragment.AdderFragment;
import com.androidtitan.alphaarmyapp.Dialog.DialogListFragment;
import com.androidtitan.alphaarmyapp.Interface.SecondF2AInterface;
import com.androidtitan.alphaarmyapp.R;


public class SecondActivity extends ActionBarActivity implements SecondF2AInterface{
    private final String ADD_FRAG_TAG = "adderTag";

    private FragmentManager fragMag;
    private FragmentTransaction fragTran;
    private AdderFragment adderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //onOrientationChange Block
        if(savedInstanceState != null) {
            //savedInstanceState, fragment may exist. Look up the instance that already exists by tag
            adderFragment = (AdderFragment) getFragmentManager().findFragmentByTag(ADD_FRAG_TAG);
        }
        else if(adderFragment == null) {
            adderFragment = new AdderFragment();
        }
        if(!adderFragment.isInLayout()) {
            fragMag = getFragmentManager();
            fragTran = fragMag.beginTransaction();
            fragTran.addToBackStack(null).replace(R.id.container2, adderFragment, ADD_FRAG_TAG).commit();
        }

        //hide actionbar
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //called from SecondF2AInterface. Creates AdderFragment and passes information to populate editTets
    @Override
    public void soldierInfo(String first, String last, String specialty) {
        Bundle args = new Bundle();
        args.putString("Fpassable", first);
        args.putString("Lpassable", last);
        args.putString("Spassable", specialty);

        FragmentManager manager = getFragmentManager();
        DialogListFragment dialog = new DialogListFragment();
        dialog.setArguments(args);

        dialog.show(manager, "dialog");
    }

    //called from SecondF2AInterface.  Passes integer so main activity can page to
    //newly added soldier's division
    @Override
    public void tabInteraction(int selection) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("viewpagerIndex", selection);
        startActivity(intent);
    }
}
