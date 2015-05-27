package com.androidtitan.alphaarmyapp.Data;

/**
 * Created by A. Mohnacs on 5/11/2015.
 */
public class Soldier {
    public static final String TAG = "Soldier";

    private long id;
    private String fName;
    private String lName;
    private String specialty;

    public Soldier(){
    }

    public Soldier(String first, String last, String spec) {
        this.fName = first;
        this.lName = last;
        this.specialty = spec;
    }

    public Soldier(int iid, String first, String last, String spec){
        this.id = iid;
        this.fName = first;
        this.lName = last;
        this.specialty = spec;
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

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

}
