package com.androidtitan.hotspots.Data;

/**
 * Created by A. Mohnacs on 5/11/2015.
 */
public class Soldier {
    public static final String TAG = "Soldier";

    private long id;
    private String fName;
    private String lName;
    //private int isLocationLocked; //we translate this into a boolean for easier programmatic use

    public Soldier() {
    }

    public Soldier(String first, String last) {
        this.fName = first;
        this.lName = last;
        //this.isLocationLocked = 0;
    }

    public Soldier(String first, String last, int isLocLocked) {

        this.fName = first;
        this.lName = last;
        //this.isLocationLocked = isLocLocked;
    }

    public Soldier(int iid, String first, String last, int isLocLocked) {
        this.id = iid;
        this.fName = first;
        this.lName = last;
        //this.isLocationLocked = isLocLocked;
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

    public String getFullName() {
        return fName + " " + lName;
    }

    /*public Boolean getIsLocationLocked() {

        if (isLocationLocked == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    public int getIsLocationLockedDatabase() {
        return isLocationLocked;
    }

    public void setIsLocationLocked(boolean isLocationLockeded) {
        if(isLocationLockeded) {
            setIsLocationLockedDatabase(1);
        }
        else {
            setIsLocationLockedDatabase(0);
        }
    }

    public void setIsLocationLockedDatabase(int isLocationLockedInt) {
        this.isLocationLocked = isLocationLockedInt;
    }*/

    //we are overriding toString() so it's default implementation is to return a string
    //not a memory address
    //We're going to roll with both names together
    @Override
    public String toString() {
        return fName + " " + lName;
    }

}
