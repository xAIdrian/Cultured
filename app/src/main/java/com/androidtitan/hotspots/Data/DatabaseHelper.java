package com.androidtitan.hotspots.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A. Mohnacs on 5/13/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;

    private static final String DATABASE_NAME = "troopTrackerDatabase";
    private static final int DATABASE_VERSION = 1;

    //tables
    public static final String TABLE_SOLDIER = "soldiers";
    private static final String TABLE_COORDINATES = "coordinates";
    private static final String TABLE_STARTER_COORDS = "randocoordinates";
    private static final String TABLE_MAP = "map";

    //Common columns
    private static final String KEY_ID = "_id";

    //soldier table
    public static final String KEY_FIRSTNAME = "first";
    public static final String KEY_LASTNAME = "last";
    public static final String KEY_LOCKED = "locationlocked";

    //coordinates table
    private static final String KEY_LOCAL = "local";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";

    //random coordinates table
    private static final String KEY_STARTER_LOCAL = "randolocal";
    private static final String KEY_STARTER_LATITUDE = "randolatitude";
    private static final String KEY_STARTER_LONGITUDE = "randolongitude";

    //map table. soldier / coordinates table
    //private static final String KEY_SOLDIER_ID = "soldier_id"; //already included above
    private static final String KEY_COORD_ID = "coordinate_id";
    private static final String KEY_SOLDIER_ID = "soldier_id";


    // Table Creation Statements
    // Soldier Table
    private static final String CREATE_TABLE_SOLDIER = "CREATE TABLE " + TABLE_SOLDIER
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FIRSTNAME + " TEXT," + KEY_LASTNAME
            + " TEXT, " + KEY_LOCKED + " BIT" + ")";

    //Coordinates Table
    private static final String CREATE_TABLE_COORDINATES = "CREATE TABLE " + TABLE_COORDINATES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_LOCAL + " TEXT,"
            + KEY_LATITUDE + " DOUBLE PRECISION," + KEY_LONGITUDE + " DOUBLE PRECISION" + ")";

    //Random Coordinates Table
    private static final String CREATE_TABLE_STARTER_COORDS = "CREATE TABLE " + TABLE_STARTER_COORDS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_STARTER_LOCAL + " TEXT,"
            + KEY_STARTER_LATITUDE + " DOUBLE PRECISION," + KEY_STARTER_LONGITUDE + " DOUBLE PRECISION" + ")";

    //Map Table
    private static final String CREATE_TABLE_MAP = "CREATE TABLE " + TABLE_MAP
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_COORD_ID + " INTEGER, "
            + KEY_SOLDIER_ID + " INTEGER" + ")";

    public static synchronized DatabaseHelper getInstance(Context context) {

        //Singleton Pattern. Use the application context, which will ensure that you
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
        db.execSQL(CREATE_TABLE_COORDINATES);
        db.execSQL(CREATE_TABLE_STARTER_COORDS);

        db.execSQL(CREATE_TABLE_MAP);

        //maybe we can insert a sample user

        db.execSQL("insert into " + TABLE_STARTER_COORDS + " values (1, 'Seattle, Washington', 47.6062095, -122.3320708)");
        db.execSQL("insert into " + TABLE_STARTER_COORDS + " values (2, 'Miami, Florida', 25.761680, -80.191790)");
        db.execSQL("insert into " + TABLE_STARTER_COORDS + " values (3, 'Washington, DC', 38.8951, -77.0367)");
        db.execSQL("insert into " + TABLE_STARTER_COORDS + " values (4, 'Tokyo, Japan', 35.6894875, 139.69170639999993)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOLDIER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COORDINATES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAP);

        // create new tables
        onCreate(db);
    }

    //todo: SOLDIER TABLE

    public void printSoldierTable() {
        String selectQuery = "SELECT * FROM " + TABLE_SOLDIER;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        //looping through all the rows to create objects to add to our list
        if (cursor.moveToFirst()) {
            do {
                Soldier soldier = new Soldier();
                soldier.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                soldier.setfName(cursor.getString(cursor.getColumnIndex(KEY_FIRSTNAME)));
                soldier.setlName(cursor.getString(cursor.getColumnIndex(KEY_LASTNAME)));
                soldier.setIsLocationLockedDatabase(cursor.getInt(cursor.getColumnIndex(KEY_LOCKED)));
                //logging
                Log.e("DBHprintAllSoldiers", cursor.getLong(cursor.getColumnIndex(KEY_ID)) + " "
                        + cursor.getString(cursor.getColumnIndex(KEY_FIRSTNAME)) + " "
                        + cursor.getString(cursor.getColumnIndex(KEY_LASTNAME)) + " "
                        + cursor.getInt(cursor.getColumnIndex(KEY_LOCKED)));

            } while (cursor.moveToNext());
        }
    }


    /**
     * The function will create a soldier item in soldiers table. In this same
     * function we are assigning the soldier to a division name which inserts a row in
     * command table.
     */
    public long createSoldier(Soldier soldier) { // , long[] division_ids <-- this is a parameter
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, soldier.getfName());
        values.put(KEY_LASTNAME, soldier.getlName());
        values.put(KEY_LOCKED, soldier.getIsLocationLockedDatabase());
        //insert row. if there is a conflict the last parameter springs into action. Replacing entry.

        //this is a possible solution for the DUPLICATES PROBLEM
        long soldier_id = database.insertWithOnConflict(TABLE_SOLDIER, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        soldier.setId(soldier_id);

        //Log.i("DBHcreateSoldier", "Created: " + getSoldier(soldier_id).getId() + " " +
        //getSoldier(soldier_id).getfName() + " " + getSoldier(soldier_id).getlName());

        return soldier_id;
    }


    /**
     * We will fetch a soldier from the soldiers table
     */
    public Soldier getSoldier(long soldier_id) {
        soldier_id = soldier_id + 1;
        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_SOLDIER
                + " WHERE " + KEY_ID + " = " + String.valueOf(soldier_id);
        Log.i("DBHgetSoldier", selectQuery);

        Cursor cursor = database.query(TABLE_SOLDIER, new String[]{KEY_ID, KEY_FIRSTNAME, KEY_LASTNAME, KEY_LOCKED},
                KEY_ID + "=?", new String[]{String.valueOf(soldier_id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Soldier soldier = new Soldier();
        soldier.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
        soldier.setfName(cursor.getString(cursor.getColumnIndex(KEY_FIRSTNAME)));
        soldier.setlName(cursor.getString(cursor.getColumnIndex(KEY_LASTNAME)));
        soldier.setIsLocationLockedDatabase(cursor.getInt(cursor.getColumnIndex(KEY_LOCKED)));

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

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        //looping through all the rows to create objects to add to our list
        if (cursor.moveToFirst()) {
            do {
                Soldier soldier = new Soldier();
                soldier.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                soldier.setfName(cursor.getString(cursor.getColumnIndex(KEY_FIRSTNAME)));
                soldier.setlName(cursor.getString(cursor.getColumnIndex(KEY_LASTNAME)));
                soldier.setIsLocationLockedDatabase(cursor.getInt(cursor.getColumnIndex(KEY_LOCKED)));
                //adding
                troops.add(soldier);

            } while (cursor.moveToNext());
        }
        return troops;
    }

    /**
     * Filtered getAllSoldiers.  We only want Soldiers in a specified Division
     * SELECT * FROM soldiers s, division d, command c WHERE d.tag_name = Watchlist AND d.id = c.tag_id AND s.id = c.todo_id;
     **//*
    public List<Soldier> getAllSoldiersByDivision(Division division) {
        String division_name = division.getName();
        Log.i("soldiersByDivision", division_name);
        List<Soldier> troops = new ArrayList<Soldier>();

        String selectQuery = "SELECT * FROM "
                + TABLE_SOLDIER + " ts, "
                + TABLE_DIVISION + " td, "
                + TABLE_COMMAND + " tc WHERE td."
                + KEY_NAME + " = '" + division_name + "'" + " AND td." + KEY_ID
                + " = " + "tc." + KEY_DIVISION_ID + " AND ts." + KEY_ID + " = "
                + "tc." + KEY_SOLDIER_ID;

        //This is a TABLE JOIN^

        Log.i("DBHsoldiersByDivision", selectQuery);

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        //loop through and add to list
        if (cursor.moveToFirst()) {
            do {
                Soldier soldier = new Soldier();
                soldier.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                soldier.setfName(cursor.getString(cursor.getColumnIndex(KEY_FIRSTNAME)));
                soldier.setlName(cursor.getString(cursor.getColumnIndex(KEY_LASTNAME)));
                //add
                troops.add(soldier);
            } while (cursor.moveToNext());
        }

        return troops;
    }
*/
    //Following function will update Soldier values only, not Division
    public int updateSoldier(Soldier soldier) {
        SQLiteDatabase database = this.getWritableDatabase();
        //prepping/formatting data for update(replace) row
        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, soldier.getfName());
        values.put(KEY_LASTNAME, soldier.getlName());
        values.put(KEY_LOCKED, soldier.getIsLocationLockedDatabase());
        //updating
        Log.i("DBHupdateSoldier", "Updated!" + TABLE_SOLDIER + " " + KEY_ID + " = " + String.valueOf(soldier.getId()));

        return database.update(TABLE_SOLDIER, values,
                KEY_ID + " = ?", new String[]{String.valueOf(soldier.getId())});

    }

    /**
     * Pass Soldier_Id to delete Soldier
     **/
    public void deleteSoldier(Soldier soldier) {

        SQLiteDatabase database = this.getWritableDatabase();

        Log.i("DBHdeleteSoldier", "Deleted! " + soldier.getfName() + " " + soldier.getId());

        List<LocationBundle> bundlesToDelete = getAllLocationsBySoldier(soldier);
        for(LocationBundle bundle2delete : bundlesToDelete) {
            deleteLocation(bundle2delete);
        }

        database.delete(TABLE_SOLDIER,
                KEY_ID + " = ?", new String[]{String.valueOf(soldier.getId())});

        /*
         if(should_delete_all_soldiers) {
            List<Soldier> troops = getAllSoldiersByDivision(division);
            //delete all soldiers
            for(Soldier soldier : troops) {
                deleteSoldier(soldier);
            }
        }
        Log.e("insideDeleteDivision", TABLE_DIVISION + ": " + KEY_ID + " = ? " + division.getId());
        database.delete(TABLE_DIVISION, KEY_ID + " = ?",
                new String[]{String.valueOf(division.getId())});
         */

    }

    //Update all of our Soldiers within the Soldiers Table
    //we are going to iterate through the table and update each item
    public void updateSoldierTable() {
        SQLiteDatabase database = this.getWritableDatabase();
        List<Soldier> troops = getAllSoldiers();

        for (Soldier soldier : troops) {
            ContentValues values = new ContentValues();
            values.put(KEY_FIRSTNAME, soldier.getfName());
            values.put(KEY_LASTNAME, soldier.getlName());
            values.put(KEY_LOCKED, soldier.getIsLocationLockedDatabase());
            //updating
            database.update(TABLE_SOLDIER, values,
                    KEY_ID + " = ?", new String[]{String.valueOf(soldier.getId())});
        }
    }


    //todo: COORDINATES TABLE
    //I did NOT include update or delete...


    public long createLocation(LocationBundle locBun) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOCAL, locBun.getLocalName());
        values.put(KEY_LATITUDE, locBun.getLatlng().latitude);
        values.put(KEY_LONGITUDE, locBun.getLatlng().longitude);

        long coordinate_id = database.insert(TABLE_COORDINATES, null, values);
        locBun.setId(coordinate_id);

        return coordinate_id;
    }

    public List<LocationBundle> getAllLocations() {
        List<LocationBundle> geoPoints = new ArrayList<LocationBundle>();
        String selectQuery = "SELECT * FROM " + TABLE_COORDINATES;

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LocationBundle locBun = new LocationBundle();
                locBun.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                locBun.setLocalName(cursor.getString(cursor.getColumnIndex(KEY_LOCAL)));
                locBun.setLatlng(new LatLng(cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))));

                geoPoints.add(locBun);
            } while (cursor.moveToNext());
        }
        return geoPoints;
    }

    public LocationBundle getLocationBundle(long coord_id) {
        //coord_id = coord_id + 1;  IDK if we will actually need this as this method might not even be used

        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_COORDINATES
                + " WHERE " + KEY_ID + " = " + coord_id;
        Log.i("DBHgetCoordinate", selectQuery);

        Cursor cursor = database.query(TABLE_COORDINATES, new String[]{KEY_ID, KEY_LOCAL, KEY_LATITUDE, KEY_LONGITUDE},
                KEY_ID + " =?", new String[]{String.valueOf(coord_id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        LocationBundle locBundle = new LocationBundle();
        locBundle.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        locBundle.setLocalName(cursor.getString(cursor.getColumnIndex(KEY_LOCAL)));
        locBundle.setLatlng(new LatLng(cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))));

        return locBundle;
    }

    // SELECT * FROM soldiers s, division d, command c WHERE d.tag_name = Watchlist AND d.id = c.tag_id AND s.id = c.todo_id;
    public List<LocationBundle> getAllLocationsBySoldier(Soldier soldier) {
        long soldierId = soldier.getId();
        SQLiteDatabase database = this.getReadableDatabase();

        ArrayList<LocationBundle> packs = new ArrayList<LocationBundle>();

        String selectQuery = "SELECT * FROM "
                + TABLE_COORDINATES + " ts, "
                + TABLE_SOLDIER + " td, "
                + TABLE_MAP + " tc WHERE td."
                + KEY_ID + " = " + soldierId + " AND td." + KEY_ID
                + " = " + "tc." + KEY_SOLDIER_ID + " AND ts." + KEY_ID + " = "
                + "tc." + KEY_COORD_ID;

        Log.i("DBHlocationBySoldier", selectQuery);

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LocationBundle locBundle = new LocationBundle();
                locBundle.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                locBundle.setLocalName(cursor.getString(cursor.getColumnIndex(KEY_LOCAL)));
                locBundle.setLatlng(new LatLng(cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))));

                packs.add(locBundle);

            } while (cursor.moveToNext());
        }
        return packs;
    }

    //this will query the MAP table and find a soldier that is in the same row as the locBun id
    public Soldier getLocationsSoldier(LocationBundle locationBundle) {
        String locId = locationBundle.getLocalName();
        SQLiteDatabase database = this.getReadableDatabase();

        ArrayList<LocationBundle> packs = new ArrayList<LocationBundle>();

        String selectQuery = "SELECT * FROM "
                + TABLE_COORDINATES + " ts, "
                + TABLE_SOLDIER + " td, "
                + TABLE_MAP + " tc WHERE ts."
                + KEY_LOCAL + " = '" + locId + "' AND td." + KEY_ID
                + " = " + "tc." + KEY_SOLDIER_ID + " AND ts." + KEY_ID + " = "
                + "tc." + KEY_COORD_ID;

        Log.i("DBHsoldierByLocation", selectQuery);

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToNext();

        Soldier soldier = new Soldier();
        soldier.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        soldier.setfName(cursor.getString(cursor.getColumnIndex(KEY_FIRSTNAME)));
        soldier.setlName(cursor.getString(cursor.getColumnIndex(KEY_LASTNAME)));

        return soldier;

    }


    public void deleteLocation(LocationBundle locBun) {
        SQLiteDatabase database = this.getWritableDatabase();

        Log.i("DBHdeleteLocation", "Deleted! " + TABLE_COORDINATES + ": " + KEY_ID + " = " + locBun.getId());

        database.delete(TABLE_COORDINATES,
                KEY_ID + " =?", new String[]{String.valueOf(locBun.getId())});

        /*
        Log.i("insideDeleteDivision", TABLE_DIVISION + ": " + KEY_ID + " = ? " + division.getId());
        database.delete(TABLE_DIVISION, KEY_ID + " = ?",
                new String[]{String.valueOf(division.getId())});
         */

    }

    public void printCoordinatesTable() {
        String selectQuery = "SELECT * FROM " + TABLE_COORDINATES;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        //looping through all the rows to create objects to add to our list
        if (cursor.moveToFirst()) {
            do {
                LocationBundle locker = new LocationBundle();
                locker.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                locker.setLocalName(cursor.getString(cursor.getColumnIndex(KEY_LOCAL)));
                locker.setLatlng(new LatLng(cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))));
                //logging
                Log.i("DBHprintAllCoordinates", cursor.getLong(cursor.getColumnIndex(KEY_ID)) + " "
                        + cursor.getString(cursor.getColumnIndex(KEY_LOCAL)) + ": "
                        + cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)) + ", "
                        + cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE)));

            } while (cursor.moveToNext());
        }
    }

    //todo: STARTER COORDS TABLE

    public LocationBundle getStarterLocationBundle(long coord_id) {
        //coord_id = coord_id + 1;  IDK if we will actually need this as this method might not even be used

        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_STARTER_COORDS
                + " WHERE " + KEY_ID + " = " + coord_id;
        Log.i("DBHgetCoordinate", selectQuery);

        Cursor cursor = database.query(TABLE_STARTER_COORDS, new String[]{KEY_ID, KEY_STARTER_LOCAL,
                        KEY_STARTER_LATITUDE, KEY_STARTER_LONGITUDE},
                KEY_ID + " =?", new String[]{String.valueOf(coord_id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        LocationBundle locBundle = new LocationBundle();
        locBundle.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        locBundle.setLocalName(cursor.getString(cursor.getColumnIndex(KEY_STARTER_LOCAL)));
        locBundle.setLatlng(new LatLng(cursor.getDouble(cursor.getColumnIndex(KEY_STARTER_LATITUDE)),
                cursor.getDouble(cursor.getColumnIndex(KEY_STARTER_LONGITUDE))));

        return locBundle;
    }


    //todo: MAP TABLE


    public long assignLocationToSolider(LocationBundle locBun, Soldier soldier) {
        SQLiteDatabase databasae = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COORD_ID, locBun.getId());
        values.put(KEY_SOLDIER_ID, soldier.getId());

        long id = databasae.insert(TABLE_MAP, null, values);
        return id;
    }

    public int updateLocationSoldier(LocationBundle locBun, Soldier soldier) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COORD_ID, locBun.getId());
        values.put(KEY_SOLDIER_ID, soldier.getId());

        return database.update(TABLE_MAP, values,
                KEY_ID + " = ?", new String[]{String.valueOf(locBun.getId())});
    }


    //Importantly dont forget to close the database connection once you done using it.
    //Call following method when you dont need access to db anymore.
    public void closeDatabase() {
        SQLiteDatabase database = this.getReadableDatabase();
        if (database != null && database.isOpen())
            database.close();

        Log.i("closeDatabase()", "3 Star DATABASE CLOSED SIR!");
    }
}
