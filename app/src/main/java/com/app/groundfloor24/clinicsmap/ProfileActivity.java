package com.app.groundfloor24.clinicsmap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class ProfileActivity extends AppCompatActivity implements OnMapReadyCallback {

    String name, price, lat, lng;

    TextView tvName, tvAddress, tvSuper, tvUnleaded, tvDiesel, tvBusy;
    RoundedImageView img;
    Toolbar toolbar;

    FirebaseFirestore db;
    SeekBar seekBar;
    int random = 1;
    MapView mapView;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        db = FirebaseFirestore.getInstance();
        name = getIntent().getStringExtra("name");
        price = getIntent().getStringExtra("price");
        lat = getIntent().getStringExtra("lat");
        lng = getIntent().getStringExtra("lng");
        random = getIntent().getIntExtra("random", 1);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        img = findViewById(R.id.img);
        tvName = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);


        tvName.setText(name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (random == 1) {
            img.setImageResource(R.drawable.glen);

        }
        if (random == 2) {
            img.setImageResource(R.drawable.bri);

        }
        if (random == 3) {
            img.setImageResource(R.drawable.qeen);

        }

        mapView = (MapView) findViewById(R.id.mapView);



        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
               // googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.addMarker(new MarkerOptions().position(new LatLng(43.599695, -79.645314)));

                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(43.599695, -79.645314)));

                googleMap.setMyLocationEnabled(true);

                mapView.onResume();
                // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
                try {
                    MapsInitializer.initialize(ProfileActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // decode the address from lat lng
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(ProfileActivity.this, Locale.getDefault());

        try {

            addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();


            tvAddress.setText(address + " " + state + " " + city + " " + country);
            //Log.e("address ", "onActivityResult: "+address+" "+state+" "+city+" "+country );

            // tvSearch.setText(address+" "+state+" "+city+" "+country);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
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
        map.setMyLocationEnabled(true);

       //in old Api Needs to call MapsInitializer before doing any CameraUpdateFactory call


        // Updates the location and zoom of the MapView
        /*CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
        map.animateCamera(cameraUpdate);*/


        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(43.599695, -79.645314)));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(43.599695, -79.645314)));

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}