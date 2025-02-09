package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile_details extends AppCompatActivity {
    private TextView tvStallName, tvPhoneNumber, tvSelectLocation, tvAddress, tvPincode;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String vendorId;
    ImageView imgproback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            vendorId = currentUser.getUid(); // Get logged-in vendor's ID
            databaseReference = FirebaseDatabase.getInstance().getReference("Vendors").child(vendorId);
            fetchVendorDetails();
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if user is not logged in
        }

        tvStallName = findViewById(R.id.tvStallName);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvSelectLocation = findViewById(R.id.tvSelectLocation);
        tvAddress = findViewById(R.id.tvAddress);
        tvPincode = findViewById(R.id.tvPincode);
        imgproback=findViewById(R.id.imgproback);
    }
    private void fetchVendorDetails() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Vendors").child(vendorId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Retrieve details
                    // Retrieve values from Firebase
                    String stallName = snapshot.child("stallDetails").child("stallName").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    String location = snapshot.child("location").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String pincode = snapshot.child("pincode").getValue(String.class);

                    // Set values to TextViews
                    tvStallName.setText("Stall Name: " + (stallName != null ? stallName : "N/A"));
                    tvPhoneNumber.setText("Phone Number: " + (phone != null ? phone : "N/A"));
                    tvSelectLocation.setText("Location: " + (location != null ? location : "N/A"));
                    tvAddress.setText("Address: " + (address != null ? address : "N/A"));
                    tvPincode.setText("Pincode: " + (pincode != null ? pincode : "N/A"));
                } else {
                    Toast.makeText(Profile_details.this, "Vendor data not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile_details.this, "Failed to load data!", Toast.LENGTH_SHORT).show();
            }
        });
        imgproback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_details.this,VendorHome.class);
                startActivity(intent);
                finish();
            }
        });

 }
}