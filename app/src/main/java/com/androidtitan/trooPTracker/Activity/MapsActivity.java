package com.androidtitan.trooptracker.Activity;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooptracker.Data.DatabaseHelper;
import com.androidtitan.trooptracker.Data.LocationBundle;
import com.androidtitan.trooptracker.Data.Soldier;
import com.androidtitan.trooptracker.Dialog.MapsAdderDialogFragment;
import com.androidtitan.trooptracker.Interface.MapsInterface;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;


//todo: search for location
// http://wptrafficanalyzer.in/blog/adding-google-places-autocomplete-api-as-custom-suggestions-in-android-search-dialog/
//http://www.androidhive.info/2012/08/android-working-with-google-places-and-maps-tutorial/

public class MapsActivity extends FragmentActivity implements MapsInterface, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    DatabaseHelper databaseHelper;

    private GoogleMap map; // Might be null if Google Play services APK is not available.
    private GoogleMapOptions options = new GoogleMapOptions();

    private GoogleApiClient googleAPIclient;

    private Handler handler;

    private ImageView editLoc;
    private ImageView addLoc;
    private ImageView locationGetter;

    private Animation slidein;

    private Location lastLocation;
    private LatLng lastLatLang;
    private double currentLatitude;
    private double currentLongitude;

    private int soldierIndex = -1;
    private boolean locationClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        databaseHelper = DatabaseHelper.getInstance(this);

        Intent intent = getIntent();
        soldierIndex = intent.getIntExtra("selectionToMap", -1);

        final Soldier tempSoldier = databaseHelper.getSoldier(soldierIndex);

        setUpMapIfNeeded();

        //our handle for our MapFragment
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buildGoogleApiClient();
        // Sets the map VIEW type to be "hybrid" and disables other UI features
        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false);

        //additional settings for map FUNCTIONALITY
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);


        //Focusing camera on Soldier's location or a random one
        //LatLng tempSoldierLatLng = databaseHelper.getAllLocationsBySoldier(tempSoldier).get(0).getLatlng();

        if (databaseHelper.getAllLocationsBySoldier(tempSoldier).size() > 0) { //if there is something present
            /*this currently gets the first location in their many saved locations...
            eventually we want to be able to 'page' through all of them
            */
            //todo set pin/marker on location
            CameraUpdate center = CameraUpdateFactory.newLatLng(databaseHelper.getAllLocationsBySoldier(tempSoldier).get(0).getLatlng());
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            map.moveCamera(center);
            map.animateCamera(zoom);

        } else {

            int rando = randInt(1, 5);
            LatLng starterLocation = databaseHelper.getLocationBundle(rando).getLatlng();

            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(starterLocation);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
            map.moveCamera(center);
            map.animateCamera(zoom);

            map.addMarker(new MarkerOptions().position(starterLocation)
                    .title(databaseHelper.getLocationBundle(rando).getLocalName()));

            Log.e("MAonCreate", String.valueOf(databaseHelper.getAllLocations()));
            //we are adding locations.  They are either not getting assigned to our Soldier
            // or we are not querying for them properly
        }

            //initializations
            handler = new Handler();

            editLoc = (ImageView) findViewById(R.id.editBtn);
            addLoc = (ImageView) findViewById(R.id.addBtn);
            locationGetter = (ImageView) findViewById(R.id.locBtn);
            locationGetter.setVisibility(View.GONE);


            map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    //placeholder
                }
            });

            locationGetter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final LocationManager manager = (LocationManager)
                            getSystemService(Context.LOCATION_SERVICE);

                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        buildAlertMessageNoGps();
                    } else {
                        lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                googleAPIclient);

                        if (lastLocation != null) {

                            currentLatitude = lastLocation.getLatitude();
                            currentLongitude = lastLocation.getLongitude();
                            lastLatLang = new LatLng(currentLatitude, currentLongitude);

                            Log.e("MAlocationGetter", "Location Found! " + currentLatitude + ", " + currentLongitude);

                            //camera
                            CameraUpdate center =
                                    CameraUpdateFactory.newLatLng(lastLatLang);
                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                            map.moveCamera(center);
                            map.animateCamera(zoom);

                            locationClick = true;

                        }
                    }
                }
            });

            addLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final LocationManager manager = (LocationManager)
                            getSystemService(Context.LOCATION_SERVICE);

                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        buildAlertMessageNoGps();
                    } else {
                        if (locationClick) {
                            LocationBundle tempBundle = new LocationBundle(new LatLng(currentLatitude, currentLongitude));
                            databaseHelper.createLocation(tempBundle);

                            databaseHelper.assignLocationToSolider(tempBundle, tempSoldier);

                            //Dialog fragment to add a Title to our
                            showAdderDialog(databaseHelper.getAllLocations().size());
                            //or size -1
                            //Log.e();
                            //todo:create a custom marker where the user can input name into edittext

                            map.addMarker(new MarkerOptions().position(tempBundle.getLatlng())
                                    .title(tempBundle.getLocalName()));

                            //this shit works!
                            Log.e("MAaddLoc", tempSoldier.getfName() + " location set! "
                                    + databaseHelper.getAllLocationsBySoldier(tempSoldier).get(0));

                            locationClick = false;
                        }
                    }


                }
            });

        editLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final LocationManager manager = (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else {

                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #map} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #map} is not null.
     */
    private void setUpMap() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //actions to take when the map is fully loaded...

    }

    //To connect to the API, you need to create an instance of the Google Play services API client
    protected synchronized void buildGoogleApiClient() {
        googleAPIclient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("APIclientConnected?", String.valueOf(googleAPIclient.isConnected()));

        googleAPIclient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e("APIclientConnected?", "Connection Suspended!");
        slidein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidein_right);

        handler.postDelayed(new Runnable() {
            public void run() {
                locationGetter.setVisibility(View.VISIBLE);
                locationGetter.startAnimation(slidein);
            }
        }, 500);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("APIclientConnected?", "Connection Suspended!");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("APIclientConnected?", "Connection Failed!!!");

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        buildAlertMessageNoGps();
    }

    //generates a random integer
    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void showAdderDialog(int inter) {
        //todo: title issue

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putInt("locationBundleIndex", inter);
        // Create and show the dialog.
        DialogFragment newFragment = new MapsAdderDialogFragment();
        newFragment.setArguments(bundle);

        newFragment.show(ft, "dialog");

    }

}

//if (!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
