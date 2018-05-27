package com.example.marko.app1.activities;

import android.graphics.Camera;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.marko.app1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //
    private Location pinLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        String loclat = getIntent().getStringExtra("lat");
        String loclng = getIntent().getStringExtra("lng");

        pinLocation = new Location("");
        pinLocation.setLatitude(Double.valueOf(loclat));
        pinLocation.setLongitude(Double.valueOf(loclng));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng postLocation = new LatLng(pinLocation.getLatitude(), pinLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(postLocation).title("Post Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(postLocation, 17f));

    }
}
