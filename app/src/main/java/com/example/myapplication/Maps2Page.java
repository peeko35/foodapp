package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.osmdroid.views.overlay.Polyline;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class Maps2Page extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView mapView;
    private DatabaseReference databaseReference;
    private FusedLocationProviderClient fusedLocationClient;
    private RequestQueue requestQueue;
    private double userLat, userLon;
    private LocationCallback locationCallback;
    private double stallLat, stallLon;
    private String vendorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_maps2_page);
        mapView = findViewById(R.id.mapView);
        requestQueue = Volley.newRequestQueue(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        vendorId = getIntent().getStringExtra("vendorId");
        databaseReference = FirebaseDatabase.getInstance().getReference("Vendors");


        // Request permissions and initialize map
        if (checkPermissions()) {
            requestLocationUpdates();
        } else {
            requestPermissions();
        }
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // Update location every 5 seconds
        locationRequest.setFastestInterval(2000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    userLat = location.getLatitude();
                    userLon = location.getLongitude();
                    Log.d("User Location", "Lat: " + userLat + ", Lon: " + userLon);

                    // Update the map
                    initializeMap();
                }
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMap();
            } else {
                finish(); // Close app if permission is denied
            }
        }
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        userLat = location.getLatitude();
                        userLon = location.getLongitude();
                        Log.d("User Location", "Lat: " + userLat + ", Lon: " + userLon);
                        initializeMap();
                    } else {
                        Toast.makeText(Maps2Page.this, "Failed to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            requestPermissions();
        }
    }

    private void initializeMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(true);

        // Center the map on the user's location if available
        GeoPoint startPoint;
        if (userLat != 0 && userLon != 0) {
            startPoint = new GeoPoint(userLat, userLon);
        } else {
            startPoint = new GeoPoint(19.0760, 72.8777); // Default to Mumbai
        }

        mapView.getController().setZoom(15.0);
        mapView.getController().setCenter(startPoint);

        // Add marker for user's location
        if (userLat != 0 && userLon != 0) {
            addMarker("Your Location", userLat, userLon);
        } else {
            Toast.makeText(this, "User location not available", Toast.LENGTH_SHORT).show();
        }

        if (vendorId != null) {
            fetchVendors();
        } else {
            Toast.makeText(this, "Vendor ID not found!", Toast.LENGTH_SHORT).show();
        }
    }

    // Add a marker
    private void fetchVendors() {
        databaseReference.child(vendorId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot vendorSnapshot = task.getResult();
                String stallName = vendorSnapshot.child("stallName").getValue(String.class);
                String address = vendorSnapshot.child("address").getValue(String.class);
                String location = vendorSnapshot.child("location").getValue(String.class);
                String pincode = vendorSnapshot.child("pincode").getValue(String.class);

                if (stallName != null && address != null && location != null && pincode != null) {
                    String fullAddress = address + ", " + location + ", India";
                    getCoordinates(stallName, fullAddress);
                } else {
                    Toast.makeText(this, "Incomplete vendor details", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Vendor not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCoordinates(String stallName, String fullAddress) {
        String encodedAddress = Uri.encode(fullAddress);
        String url = "https://nominatim.openstreetmap.org/search?q=" + encodedAddress + "&format=json";
        Log.d("Geocoding Request", "URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("Geocoding Response", "Response: " + response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            JSONObject address = jsonArray.getJSONObject(0);
                            stallLat = address.getDouble("lat"); // Set stall latitude
                            stallLon = address.getDouble("lon"); // Set stall longitude
                            Log.d("Geocoding Success", "Stall Lat: " + stallLat + ", Stall Lon: " + stallLon);

                            addMarker(stallName, stallLat, stallLon);

                            // Ensure stall coordinates are set before drawing the route
                            if (userLat != 0 && userLon != 0) {
                                drawRoute();
                            } else {
                                Log.d("Route Draw", "Waiting for user location...");
                            }
                        } else {
                            Log.e("Geocoding", "No coordinates found for: " + fullAddress);
                        }
                    } catch (Exception e) {
                        Log.e("Geocoding Error", e.getMessage());
                    }
                },
                error -> Log.e("Volley Error", error.toString()));

        requestQueue.add(stringRequest);
    }

    private void addMarker(String title, double latitude, double longitude) {
        GeoPoint point = new GeoPoint(latitude, longitude);
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setTitle(title);
        mapView.getOverlays().add(marker);
        // Refresh map
        mapView.invalidate();
        Log.d("Marker Added", "Title: " + title + ", Lat: " + latitude + ", Lon: " + longitude);
    }

    private void drawRoute() {
        if (userLat == 0 || userLon == 0 || stallLat == 0 || stallLon == 0) {
            Toast.makeText(this, "Waiting for location data...", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "https://router.project-osrm.org/route/v1/driving/" + userLon + "," + userLat + ";" + stallLon + "," + stallLat + "?overview=full&geometries=geojson";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray routes = jsonObject.getJSONArray("routes");
                        if (routes.length() > 0) {
                            JSONObject route = routes.getJSONObject(0);
                            JSONObject geometry = route.getJSONObject("geometry");
                            JSONArray coordinates = geometry.getJSONArray("coordinates");

                            List<GeoPoint> geoPoints = new ArrayList<>();
                            for (int i = 0; i < coordinates.length(); i++) {
                                JSONArray point = coordinates.getJSONArray(i);
                                double lon = point.getDouble(0);
                                double lat = point.getDouble(1);
                                geoPoints.add(new GeoPoint(lat, lon));
                            }

                            Polyline polyline = new Polyline();
                            polyline.setPoints(geoPoints);
                            mapView.getOverlays().add(polyline);
                            mapView.invalidate();
                            Log.d("Route Drawn", "Route successfully displayed on map.");
                        } else {
                            Log.e("Routing", "No route found.");
                        }
                    } catch (Exception e) {
                        Log.e("Routing Error", e.getMessage());
                    }
                },
                error -> Log.e("Volley Error", error.toString()));

        requestQueue.add(stringRequest);
    }
}