package com.app.groundfloor24.clinicsmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.groundfloor24.clinicsmap.model.MyItem;
import com.app.groundfloor24.clinicsmap.model.Clinics;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Maps2Activity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    private static String TAG = "MAP LOCATION";
    private LatLng mCenterLatLong;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private Location mylocation;
    private GoogleApiClient mGoogleApiClient;
    LocationManager locationManager;
    TextView tvLocation;
    LocationListener locationListener;
    GoogleMap mMap;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    TextView mLocationMarkerText;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 6;
    Context mContext;
    Toolbar mToolbar;
    TextView tvSearch;
    Location loc;
    boolean click = true;
    ProgressBar ProgressBar;
    AHBottomNavigation bottomNavigation;
    private ClusterManager<MyItem> clusterManager;
    MyItem item;
    String geoHashlocation;
    boolean check = true;
    LatLng latLngMarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);
        mContext = this;



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        // ==========init views===========

        mLocationMarkerText = (TextView) findViewById(R.id.locationMarkertext);
        ProgressBar =  findViewById(R.id.progress);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvSearch =  findViewById(R.id.tvSearch);
        mapFragment.getMapAsync(this);


        //===========location client=============
        setUpGClient();


        //top search bar
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity();
            }
        });


        //Replace with your key here
        String apiKey = "AIzaSyB6SgFAFFwCKyTSZvGWX9X9fbPWqbivo1Y";
        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }



        //==========Bottom Navigation=============

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bnve);

        //bottom bar
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.home, R.color.teal_200);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Clinics", R.drawable.clinic, R.color.teal_200);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Map", R.drawable.pin, R.color.teal_200);


        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);



        bottomNavigation.setVisibility(View.VISIBLE);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#050506"));

        bottomNavigation.setAccentColor(Color.parseColor("#FF9800"));
        bottomNavigation.setInactiveColor(Color.parseColor("#5F7586"));
        bottomNavigation.enableItemAtPosition(2);
        bottomNavigation.setCurrentItem(2);
        bottomNavigation.setForceTint(true);

        bottomNavigation.setNotificationBackgroundColor(getResources().getColor(R.color.teal_200));
        bottomNavigation.setNotificationBackgroundColorResource(R.color.teal_200);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setOnTabSelectedListener(ob);







    }


    //when maps get ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        mMap = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.mapsstyle));


        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {


                ProgressBar.setVisibility(View.VISIBLE);
                click = false;
            }


        });



        // ===================when camera stops location stations=======================
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {




                if(latLngMarker!=null){

                    ProgressBar.setVisibility(View.GONE);

                }



                if (latLngMarker==null ) {


                    Log.e("lol----", "aaaa");
                    ProgressBar.setVisibility(View.GONE);

                    // get pin location
                    String lat = String.valueOf(loc.getLatitude());
                    String lng = String.valueOf(loc.getLongitude());


                    // init cluster
                    if (clusterManager == null) {
                        clusterManager = new ClusterManager<MyItem>(Maps2Activity.this, mMap);
                    }
                    clusterManager.setRenderer(new CustomClusterRenderer(Maps2Activity.this, mMap, clusterManager));



                    clusterManager.clearItems();

                    clusterManager.cluster();

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @SuppressLint("PotentialBehaviorOverride")
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            ProgressBar.setVisibility(View.GONE);


                            check = false;

                            latLngMarker = marker.getPosition();

                            //Log.e("uuu----", marker.getTitle());

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                                    intent.putExtra("name",marker.getTitle());
                                    intent.putExtra("price","price");
                                    final int min = 1;
                                    final int max = 3;
                                    final int random = new Random().nextInt((max - min) + 1) + min;
                                    intent.putExtra("random",random);

                                    intent.putExtra("lat",String.valueOf(marker.getPosition().latitude));
                                    intent.putExtra("lng",String.valueOf(marker.getPosition().longitude));

                                    startActivity(intent);
                                }
                            },1000);


                            return false;

                        }
                    });



                    stationList2.clear();
