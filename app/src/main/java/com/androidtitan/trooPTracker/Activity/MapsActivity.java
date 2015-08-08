package com.androidtitan.trooptracker.Activity;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooptracker.Data.DatabaseHelper;
import com.androidtitan.trooptracker.Data.LocationBundle;
import com.androidtitan.trooptracker.Data.Soldier;
import com.androidtitan.trooptracker.Dialog.MapsAdderDialogFragment;
import com.androidtitan.trooptracker.Interface.MapsPullInterface;
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

import java.util.List;
import java.util.Random;


//todo: search for location
// http://wptrafficanalyzer.in/blog/adding-google-places-autocomplete-api-as-custom-suggestions-in-android-search-dialog/
//http://www.androidhive.info/2012/08/android-working-with-google-places-and-maps-tutorial/

public class MapsActivity extends FragmentActivity implements MapsPullInterface, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "MapActivity";

    private static final String SAVED_INITIAL_BOOL = "isInitialOpening";
    private static final String SAVED_DIALOG_BOOL = "savedDialogBool";

    DatabaseHelper databaseHelper;

    private GoogleMap map; // Might be null if Google Play services APK is not available.
    private GoogleMapOptions options = new GoogleMapOptions();
    private GoogleApiClient googleAPIclient;

    private Soldier tempSoldier;
    private List<LocationBundle> soldierLocations;
    private int soldierLocationsSize;

    private Handler handler;

    private ImageView addLoc;
    private ImageView locationGetter;
    private RelativeLayout editView;
    private ImageView locker;

    private Animation slidein;

    private Location lastLocation;
    private LatLng lastLatLang;
    private double currentLatitude;
    private double currentLongitude;

    private int soldierIndex = -1;
    private boolean locationClick = false;
    private int locationIndex = -1;

    private boolean isInitialOpening = true;
    private boolean isLocationAdded = false; //use this to control yo dialogs
    private boolean isLocked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if(savedInstanceState != null) {

            isInitialOpening = savedInstanceState.getBoolean(SAVED_INITIAL_BOOL);
            isLocationAdded = savedInstanceState.getBoolean(SAVED_DIALOG_BOOL);
        }

        databaseHelper = DatabaseHelper.getInstance(this);

        Intent intent = getIntent();
        //todo: our indexing troubles
        soldierIndex = intent.getIntExtra("selectionToMap", -1);
        soldierIndex--;
        Log.e(TAG, String.valueOf(soldierIndex));

        //todo
        tempSoldier = databaseHelper.getSoldier(soldierIndex);
        soldierLocations = databaseHelper.getAllLocationsBySoldier(tempSoldier);
        soldierLocationsSize = soldierLocations.size();

        //setting up InstructionNumber so it works with our Dialogs
        locationIndex = soldierLocationsSize - 1;

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

        //place markers for every saved location
        for (LocationBundle bund : databaseHelper.getAllLocations()) {
            try {

                map.addMarker(new MarkerOptions()
                        .position(bund.getLatlng())
                        .title(bund.getLocalName())
                        .snippet(databaseHelper.getLocationsSoldier(bund).getFullName()));
            } catch (CursorIndexOutOfBoundsException e) {

                map.addMarker(new MarkerOptions()
                        .position(bund.getLatlng())
                        .title(bund.getLocalName()));
            }
        }

        if (soldierLocationsSize > 0) { //if there is something present
            /*this currently gets the first location in their many saved locations...
            eventually we want to be able to 'page' through all of them
            */
            cameraLocation(false, locationIndex, null);

        } else {

            cameraLocation(true, -1, null);

        }


        //log printer
        for (LocationBundle loc : soldierLocations) {
            Log.e("MAonCreate", loc.getId() + " " + loc.getLocalName() + ", " + loc.getLatlng());
        }
        databaseHelper.printCoordinatesTable();

        //initializations

        locker = (ImageView) findViewById(R.id.locker);

        addLoc = (ImageView) findViewById(R.id.addBtn);
        locationGetter = (ImageView) findViewById(R.id.locBtn);
        addLoc.setVisibility(View.GONE);
        locationGetter.setVisibility(View.GONE);

        //if we've used all of our locations then we lock-it up
        /*if(soldierLocationsSize == 0) {
            customDialogHandler();
        }*/
        if(soldierLocationsSize > 0) {
            isLocationAdded = true;
            //lockingAction();
            locker.setImageResource(R.drawable.lock_closed);
            addLoc.setVisibility(View.GONE); //this is not being called
        }
        else {
            customDialogHandler();
        }


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
                        cameraLocation(false, -1, lastLatLang);

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

                        customDialogHandler();
                        locationClick = false;

                    } else {
                        //Dialog saying "You need to get your location first!"
                        final AlertDialog.Builder noLocationDialog = new AlertDialog.Builder(MapsActivity.this);



                            noLocationDialog.setTitle("You need to get your location")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            noLocationDialog.show();
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
    public void onConnected(Bundle bundle) {
        Log.e("APIclientConnected?", "Connection Suspended!");
        slidein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidein_right);
        handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                locationGetter.setVisibility(View.VISIBLE);
                locationGetter.startAnimation(slidein);

                if(!isLocationAdded) {
                    addLoc.setVisibility(View.VISIBLE);
                    addLoc.startAnimation(slidein);
                }
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


    //todo: custom methods
/////////////////////////////////////////////////////////////////////////////////////////////////////

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View v;
        private TextView infoHeader;
        private TextView infoSecondary;

        private String title;
        private String secondary;

        public CustomInfoWindowAdapter() {
            v = getLayoutInflater().inflate(R.layout.info_window_custom,
                    null);
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
    private void cameraLocation(boolean isRandom, int locationIndx, LatLng setLatLang) {

        LatLng starterLocation;
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        if (setLatLang != null) {
            starterLocation = setLatLang;

        } else {

            if (isRandom == true) {
                int rando = randInt(1, 4);
                starterLocation = databaseHelper.getStarterLocationBundle(rando).getLatlng();
                Log.e("MAcameraLocation", String.valueOf(starterLocation));
                //add marker
                map.addMarker(new MarkerOptions()
                        .position(databaseHelper.getStarterLocationBundle(rando).getLatlng()));
                zoom = CameraUpdateFactory.zoomTo(10);
            } else {
                starterLocation = databaseHelper
                        .getAllLocationsBySoldier(tempSoldier).get(locationIndx).getLatlng();

            }
        }

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
        bundle.putInt("soldierIndex", soIndex);
        //bundle.putInt("locationBundleIndex", index);
        bundle.putDouble("locationBundleLat", lat);
        bundle.putDouble("locationBundleLng", lng);
        // Create and show the dialog.
        DialogFragment newFragment = new MapsAdderDialogFragment();
        newFragment.setArguments(bundle);

        newFragment.show(ft, "dialog");

    }

    private void customDialogHandler() {

        final AlertDialog.Builder aDawg = new AlertDialog.Builder(this);

        if (isInitialOpening) {
            isInitialOpening = false;

            if(!isLocationAdded) {
                aDawg.setTitle(R.string.careful)
                        .setMessage(R.string.initial_instruction)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                aDawg.show();
            } else {
                aDawg.setTitle(R.string.youDone)
                        .setMessage("You are out of Spots\nLock-In if you haven't")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                aDawg.show();
            }
        }
        else {
            showAdderDialog(soldierIndex, currentLatitude, currentLongitude);
        }



    }

    public void onDialogCompletion(LocationBundle locationBundle, List<LocationBundle> daBundle2) {

        map.addMarker(new MarkerOptions()
                .title(locationBundle.getLocalName())
                .position(locationBundle.getLatlng())
                .snippet(databaseHelper.getLocationsSoldier(locationBundle).getFullName()))
        .showInfoWindow();

        if(daBundle2 != null) {
            soldierLocations = daBundle2;
            soldierLocationsSize = soldierLocations.size();
        }
        isLocationAdded = true;

    }

    public void lockingAction() {

        final AlertDialog.Builder lockerD = new AlertDialog.Builder(this);

        if(!isLocked) {
            if(isLocationAdded) {

                lockerD.setTitle("Locked-In")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                locker.setImageResource(R.drawable.lock_closed);
                                addLoc.setVisibility(View.GONE);
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
                                addLoc.setVisibility(View.GONE);
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
            addLoc.setVisibility(View.GONE);
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

                            soldierLocations = databaseHelper.getAllLocationsBySoldier(tempSoldier);
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

