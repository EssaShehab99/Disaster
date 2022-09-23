package com.example.form.new_;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.example.form.R;
import com.example.form.notification_service.MyService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
   private   FusedLocationProviderClient fusedLocationProviderClient;
   private    final int Request_code= 101;
   private   double lat, lng;


    public SharedPreferences prefs;
    Intent mServiceIntent;
    private MyService mMyService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_new);

        prefs = getSharedPreferences(Keys.PREFS_KEY, Context.MODE_PRIVATE);




        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this.getApplicationContext());




        mMyService = new MyService();
        mServiceIntent = new Intent(this, mMyService.getClass());




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       getCurrentLocation();
    }


    Location new_location;
    @SuppressLint("MissingPermission")
    private void getCurrentLocation(){

        if(ActivityCompat.checkSelfPermission(
              this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions
                    (this ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_code);

            return;
        }
        LocationRequest locationRequest=LocationRequest.create();
        locationRequest.setInterval(60000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(5000);
        LocationCallback locationCallback=new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
//                Toast.makeText(getApplicationContext(),"location result is="+locationResult
//                ,Toast.LENGTH_LONG).show();


                new_location=locationResult.getLastLocation();






                getCityName(new_location, new OnGeocoderFinishedListener() {

                    @Override
                    public void onFinished(List<Address> results) {
                        // do something with the result


                        String city=results.get(0).getLocality();

                        prefs = getSharedPreferences(Keys.PREFS_KEY, Context.MODE_PRIVATE);
                        if (city.length()>0) {



                            prefs.edit().putString(Keys.CITY_NAME,city).apply();

                            if (!isCheckServiceRunning(mMyService.getClass())) {
                                startService(mServiceIntent);
                            }
                        }
//                        Toast.makeText(MapsActivity.this, ""+results.get(0).getLocality()+"\n", Toast.LENGTH_SHORT).show();

                    }
                });


//                Toast.makeText(MapsActivity.this, ""+new_location, Toast.LENGTH_SHORT).show();
                for (Location location:locationResult.getLocations()){
                    if (location!=null){
//                        Toast.makeText(getApplicationContext(),"Current location is="+location.getLongitude()
//
//                                ,Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
           @SuppressLint("MissingPermission") Task<Location> task=fusedLocationProviderClient.getLastLocation();
           task.addOnSuccessListener(location -> {
               if (location != null){

                   lat=location.getLatitude();
                   lng=location.getLongitude();


                   LatLng latLng=new LatLng(lat,lng);
                   mMap.addMarker(new MarkerOptions().position(latLng).title("current location"));
                   mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

               }
           });
          }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull int[] grantResults) {

        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){

               getCurrentLocation();
    }


    }




    public void getCityName(final Location location, final OnGeocoderFinishedListener listener) {
        new AsyncTask<Void, Integer, List<Address>>() {
            @Override
            protected List<Address> doInBackground(Void... arg0) {
                Geocoder coder = new Geocoder(MapsActivity.this, Locale.ENGLISH);
                List<Address> results = null;
                try {
                    results = coder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    // nothing
                }
                return results;
            }

            @Override
            protected void onPostExecute(List<Address> results) {
                if (results != null && listener != null) {
                    listener.onFinished(results);
                }
            }
        }.execute();
    }

    public abstract class OnGeocoderFinishedListener {
        public abstract void onFinished(List<Address> results);
    }










    private boolean isCheckServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

}