//=========================================== Loading Fuel Stations =======================================
                    StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                    googlePlacesUrl.append("location=" + lat + "," +lng);
                   // googlePlacesUrl.append("&rankby=distance");
                    googlePlacesUrl.append("&radius=5500");

                    googlePlacesUrl.append("&type=" + "hospital");
                    googlePlacesUrl.append("&key=" + "AIzaSyB6SgFAFFwCKyTSZvGWX9X9fbPWqbivo1Y");
                    //"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.519159,-0.5267714&radius=5500&type=clinic&key=AIzaSyB6SgFAFFwCKyTSZvGWX9X9fbPWqbivo1Y"
                   // String aa = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.519159,-0.5267714&radius=5500&type=gas_station&keyword=cruise&key=AIzaSyB6SgFAFFwCKyTSZvGWX9X9fbPWqbivo1Y";

                    //"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=43.599695,-79.645314&radius=5500&type=hospital&key=AIzaSyB6SgFAFFwCKyTSZvGWX9X9fbPWqbivo1Y"
                    JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, googlePlacesUrl.toString(), null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            JSONObject obj = response;

                            try {
                                JSONArray arr = obj.getJSONArray("results");

                                Log.e("ooopppppppp", "onResponse: "+arr.toString() );
                                try {
                                    for(int i=0;i<arr.length();i++)
                                    {
                                        JSONObject jsonObject = arr.getJSONObject(i);
                                        String business_status = jsonObject.getString("business_status");
                                        String name = jsonObject.getString("name");
                                        String icon = jsonObject.getString("icon");

                                        String rating = jsonObject.getString("rating");
                                        String rating_user = jsonObject.getString("user_ratings_total");
                                        JSONObject geometry = jsonObject.getJSONObject("geometry");
                                        JSONObject location = geometry.getJSONObject("location");
                                        String lat = location.getString("lat");
                                        String lng = location.getString("lng");


                                        Log.e("aq", "onResponse: "+name );

                                        double distance = distance(loc.getLatitude(),loc.getLongitude(),Double.parseDouble(lat),Double.parseDouble(lng) );

                                        Log.e("aq", "onResponse:fffff  "+distance );

                                        Clinics clinics = new Clinics(name,lat,lng,icon,String.format("%.2f", distance));

                                        stationList2.add(clinics);
                                        String geoHashStations = geoHashFromLoc(Double.parseDouble(lat), Double.parseDouble(lng));

                                        if (geoHashlocation != null && !geoHashlocation.isEmpty()) {

                                            if (geoHashlocation.equalsIgnoreCase(geoHashStations)) {
                                                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                                                item = new MyItem(latLng, name, "Connection: " + name);
                                                clusterManager.addItem(item);
                                                Log.e("Station==================== ", String.valueOf(i));

                                            }

                                        }

                                        clusterManager.cluster();

                                    }



                                    Log.e("apna", "onResponse: "+stationList2.size() );



                                }

                                catch (Exception w)
                                {
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.e("rrr", "onErrorResponse: "+error.getMessage() );
                        }
                    });
                    AppController.getInstance().addToRequestQueue(jsonArrayRequest);

