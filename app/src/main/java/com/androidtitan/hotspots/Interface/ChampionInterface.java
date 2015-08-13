package com.androidtitan.hotspots.Interface;

/**
 * Created by A. Mohnacs on 5/15/2015.
 */

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * <p>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 */
public interface ChampionInterface {
    public void tabInteraction(int id);
    public void soldierPasser(int soldierInt, String first, String last);
    public void drawerListViewSelection(int selection);
    public void setListViewSelection(int selection);

    public void selectionToMap(int selection);
}