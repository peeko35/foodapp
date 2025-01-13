package com.example.myapplication;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.location.LocationManager;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    ViewFlipper v_flipper;
    ImageView locpin;
    TextView textmp;
    private String[] foregroundLocationPermission={Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private PermissionManager permissionManager;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationClient;

    int[] imageos={
            R.drawable.panipuri,
            R.drawable.vadapav,
            R.drawable.chaat,
            R.drawable.nooodles,
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        locpin=view.findViewById(R.id.locpin);
        textmp=view.findViewById(R.id.textmp);
        permissionManager=PermissionManager.getInstance(requireContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());


        v_flipper=view.findViewById(R.id.v_flipper);

        locpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!permissionManager.checkPermissions(foregroundLocationPermission)){
                    permissionManager.askPermissions(HomeFragment.this,foregroundLocationPermission,100);

                }else{
                    getAddress();
                }
            }
        });

        for(int i=0;i<imageos.length;i++){
            flip_image(imageos[i]);
        }

        return view;

    }
    private void getAddress(){
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "Location permissions are required.", Toast.LENGTH_SHORT).show();
            return;
        }

       
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    Address address = addresses.get(0);

                    String strAddress = "" + address.getAddressLine(0) + "\n" +
                            "Admin Area:" + address.getAdminArea() + "\n" +
                            "Country Name:" + address.getCountryName() + "\n" +
                            "Feature Name:" + address.getFeatureName() + "\n" +
                            "Locality:" + address.getLocality() + "\n";
                    textmp.setText(strAddress);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(requireContext(), "Please try again", Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                getAddress();
            } else {
                // Permission denied
                Toast.makeText(requireContext(), "Location permission is required to use this feature.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void flip_image(int i) {
        ImageView view =new ImageView(requireContext());
        view.setBackgroundResource(i);
        v_flipper.addView(view);
        v_flipper.setFlipInterval(4000);
        v_flipper.setAutoStart(true);

        v_flipper.setInAnimation(requireContext(),android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(requireContext(),android.R.anim.slide_out_right);
    }
}
