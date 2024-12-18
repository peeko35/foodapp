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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile_details extends AppCompatActivity {
    private TextView tvStallName, tvPhoneNumber, tvSelectLocation, tvAddress, tvPincode;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    ImageView imgproback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        mAuth = FirebaseAuth.getInstance();

        tvStallName = findViewById(R.id.tvStallName);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvSelectLocation = findViewById(R.id.tvSelectLocation);
        tvAddress = findViewById(R.id.tvAddress);
        tvPincode = findViewById(R.id.tvPincode);
        imgproback=findViewById(R.id.imgproback);

        String vendorId = mAuth.getCurrentUser().getUid();
        Log.d("ProfileDetails", "Fetching details for vendor: " + vendorId);

        // Fetch the vendor details from Firebase
        fetchVendorDetails(vendorId);
    }
    private void fetchVendorDetails(String vendorId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Vendors").child(vendorId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Retrieve details
                    String stallName = snapshot.child("stallName").getValue(String.class);
                    String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                    String selectLocation = snapshot.child("selectedLocation").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String pincode = snapshot.child("pincode").getValue(String.class);

                    // Set values to TextViews
                    tvStallName.setText("Stall Name: " + stallName);
                    tvPhoneNumber.setText("Phone Number: " + phoneNumber);
                    tvSelectLocation.setText("Location: " + selectLocation);
                    tvAddress.setText("Address: " + address);
                    tvPincode.setText("Pincode: " + pincode);
                } else {
                    Toast.makeText(Profile_details.this, "No profile found.", Toast.LENGTH_SHORT).show();
                }

            }



    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Toast.makeText(Profile_details.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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