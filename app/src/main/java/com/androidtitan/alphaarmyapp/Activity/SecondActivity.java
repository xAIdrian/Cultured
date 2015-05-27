package com.androidtitan.alphaarmyapp.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.androidtitan.alphaarmyapp.Fragment.AdderFragment;
import com.androidtitan.alphaarmyapp.Fragment.ListDialogFragment;
import com.androidtitan.alphaarmyapp.Interface.SecondF2AInterface;
import com.androidtitan.alphaarmyapp.R;


public class SecondActivity extends ActionBarActivity implements SecondF2AInterface{

    private FragmentManager fragMag;
    private FragmentTransaction fragTran;
    private AdderFragment adderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        fragMag = getFragmentManager();
        fragTran = fragMag.beginTransaction();
        adderFragment = new AdderFragment();
        fragTran.addToBackStack(null).add(R.id.container2, adderFragment, "adderFrag").commit();

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

    @Override
    public void soldierInfo(String first, String last, String specialty) {
        Bundle args = new Bundle();
        args.putString("Fpassable", first);
        args.putString("Lpassable", last);
        args.putString("Spassable", specialty);

        FragmentManager manager = getFragmentManager();
        ListDialogFragment dialog = new ListDialogFragment();
        dialog.setArguments(args);

        dialog.show(manager, "dialog");
    }
}