//===========================================================================================






                    check = true;

                }
                latLngMarker= null;

            }
        });



        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {



                //==============when camera moves get geohash of the box======================

                mCenterLatLong = cameraPosition.target;


                click = true;

                try {

                    Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);

                    loc = mLocation;
                    //Here is main location
                    geoHashlocation = geoHashFromLoc(mCenterLatLong.latitude,mCenterLatLong.longitude);




                } catch (Exception e) {
                    e.printStackTrace();
                }














            }

        });



        //===============================location permission check==================
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return;
        }





    }

    public static List<Clinics> stationList2 = new ArrayList<>();



    //======Get Location permision=======
    private synchronized void setUpGClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(Maps2Activity.this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    //=======get Current location===========
    private void getMyLocation() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(Maps2Activity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {

                    mylocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(mGoogleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(mGoogleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.


                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(Maps2Activity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(mGoogleApiClient);
                                    }

                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But  could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(Maps2Activity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        //finish();
                        break;
                }
                break;
        }


        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                //Place place = PlaceAutocomplete.getPlace(mContext, data);
                com.google.android.libraries.places.api.model.Place place = Autocomplete.getPlaceFromIntent(data);

                tvSearch.setText(place.getName().toString());
                // TODO call location based filter


                LatLng latLong;


                latLong = place.getLatLng();

                loc.setLatitude(latLong.latitude);
                loc.setLongitude(latLong.longitude);

                //mLocationText.setText(place.getName() + "");

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLong).zoom(15f).tilt(0).build();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));


                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());

                try {

                    addresses = geocoder.getFromLocation(latLong.latitude, latLong.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    Log.e("address ", "onActivityResult: "+address+" "+state+" "+city+" "+country );

                    // tvSearch.setText(address+" "+state+" "+city+" "+country);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(mContext, data);
        } else if (resultCode == RESULT_CANCELED) {
            // Indicates that the activity closed before a selection was made. For example if
            // the user pressed the back button.
        }
    }

    //===========checking location permission===========
    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(Maps2Activity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }

    }


    // =====when permission is granted==========
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionLocation = ContextCompat.checkSelfPermission(Maps2Activity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {

            getMyLocation();

        }
    }

    public static boolean isLocationEnabled(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return true;
        } else {

            return false;
        }
    }

    //========Update location on map Change=========
    private void changeMap(Location location) {


        // check if map is created successfully or not
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(false);
            LatLng latLong;


            Log.e("latlong", location.getLatitude() + " " + location.getLongitude());
            latLong = new LatLng(location.getLatitude(), location.getLongitude());

            loc = location;
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(15f).tilt(0).build();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));



            mLocationMarkerText.setText("Lat : " + location.getLatitude() + "," + "Long : " + location.getLongitude());




        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }

    }


    // ============Top locaiton search bar======
    private void openAutocompleteActivity() {
        // The autocomplete activity requires Google Play Services to be available. The intent
        // builder checks this and throws an exception if it is not the case.


        List<com.google.android.libraries.places.api.model.Place.Field> fields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.ADDRESS, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields) //NIGERIA
                .build(this);
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);

//            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
//            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);


    }










    @Override
    protected void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        checkPermissions();
    }


    // when location changes
    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null)
                changeMap(location);
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }








    @Override
    protected void onResume() {
        super.onResume();


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();


    }


    // =========get geohash code============
    public String geoHashFromLoc(double lat, double lng){


        Location location = new Location("") ;
        location.setLatitude(lat);
        location.setLongitude(lng);
        com.app.groundfloor24.clinicsmap.GeoHash geoHash = com.app.groundfloor24.clinicsmap.GeoHash.fromLocation(location, 1);//Radius


        return geoHash.toString();



    }



    AHBottomNavigation.OnTabSelectedListener ob = new AHBottomNavigation.OnTabSelectedListener() {
        @Override
        public boolean onTabSelected(int position, boolean wasSelected) {
            switch(position){

                case 0:


                    Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(0, 0);
//                    bottomNavigation.enableItemAtPosition(0);
//                    bottomNavigation.setCurrentItem(0);
                    break;

                case 1:

//                    bottomNavigation.enableItemAtPosition(1);
//                    bottomNavigation.setCurrentItem(1);

                    String lat = String.valueOf(loc.getLatitude());
                    String lng = String.valueOf(loc.getLongitude());

                    Intent intent = new Intent(getApplicationContext(),FilterActivity.class);
                    intent.putExtra("lat",loc.getLatitude());
                    intent.putExtra("lng",loc.getLongitude());

                    startActivity(intent);
                    overridePendingTransition(0, 0);

                    break;
                case 2:
                    Intent intent2 = new Intent(getApplicationContext(),Maps2Activity.class);
                    startActivity(intent2);
                    overridePendingTransition(0, 0);
//                    bottomNavigation.enableItemAtPosition(1);
//                    bottomNavigation.setCurrentItem(1);


                    break;
            }

            return true;
        }
    };


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}

