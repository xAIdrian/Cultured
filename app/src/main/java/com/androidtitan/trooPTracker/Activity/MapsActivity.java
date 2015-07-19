package com.androidtitan.trooptracker.Activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidtitan.alphaarmyapp.R;
import com.androidtitan.trooptracker.Data.DatabaseHelper;
import com.androidtitan.trooptracker.Data.Soldier;
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


//todo: search for location
// http://wptrafficanalyzer.in/blog/adding-google-places-autocomplete-api-as-custom-suggestions-in-android-search-dialog/
//http://www.androidhive.info/2012/08/android-working-with-google-places-and-maps-tutorial/

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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
    private boolean locationClick = false; //true when getMyLocation or location search
                                           //false when location is added

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        databaseHelper = DatabaseHelper.getInstance(this);

        Intent intent = getIntent();
        soldierIndex = intent.getIntExtra("selectionToMap", -1);

        final Soldier tempSoldier = databaseHelper.getSoldier(soldierIndex);
        Log.e("MAcoolSoldierChecker", String.valueOf(tempSoldier.getLatLang()));

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
        LatLng defaultLatLng = new LatLng(0, 0);
        if(!tempSoldier.getLatLang().equals(defaultLatLng)) {
            CameraUpdate center = CameraUpdateFactory.newLatLng(tempSoldier.getLatLang());
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            map.moveCamera(center);
            map.animateCamera(zoom);
        }
        else {
            /* todo: If we end up here. Have camera focus on a famous location that the system
               todo: chooses from a constructed arrayList at random */
            //CameraUpdate center =
                    //CameraUpdateFactory.newLatLng(new LatLng(,));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
            //map.moveCamera(center);
            map.animateCamera(zoom);
        }


        //initializations
        handler = new Handler();

        editLoc = (ImageView) findViewById(R.id.editBtn);
        addLoc = (ImageView) findViewById(R.id.addBtn);
        locationGetter = (ImageView) findViewById(R.id.locBtn);
        locationGetter.setVisibility(View.GONE);

        /*LocationSource.OnLocationChangedListener(new LocationSource.OnLocationChangedListener() {
            @Override
            public void onLocationChanged(Location location) {

                slidein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidein_right);
                locationGetter.setVisibility(View.VISIBLE);
                locationGetter.startAnimation(slidein);
            }
        });*/

        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                //placeholder
            }
        });

        locationGetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                    //marker todo:change what will fill the text.
                    map.addMarker(new MarkerOptions().position(lastLatLang).title("PlaceHolder"));

                    locationClick = true;
                } else {
                    Toast.makeText(getApplicationContext(), "Please wait while your location loads...",
                            Toast.LENGTH_LONG);
                }
            }
        });

        addLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(locationClick) {
                    tempSoldier.setLatLng(currentLatitude, currentLongitude);
                    databaseHelper.updateSoldier(tempSoldier);
                    Log.e("MAaddLoc", tempSoldier.getfName() + " location set! " + tempSoldier.getLatitude()
                            + ", " + tempSoldier.getLongitude());
                } else {
                    Toast.makeText(getApplicationContext(), "Please wait while your location loads...",
                            Toast.LENGTH_LONG);
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

        map.addMarker(new MarkerOptions().position(new LatLng(38.8951,
                -77.0367)).title("Marker"));
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
}
