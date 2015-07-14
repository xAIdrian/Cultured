package com.androidtitan.trooptracker.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.androidtitan.alphaarmyapp.R;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap map; // Might be null if Google Play services APK is not available.
    private GoogleMapOptions options = new GoogleMapOptions();

    private ImageView editLoc;
    private ImageView addLoc;
    private ImageView locationGetter;

    Animation slidein;

    private LatLng currentLocation;
    private double currentLatitude;
    private double currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        //our handle for our MapFragment
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Sets the map type to be "hybrid"
        //todo: reevaluate what is enabled and what is disabled
        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false);

        //38.8951, -77.0367
        //Setting camera location and zoom
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(38.8951,
                        -77.0367));
        CameraUpdate zoom= CameraUpdateFactory.zoomTo(10);

        map.moveCamera(center);
        map.animateCamera(zoom);

        //additional settings for map
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);  //todo: https://developer.android.com/training/location/index.html
        map.setMyLocationEnabled(true);

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
                slidein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidein_right);
                locationGetter.setVisibility(View.VISIBLE);
                locationGetter.startAnimation(slidein);
            }
        });

        locationGetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLocation = new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude());
                currentLatitude = currentLocation.latitude;
                currentLongitude = currentLocation.longitude;

                //camera
                CameraUpdate center=
                        CameraUpdateFactory.newLatLng(currentLocation);
                CameraUpdate zoom= CameraUpdateFactory.zoomTo(15);
                map.moveCamera(center);
                map.animateCamera(zoom);

                //marker todo:change what will fill the text.
                map.addMarker(new MarkerOptions().position(currentLocation).title("PlaceHolder"));

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
}
