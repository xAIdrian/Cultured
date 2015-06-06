package com.androidtitan.trooPTracker.Data;

/**
 * Created by A. Mohnacs on 5/13/2015.
 */
public class Division {

    private long id;
    private String name;
    private String location; // this could be GPS coords eventually

    public Division(){

    }

    public Division(String naname, String local) {
        this.name = naname;
        this.location = local;
    }

    public Division(long iid, String naname, String local) {
        this.id = iid;
        this.name = naname;
        this.location = local;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
