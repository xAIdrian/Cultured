package com.androidtitan.hotspots.Activity;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtitan.hotspots.Data.DatabaseHelper;
import com.androidtitan.hotspots.Data.LocationBundle;
import com.androidtitan.hotspots.Dialog.MapsAdderDialogFragment;
import com.androidtitan.hotspots.Interface.MapsPullInterface;
import com.androidtitan.hotspots.R;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;


public class MapsActivity extends FragmentActivity implements MapsPullInterface, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "MapActivity";

    private static final String SAVED_INITIAL_BOOL = "isInitialOpening";
    private static final String SAVED_DIALOG_BOOL = "savedDialogBool";

    DatabaseHelper databaseHelper;

    private GoogleMap map; // Might be null if Google Play services APK is not available.
    private GoogleMapOptions options = new GoogleMapOptions();
    private GoogleApiClient googleAPIclient;

    private LocationBundle focusLocation;
    private Location tempLocation;
//    private List<LocationBundle> soldierLocations;
//    private int soldierLocationsSize;

    private Handler handler;

    private ImageButton actionButton;
    private ImageView backer;
    private ImageView locker;

    private Animation slidein;
    private Animation slideOut;
    private Animation leftSlideIn;
    private Animation leftSlideOut;

    private Location lastLocation;
    private LatLng lastLatLang;
    private double currentLatitude;
    private double currentLongitude;

    private int locationIndex = -1;

    private int FABstatus = -1; //0=location 1=add   2=submit

    private boolean isInitialOpening = true;
    private boolean isLocationAdded = false; //use this to control yo dialogs
    private boolean isLocked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (savedInstanceState != null) {

            isInitialOpening = savedInstanceState.getBoolean(SAVED_INITIAL_BOOL);
            isLocationAdded = savedInstanceState.getBoolean(SAVED_DIALOG_BOOL);
        }

        databaseHelper = DatabaseHelper.getInstance(this);

        Intent intent = getIntent();
        locationIndex = intent.getIntExtra("selectionToMap", -1);

        //TODO

        focusLocation = databaseHelper.getAllLocations().get(locationIndex);

        tempLocation = new Location("tempLocation");
        try {
            tempLocation.setLatitude(focusLocation.getLatlng().latitude);
            tempLocation.setLongitude(focusLocation.getLatlng().longitude);
        } catch (NullPointerException e) {
            Log.e(TAG, String.valueOf(e));
        }

        isLocked = focusLocation.getIsLocationLocked();

        setUpMapIfNeeded();

        //our handle for our MapFragment
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buildGoogleApiClient();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false);

        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        //place markers for every saved location
        for (LocationBundle bund : databaseHelper.getAllLocations()) {
            try {
//TODO
                map.addMarker(new MarkerOptions()
                        .position(bund.getLatlng())
                        .title(bund.getLocalName())
                        .snippet(bund.getLocalName()));
            } catch (CursorIndexOutOfBoundsException e) {

                map.addMarker(new MarkerOptions()
                        .position(bund.getLatlng())
                        .title(bund.getLocalName()));
            }
        }

        //Location.distanceBetween(focusLocation.getLatlng().latitude, focusLocation.getLatlng().longitude,
        //tempLocation.getLatitude(), tempLocation.getLongitude(), );
        if (!focusLocation.getLatlng().equals(new LatLng(0.0, 0.0))) { //if there is something present
            /*this currently gets the first location in their many saved locations...
            eventually we want to be able to 'page' through all of them
            */
            cameraLocation(false, -1, null);
        } else {
            cameraLocation(true, -1, null);
        }


        databaseHelper.printCoordinatesTable();

        //initializations

        backer = (ImageView) findViewById(R.id.back_action);
        backer.setVisibility(View.GONE);
        locker = (ImageView) findViewById(R.id.locker);
        actionButton = (ImageButton) findViewById(R.id.floatingActionImageButton);
        actionButton.setVisibility(View.GONE);

        //if we've used all of our locations then we lock-it up
        //this is our Critical Logic
        if (isLocked) {
            isLocationAdded = true;
            //lockingAction();
            locker.setImageResource(R.drawable.lock_closed);
            actionButton.setImageResource(R.drawable.icon_submit);

            FABstatus = 2;
            //todo: LATER we need to include for if we are Locked AND Scored
        } else {
            //todo
            //they've already been here
            if (focusLocation.getLatlng() != null) {

            } else {
                final AlertDialog.Builder aDawg = new AlertDialog.Builder(MapsActivity.this);

                aDawg.setTitle(R.string.careful)
                        .setMessage(R.string.initial_instruction)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                aDawg.show();

                FABstatus++;
            }
        }


        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                //placeholder
            }
        });


        //using existing variables we are going to check which state it is already in
        //on changing state 'actionButton' is going to slide out and then slide back in
        //new sourceImage and newColor (newColor will require 2 new circle backgrounds)

        //maybe we can use a SWITCH statement
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final LocationManager manager = (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);
                lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        googleAPIclient);

                final AlertDialog.Builder aDawg = new AlertDialog.Builder(MapsActivity.this);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else {

                    switch (FABstatus) {

                        case -1:

                            aDawg.setTitle(R.string.careful)
                                    .setMessage(R.string.initial_instruction)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            aDawg.show();

                            FABstatus++;

                            break;

                        case 0: //LOCATION fab

                        if (lastLocation != null) {

                            currentLatitude = lastLocation.getLatitude();
                            currentLongitude = lastLocation.getLongitude();
                            lastLatLang = new LatLng(currentLatitude, currentLongitude);

                            Log.e("MAlocationGetter", "Location Found! " + currentLatitude + ", " + currentLongitude);

                            //camera
                            cameraLocation(false, -1, lastLatLang);

                            FABstatus ++;

                            actionButton.startAnimation(slideOut);
                            actionButton.setVisibility(View.GONE);

                            //handler is being used for the return action
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    actionButton.setImageResource(R.drawable.icon_add);
                                    actionButton.setVisibility(View.VISIBLE);
                                    actionButton.startAnimation(slidein);

                                    backer.startAnimation(leftSlideIn);
                                    backer.setVisibility(View.VISIBLE);

                                //listView.setEnabled(true);
                                }
                            }, slideOut.getDuration());

                        }
                            break;

                        case 1: //ADD fab
                            showAdderDialog(locationIndex, currentLatitude, currentLongitude);

                            break;

                        case 2:

                            break;

                        default:
                            Log.e("hotspots", "SOMETHING WENT WRONG IN OUR SWITCH");
                            break;
                    }
                }
            }
        });


        locker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lockingAction();

            }
        });

        backer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FABstatus == 1) {
                    actionButton.startAnimation(slideOut);
                    actionButton.setVisibility(View.GONE);

                    //handler is being used for the return action
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            actionButton.setImageResource(R.drawable.icon_location);
                            actionButton.setVisibility(View.VISIBLE);
                            actionButton.startAnimation(slidein);

                            backer.startAnimation(leftSlideOut);
                            backer.setVisibility(View.GONE);

                            FABstatus--;
                        }
                    }, slideOut.getDuration());
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

        map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #map} is not null.
     */
    private void setUpMap() {
        map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
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

    //Saves data that is lost on rotation
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putBoolean(SAVED_INITIAL_BOOL, isInitialOpening);
        savedInstanceState.putBoolean(SAVED_DIALOG_BOOL, isLocationAdded);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("APIclientConnected?", String.valueOf(googleAPIclient.isConnected()));

        googleAPIclient.connect();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ChampionActivity.class);
        startActivity(intent);
        this.finish();
    }



    @Override
    public void onConnected(Bundle bundle) {
        Log.e("APIclientConnected?", "Connection Suspended!");
        slidein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_slidin_bottom);
        slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_slideout_bottom);
        leftSlideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_slidein_left);
        leftSlideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.icon_slideout_left);

        handler = new Handler();

        if (!focusLocation.getLatlng().equals(new LatLng(0.0, 0.0)) && !isLocked) {
            actionButton.setVisibility(View.GONE);
            FABstatus = 2;
        } else {

            handler.postDelayed(new Runnable() {
                public void run() {

                    actionButton.setVisibility(View.VISIBLE);
                    actionButton.startAnimation(slidein);

                }
            }, 500);
        }
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




    //todo: custom methods
