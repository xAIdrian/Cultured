package com.androidtitan.trooPTracker.Data;

/**
 * Created by A. Mohnacs on 5/13/2015.
 */
public class Division {

    private long id;
    private String name;
    private Integer visits; // this could be GPS coords eventually

    public Division(){

    }

    public Division(String naname) {
        this.name = naname;
        this.visits = 0;
    }

    public Division(long iid, String naname) {
        this.id = iid;
        this.name = naname;
        this.visits = 0;
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

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visiter) {
        this.visits = visiter;
    }
}
