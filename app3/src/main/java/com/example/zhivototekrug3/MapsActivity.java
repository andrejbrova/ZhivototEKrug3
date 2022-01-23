package com.example.zhivototekrug3;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.zhivototekrug3.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    List<Address> listGeoCoder;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        location = getIntent().getStringExtra("location");

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            listGeoCoder = new Geocoder(this).getFromLocationName(location,1);
            double latitude = listGeoCoder.get(0).getLatitude();
            double longtude = listGeoCoder.get(0).getLongitude();
            LatLng loc = new LatLng(latitude,longtude);
            mMap.addMarker(new MarkerOptions().position(loc).title("Location of the Activity !!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}