/////////////////////////////////////////////////////////////////////////////////////////////////////

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View v;
        private TextView infoHeader;
        private TextView infoSecondary;

        private String title;
        private String secondary;

        public CustomInfoWindowAdapter() {
            v = getLayoutInflater().inflate(R.layout.info_window_custom, null);
        }

        //onCreate()
        @Override
        public View getInfoContents(Marker marker) {

            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
            return null;
        }

        //onCreateView()
        @Override
        public View getInfoWindow(final Marker marker) {

            infoHeader = (TextView) v.findViewById(R.id.info_header);
            infoSecondary = (TextView) v.findViewById(R.id.info_second);

            title = marker.getTitle();
            infoHeader.setText(title);

            try {
                secondary = marker.getSnippet();
                infoSecondary.setText(secondary);
            } catch (NullPointerException e) {
                infoSecondary.setText("");
            }

            return v;
        }
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

    //directs the user to a location on the map
    //eventually we will use tha parameter to navigate to SQL row item
    //used for all of the Map Navigations

    //the int will be used in the future if we need to view a number of locations
    private void cameraLocation(boolean isRandom, int locationIndx, LatLng setLatLang) {


        LatLng starterLocation;
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        if (setLatLang != null) {
            starterLocation = setLatLang;
            Log.e(TAG, "setLatLang !- null:" + starterLocation);

        } else {

            if (isRandom == true) {
                int rando = randInt(1, 4);
                starterLocation = databaseHelper.getStarterLocationBundle(rando).getLatlng();
                //add marker
                map.addMarker(new MarkerOptions()
                        .position(databaseHelper.getStarterLocationBundle(rando).getLatlng()));
                zoom = CameraUpdateFactory.zoomTo(10);
            } else {
                //TODO
                starterLocation = focusLocation.getLatlng();
                Log.e(TAG, "setLatLng == null" + starterLocation);

            }
        }

        //TODO
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(starterLocation);

        map.moveCamera(center);
        map.animateCamera(zoom);

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


    private void showAdderDialog(int soIndex, double lat, double lng) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putInt("locationBundleIndex", soIndex);
        bundle.putDouble("locationBundleLat", lat);
        bundle.putDouble("locationBundleLng", lng);
        // Create and show the dialog.
        DialogFragment newFragment = new MapsAdderDialogFragment();
        newFragment.setArguments(bundle);

        newFragment.show(ft, "dialog");

    }


    //this will be removed eventually...the dialog at least
    public void onDialogCompletion(LocationBundle locationBundle) {
//TODO
        map.addMarker(new MarkerOptions()
                .title(locationBundle.getLocalName())
                .position(locationBundle.getLatlng()))
                .showInfoWindow();

        /*if(daBundle2 != null) {
            soldierLocations = daBundle2;
            soldierLocationsSize = soldierLocations.size();
        }*/

        isLocationAdded = true;

        FABstatus++;

        actionButton.startAnimation(slideOut);
        actionButton.setVisibility(View.GONE);

        backer.startAnimation(leftSlideOut);
        backer.setVisibility(View.GONE);

    }

    public void lockingAction() {

        final AlertDialog.Builder lockerD = new AlertDialog.Builder(this);

        if(!isLocked) {
            if(isLocationAdded) {

                //TODO
                lockerD.setTitle("Locked-In")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                locker.setImageResource(R.drawable.lock_closed);
                                focusLocation.setIsLocationLocked(true);
                                databaseHelper.updateLocationBundle(focusLocation);

                                actionButton.setImageResource(R.drawable.icon_submit);
                                actionButton.setVisibility(View.VISIBLE);
                                actionButton.startAnimation(slidein);

                                FABstatus++;
                                dialog.cancel();
                            }
                        });
            }
            else {
                lockerD.setTitle("Are you sure?")
                        .setMessage(getResources().getString(R.string.notallspots))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                isLocked = true;

                                locker.setImageResource(R.drawable.lock_closed);
                                //todo
                                focusLocation.setIsLocationLocked(true);
                                databaseHelper.updateLocationBundle(focusLocation);

                                actionButton.setImageResource(R.drawable.icon_submit);
                                actionButton.setVisibility(View.VISIBLE);
                                actionButton.startAnimation(slidein);

                                FABstatus++;

                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
            }

            lockerD.show();

        } else {
            locker.setImageResource(R.drawable.lock_closed);
            //TODO
            focusLocation.setIsLocationLocked(true);
            databaseHelper.updateLocationBundle(focusLocation);

        }
    }

}




