package com.androidtitan.trooptracker.Data;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by A. Mohnacs on 5/11/2015.
 */
public class Soldier {
    public static final String TAG = "Soldier";

    private long id;
    private String fName;
    private String lName;
    private double lat;
    private double longi;

    public Soldier(){
    }

    public Soldier(String first, String last) {
        this.fName = first;
        this.lName = last;
    }

    public Soldier(String first, String last, double latitude, double longitude) {
        this.fName = first;
        this.lName = last;
        this.lat = latitude;
        this.longi = longitude;
    }

    public Soldier(int iid, String first, String last){
        this.id = iid;
        this.fName = first;
        this.lName = last;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public LatLng getLatLang() {

        LatLng soldierLocation = new LatLng(lat, longi);
        return soldierLocation;
    }

    public void setLatLng(double latitude, double longitude) {
        this.lat = latitude;
        this.longi = longitude;
    }

    //we are overriding toString() so it's default implementation is to return a string
    //not a memory address
    //We're going to roll with both names together
    @Override
    public String toString() {
        return fName + " " + lName;
    }

}
