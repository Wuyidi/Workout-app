package edu.monash.fit4039.gym_fitness_workout_tracker.activity;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import java.util.ArrayList;

import edu.monash.fit4039.gym_fitness_workout_tracker.R;

public class MapActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // Min and Max Update Intervals for our Location Service
    private static final long MAX_UPDATE_INTERVAL = 10000; // 10 Seconds
    private static final long MIN_UPDATE_INTERVAL = 2000;  // 2 Seconds
    private static final float SMALLEST_DISPLACEMENT = 0.25F; //quarter of a meter


    // Instance variable
    private GoogleMap googleMap;
    private GoogleApiClient APIClient;
    private LocationRequest locationRequest;
    private Marker currentLocationMarker;
    private Location currentLocation;
    // Unique Identifier for receiving result
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private ArrayList<LatLng> points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        points = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Get access to our MapFragment
        MapFragment mapFrag = (MapFragment)
                getFragmentManager().findFragmentById(R.id.map_fragment);
        // Set up an asynchronous callback to let us know when the map has loaded

        mapFrag.getMapAsync(this);


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Android 6.0 & up added security permissions
        // If the user rejects allowing access to location data then this try block
        // will stop the application from crashing (Will not track location)
        try {
            // Set up a constant updater for location services
            locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                    .setInterval(MAX_UPDATE_INTERVAL)
                    .setFastestInterval(MIN_UPDATE_INTERVAL)
                    .setSmallestDisplacement(SMALLEST_DISPLACEMENT);


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi
                        .requestLocationUpdates(APIClient, locationRequest, this);
            }
        } catch (SecurityException secEx) {
            feedback("ERROR: Please enable location services");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        currentLocation = location;
        // Place current location marker
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        points.add(latLng);
        readLine();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(points.get(0));
        markerOptions.title("Start Position");
        // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        currentLocationMarker = googleMap.addMarker(markerOptions);

        // Move map camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Function is called once the map has fully loaded.
        // Set our map object to reference the loaded map
        this.googleMap = googleMap;
        // Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            this.googleMap.setMyLocationEnabled(true);
        }


    }

    // Build google Api client
    protected synchronized void buildGoogleApiClient() {
        if (APIClient == null) {
            // Create our API Client and tell it to connect to Location Services
            APIClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        APIClient.connect();
    }

    // Check the permission
    // https://www.codeproject.com/Articles/1113633/Google-Map-Tutorial-in-Android-Studio-How-to-get-c
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (APIClient == null) {
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }
                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    feedback("Permission denied");
                }
                return;
        }
    }


    // Draw a poly line on the map
    // https://stackoverflow.com/questions/17341088/android-draw-polyline-maps-v2
    private void readLine() {
        googleMap.clear();
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        //add Polyline
        googleMap.addPolyline(options);

    }


}