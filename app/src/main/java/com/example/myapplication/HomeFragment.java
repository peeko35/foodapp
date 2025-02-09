package com.example.myapplication;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    ViewFlipper v_flipper;
    RecyclerView recyclerView;
    List<MainModelrecy>dataList;
    DatabaseReference databaseReference;
    TextView textname;

    int[] imageos={
            R.drawable.panipuri,
            R.drawable.vadapav,
            R.drawable.chaat,
            R.drawable.nooodles,
            R.drawable.idli

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=view.findViewById(R.id.foodstallsrecylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Vendors");
        fetchVendorData();
        textname=view.findViewById(R.id.textname);
        fetchUserName();
        v_flipper=view.findViewById(R.id.v_flipper);

        for(int i=0;i<imageos.length;i++){
            flip_image(imageos[i]);
        }

        return view;

    }

    private void fetchUserName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid(); // Get current user ID
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        if (name != null) {
                            textname.setText("Welcome "+name); // Set user name in TextView
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(requireContext(), "Failed to fetch user name", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void fetchVendorData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataList.clear();
                for (DataSnapshot vendorSnapshot : dataSnapshot.getChildren()) {
                    // Fetch the stall name
                    String stallName = vendorSnapshot.child("stallName").getValue(String.class);

                    // Fetch the stallDetails for the vendor
                    DataSnapshot stallDetailsSnapshot = vendorSnapshot.child("stallDetails");
                    String vendorId = vendorSnapshot.getKey();

                    DataSnapshot ratingsSnapshot = vendorSnapshot.child("Ratings");
                    float totalRating = 0;
                    int count = 0;
                    for (DataSnapshot rating : ratingsSnapshot.getChildren()) {
                        try {
                            Object ratingValueObj = rating.child("rating").getValue();
                            if (ratingValueObj instanceof Number) {
                                totalRating += ((Number) ratingValueObj).floatValue();
                                count++;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    float averageRating = (count > 0) ? totalRating / count : 0;

                    // Fetch each food item details
                    for (DataSnapshot foodSnapshot : stallDetailsSnapshot.getChildren()) {
                        vendorId=vendorSnapshot.getKey();
                        String foodName = foodSnapshot.child("foodName").getValue(String.class);
                        String imageUrl = foodSnapshot.child("imageUrl").getValue(String.class);
                        String description = foodSnapshot.child("description").getValue(String.class);
                        String price = foodSnapshot.child("price").getValue(String.class);
                        // Add data to the list
                        MainModelrecy model = new MainModelrecy(stallName, foodName, imageUrl, price,description,vendorId,averageRating);
                        dataList.add(model);
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dataList.sort((m1, m2) -> Float.compare(m2.getAverageRating(), m1.getAverageRating()));
                }
                // Update the adapter with new data
                MainAdapterrecy mainAdapterrecy = new MainAdapterrecy(requireContext(), dataList); // Fixed context here
                recyclerView.setAdapter(mainAdapterrecy);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
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
