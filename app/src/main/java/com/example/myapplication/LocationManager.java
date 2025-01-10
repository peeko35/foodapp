package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class LocationManager {
    private static LocationManager instance = null;
    private Context context;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastLocation;
    private static int REQUEST_CHECK_SETTINGS;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    StringBuilder stringBuilder = new StringBuilder();
    private Activity activity;

    private LocationManager(){

    }
    public static LocationManager getInstance(Context context){
        if (instance==null){
            instance=new LocationManager();
        }
        instance.init(context);
        return instance;

    }

    private void init(Context context) {
        this.context=context;
        this.fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(context);
        if(context instanceof Activity){
            activity=(Activity) context;
        }
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if(locationResult==null){
                    return;
                }
                for(Location location : locationResult.getLocations()){
                    if(location!=null){
                        stringBuilder.setLength(0);
                        stringBuilder.append("Time:"+System.currentTimeMillis()+"\nLat:"+location.getLatitude() + "=>" + "Long:" +location.getLongitude());

                    }


                }
            }
        };
        createLocationRequest();
    }

    @SuppressLint("NewApi")
    protected void createLocationRequest() {
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY)
                .setIntervalMillis(10000) // 10 seconds
                .setMaxUpdateDelayMillis(30000) // 30 seconds max wait
                .build();
        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(context);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException){
                    try{
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(activity,REQUEST_CHECK_SETTINGS);
                    }catch (Exception sendEx){

                    }
                }

            }
        });
    }

    public Location getLastLocation(){
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            return null;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location==null){
                    startLocationUpdates();
                    lastLocation=null;               
                }else{
                    lastLocation=location;
                }

            }


        });
        return lastLocation;
    }
    public void startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
         && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());

    }
    public void stopLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}
