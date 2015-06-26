package com.androidtitan.trooptracker.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A. Mohnacs on 5/13/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;

    private static final String String_LOG = "Database Helper";

    private static final String DATABASE_NAME = "alphaArmyDatabase";
    private static final int DATABASE_VERSION = 1;

    //tables
    private static final String TABLE_SOLDIER = "soldiers";
    private static final String TABLE_DIVISION = "divisions";
    private static final String TABLE_COMMAND = "command";

    //Common columns
    private static final String KEY_ID = "_id";

    //soldier table
    private static final String KEY_FIRSTNAME = "first";
    private static final String KEY_LASTNAME = "last";

    //division table
    private static final String KEY_NAME = "name";
    private static final String KEY_VISITS = "visits";

    //command table. Stores the id's for soldier and division
    private static final String KEY_SOLDIER_ID = "soldier_id";
    private static final String KEY_DIVISION_ID = "division_id";

    // Table Creation Statements
    // Soldier Table
    private static final String CREATE_TABLE_SOLDIER = "CREATE TABLE " + TABLE_SOLDIER
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FIRSTNAME + " TEXT," + KEY_LASTNAME + " TEXT" + ")";
    //Division Table
    private static final String CREATE_TABLE_DIVISION = "CREATE TABLE " + TABLE_DIVISION
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_VISITS + " INTEGER,"
            + "UNIQUE (" + KEY_NAME + "))";
    //Command Table
    private static final String CREATE_TABLE_COMMAND = "CREATE TABLE " + TABLE_COMMAND
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SOLDIER_ID + " INTEGER," + KEY_DIVISION_ID + " INTEGER" + ")";

    public static synchronized DatabaseHelper getInstance(Context context) {

        //Singleton Pattern
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating the required tables
        db.execSQL(CREATE_TABLE_SOLDIER);
        db.execSQL(CREATE_TABLE_DIVISION);
        db.execSQL(CREATE_TABLE_COMMAND);


        //insert into TABLE_DIVISION (_id, name, local) values(1, 'Hinogi', 'Japan')
        db.execSQL("insert into " + TABLE_DIVISION + " values (1, 'Seattle, Washington', 0)");
        db.execSQL("insert into " + TABLE_DIVISION + " values (2, 'San Diego, California', 0)");
        db.execSQL("insert into " + TABLE_DIVISION + " values (3, 'London, England', 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOLDIER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIVISION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMAND);

        // create new tables
        onCreate(db);
    }

    //SOLDIER TABLE

    /**
     * The function will create a soldier item in soldiers table. In this same
     * function we are assigning the soldier to a division name which inserts a row in
     * command table.
    **/
    public long createSoldier(Soldier soldier) { // , long[] division_ids <-- this is a parameter
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, soldier.getfName());
        values.put(KEY_LASTNAME, soldier.getlName());
        //insert row
        long soldier_id = database.insert(TABLE_SOLDIER, null, values);
        soldier.setId(soldier_id);

        Log.e("DBHcreateSoldier", getSoldier(soldier_id).getId() + " " +
                getSoldier(soldier_id).getfName() + " " + getSoldier(soldier_id).getlName());

        return soldier_id;
    }


    /**
     * We will fetch a soldier from the soldiers table
     * SELECT * FROM soldiers WHERE id = 1
    **/
    public Soldier getSoldier(long soldier_id) {
        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_SOLDIER
                + " WHERE " + KEY_ID + " = " + soldier_id;

        Log.e("DBHgetSoldier", selectQuery);

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }
        Soldier soldier = new Soldier();
        soldier.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
        soldier.setfName(cursor.getString(cursor.getColumnIndex(KEY_FIRSTNAME)));
        soldier.setlName(cursor.getString(cursor.getColumnIndex(KEY_LASTNAME)));

        return soldier;
    }


    /**
     * Fetching all soldiers involves reading all soldier rows and adding them to
     * a List Array (not arraylist)     *
     * SELECT * FROM soldiers
    **/

    public List<Soldier> getAllSoldiers() {
        List<Soldier> troops = new ArrayList<Soldier>();
        String selectQuery = "SELECT * FROM " + TABLE_SOLDIER;

        Log.e("DBHgetAllSoldiers", selectQuery);

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        //looping through all the rows to create objects to add to our list
        if(cursor.moveToFirst()){
            do{
                Soldier soldier = new Soldier();
                soldier.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                soldier.setfName(cursor.getString(cursor.getColumnIndex(KEY_FIRSTNAME)));
                soldier.setlName(cursor.getString(cursor.getColumnIndex(KEY_LASTNAME)));
                //adding
                troops.add(soldier);

            } while(cursor.moveToNext());
        }
        return troops;
    }

    /**
     * Filtered getAllSoldiers.  We only want Soldiers in a specified Division
     * SELECT * FROM soldiers s, division d, command c WHERE d.tag_name = Watchlist AND d.id = c.tag_id AND s.id = c.todo_id;
    **/
    public List<Soldier> getAllSoldiersByDivision(Division division) {
        String division_name = division.getName();
        Log.e("soldiersByDivision", division_name);
        List<Soldier> troops = new ArrayList<Soldier>();

        String selectQuery = "SELECT * FROM " + TABLE_SOLDIER + " ts, "
                + TABLE_DIVISION + " td, " + TABLE_COMMAND + " tc WHERE td."
                + KEY_NAME + " = '" + division_name + "'" + " AND td." + KEY_ID
                + " = " + "tc." + KEY_DIVISION_ID + " AND ts." + KEY_ID + " = "
                + "tc." + KEY_SOLDIER_ID;

        Log.e("DBHsoldiersByDivision", selectQuery);

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        //loop through and add to list
        if(cursor.moveToFirst()) {
            do {
                Soldier soldier = new Soldier();
                soldier.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                soldier.setfName(cursor.getString(cursor.getColumnIndex(KEY_FIRSTNAME)));
                soldier.setlName(cursor.getString(cursor.getColumnIndex(KEY_LASTNAME)));
                //add
                troops.add(soldier);
            } while(cursor.moveToNext());
        }

        return troops;
    }

    //Following function will update Soldier values only, not Division
    public int updateSoldier(Soldier soldier){
        SQLiteDatabase database = this.getWritableDatabase();
        //prepping/formatting data for update(replace) row
        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, soldier.getfName());
        values.put(KEY_LASTNAME, soldier.getlName());
        //updating
        Log.e("DBHupdateSoldier","Updated!" + TABLE_SOLDIER + " " + KEY_ID + " = " + String.valueOf(soldier.getId()));

        return database.update(TABLE_SOLDIER, values,
                KEY_ID + " = ?", new String[] { String.valueOf(soldier.getId()) });

    }

    /**
     * Pass Soldier_Id to delete Soldier
    **/
    public void deleteSoldier(Soldier soldier) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_SOLDIER,
                KEY_ID + " = ?", new String[]{String.valueOf(soldier.getId())});

    }

    //Update all of our Soldiers within the Soldiers Table
    //we are going to iterate through the table and update each item
    //todo: look up "how to recreate database on action"
    public void updateSoldierTable() {
        SQLiteDatabase database = this.getWritableDatabase();
        List<Soldier> troops = getAllSoldiers();

        for(Soldier soldier : troops) {
            ContentValues values = new ContentValues();
            values.put(KEY_FIRSTNAME, soldier.getfName());
            values.put(KEY_LASTNAME, soldier.getlName());
            //updating
            database.update(TABLE_SOLDIER, values,
                    KEY_ID + " = ?", new String[]{String.valueOf(soldier.getId())});
        }
    }

    //DIVISION TABLE
    //The tables are seperate they are "joined"/linked

    //Insert a row into tags table
    public long createDivision(Division division) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, division.getName());
        values.put(KEY_VISITS, division.getVisits());
        //insert
        long division_id = database.insert(TABLE_DIVISION, null, values);
        division.setId(division_id);
        Log.e("DBHcreateDivision", String.valueOf(values));
        return division_id;
    }

    //Fetching all of our DIVISION names
    //SELECT * FROM divisions
    public List<Division> getAllDivisions() {
        List<Division> battalion = new ArrayList<Division>();
        String selectQuery = "SELECT * FROM " + TABLE_DIVISION;

        Log.e("DBHgetAllDivisions", selectQuery);

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Division div = new Division();
                div.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                div.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                div.setVisits(cursor.getInt(cursor.getColumnIndex(KEY_VISITS)));
                //add
                battalion.add(div);
            } while (cursor.moveToNext());
        }
        return battalion;
    }

    //todo: STAY AWAY FROM THIS IT DOES NOT WORK
    /*public Division getDivision(long div_id) {
        SQLiteDatabase database = this.getReadableDatabase();

        div_id = div_id + 1; //incrementation to align divIndex with dbIndex
        String selectQuery = "SELECT * FROM " + TABLE_DIVISION
                + " WHERE " + KEY_ID + " = " + div_id;

        Log.e("DBHgetDivision", selectQuery);

        Cursor cursor = database.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        Division division = new Division();

        if(cursor.getCount() > 0 && cursor.moveToFirst()) {

            division.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            division.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            division.setVisits(cursor.getInt(cursor.getColumnIndex(KEY_VISITS)));
        }
        return division;

    }*/

    //Updating Division
    public int updateDivision(Division division) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, division.getName());
        values.put(KEY_VISITS, division.getVisits());
        //updating row
        return database.update(TABLE_DIVISION, values,
                KEY_ID + " = ?", new String[] { String.valueOf(division.getId()) });
    }

    /**
     * Delete Division. It will also delete associated Soldiers.
     *
     * Must be passed an INDEX.  Divisions are not given ID's until they
     **/
    public void deleteDivision(Division division, boolean should_delete_all_soldiers) {
        SQLiteDatabase database = this.getWritableDatabase();
        //If we are told to Delete all associated soldiers and they have soldiers

        if(should_delete_all_soldiers) {
            List<Soldier> troops = getAllSoldiersByDivision(division);
            //delete all soldiers
            for(Soldier soldier : troops) {
                deleteSoldier(soldier);
            }
        }
        Log.e("insideDeleteDivision", TABLE_DIVISION + ": " + KEY_ID + " = ? " + division.getId());
        database.delete(TABLE_DIVISION, KEY_ID + " = ?",
                new String[]{String.valueOf(division.getId()) });

    }

    //This will assign a soldier under a division.  Multiple calls for multiple adds
    //We will be creating an entry in the COMMAND TABLE
    public long assignSoldierToDivision(Soldier soldier, Division division) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SOLDIER_ID, soldier.getId());
        values.put(KEY_DIVISION_ID, division.getId());

        long id = database.insert(TABLE_COMMAND, null, values);
        return id;
    }

    //Removing Division ID from Soldier
    //The DBitem will still exist but they will no longer be associated
    public int updateSoldierDivision(Soldier soldier, Division division) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SOLDIER_ID, soldier.getId());
        values.put(KEY_DIVISION_ID, division.getId());
        //update
        Log.e("COMMAND_TABLE", TABLE_COMMAND + ": " + values + KEY_ID + " = ?" + new String[]{ String.valueOf(soldier.getId()) });

        return database.update(TABLE_COMMAND, values,
                KEY_ID + " = ?", new String[]{ String.valueOf(soldier.getId()) });
    }

    /*
   return database.update(TABLE_SOLDIER, values,
                KEY_ID + " = ?", new String[] { String.valueOf(soldier.getId()) });
     */

    //Importantly dont forget to close the database connection once you done using it.
    //Call following method when you dont need access to db anymore.
    public void closeDatabase() {
        SQLiteDatabase database = this.getReadableDatabase();
        if(database != null && database.isOpen())
            database.close();

        Log.e("closeDatabase()", "3 Star DATABASE CLOSED SIR!");
    }

}