//////////// METHOD BANK ///////////////////////////

/*
        We're going to need these methods for:
        VIEWING RESULTS and ROUND REVIEW

        topLayout = (RelativeLayout) findViewById(R.id.topLayout);
        prevArrow = (ImageView) findViewById(R.id.previousMark);
        nextArrow = (ImageView) findViewById(R.id.nextMark);

        topLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditOpen) {
                    editView.setVisibility(View.VISIBLE);
                    isEditOpen = true;


                } else {
                    editView.setVisibility(View.GONE);
                    isEditOpen = false;
                }
            }
        });

                nextArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soldierLocationsSize > 0) {
                    locationIndex++;
                    if (locationIndex >= soldierLocationsSize) {
                        locationIndex = 0;
                        onDialogCompletion(soldierLocations.get(locationIndex), null);
                        cameraLocation(false, locationIndex, null);
                    } else {
                        onDialogCompletion(soldierLocations.get(locationIndex), null);
                        cameraLocation(false, locationIndex, null);
                    }
                }
            }
        });

        prevArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soldierLocationsSize > 0) {
                    locationIndex--;
                    if (locationIndex < 0) {
                        locationIndex = soldierLocationsSize - 1;
                        cameraLocation(false, locationIndex, null);
                        onDialogCompletion(soldierLocations.get(locationIndex), null);
                    } else {
                        onDialogCompletion(soldierLocations.get(locationIndex), null);
                        cameraLocation(false, locationIndex, null);
                    }
                }
            }
        });
*/

/*
    DELETE FUNCTIONALITY

        if (soldierLocationsSize > 0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Delete this location?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            //delete actions
                            LocationBundle tempLoc = soldierLocations.get(locationIndex);
                            Log.e("MAdeleteAlertDialog", tempLoc.getLocalName());
                            databaseHelper.deleteLocation(tempLoc);

                            soldierLocations = databaseHelper.getAllLocationsBySoldier(focusSoldier);
                            locationIndex--;

                            if (locationIndex > soldierLocationsSize
                                    || locationIndex < soldierLocationsSize) {
                                cameraLocation(true, -1, null);
                            } else {
                                cameraLocation(false, 0, null);
                            }
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
        else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No locations to delete...")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }

    */

