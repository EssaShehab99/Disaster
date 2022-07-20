package com.example.disaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class DisasterMap extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    boolean locationPermissionGranted = false;
    GoogleMap map;
    TextView title, description, phone;
    List<HelpCenterModel> helpCenterModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disaster_map);
        title = findViewById(R.id.title_tv);
        description = findViewById(R.id.description_tv);
        phone = findViewById(R.id.phone_tv);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        updateLocationUI();

        getDeviceLocation();
        map.setOnMarkerClickListener(this);
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(this);
                @SuppressLint("MissingPermission")
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    private static final float DEFAULT_ZOOM = 11f;

                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Location lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                map.addMarker(new MarkerOptions()
                                        .position(new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()))
                                        .title("You are here"));
                                initial();
                            }
                        } else {
                            LatLng defaultLocation = new LatLng(0, 0);
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    public void initial() {

        helpCenterModels.add(new HelpCenterModel("first help center", "777111222", "first help center description", new LatLng(15.41741396036151, 44.23907207834295)));
        helpCenterModels.add(new HelpCenterModel("second help center", "777111333", "second help center description", new LatLng(15.418376847811542, 44.167156102268336)));
        helpCenterModels.add(new HelpCenterModel("third help center", "777111444", "third help center description", new LatLng(15.390856073209445, 44.19097679223114)));
        helpCenterModels.add(new HelpCenterModel("fourth help center", "777111555", "fourth help center description", new LatLng(15.408790172241229, 44.29015245778259)));
        addMarkersToMap(helpCenterModels);
    }

    private void addMarkersToMap(List<HelpCenterModel> helpCenterModels) {
        for (HelpCenterModel helpCenter : helpCenterModels) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(helpCenter.getLatLng());
            markerOptions.title(helpCenter.getName());
            markerOptions.snippet(helpCenter.getDescription());
            int height = 100;
            int width = 100;
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icon_marker);
            BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(b, width, height, false));
            markerOptions.icon(smallMarkerIcon);
            map.addMarker(markerOptions);
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        helpCenterModels.forEach(helpCenterModel -> {
            if (marker.getPosition().equals(helpCenterModel.getLatLng())) {
                title.setText(helpCenterModel.getName());
                description.setText(helpCenterModel.getDescription());
                phone.setText(helpCenterModel.getPhone());
            }
        });
        return false;
    }
}