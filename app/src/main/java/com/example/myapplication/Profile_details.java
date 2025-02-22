package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

     TextView VstallNm,Vphone,Vadd,Vloc;
     Button Vlogout,Vedit;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    FirebaseUser vendor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        auth=FirebaseAuth.getInstance();
        VstallNm=findViewById(R.id.stallName);
        Vphone=findViewById(R.id.Vendorphne);
        Vadd=findViewById(R.id.Vendoradd);
        Vloc=findViewById(R.id.Vendorloc);
        Vlogout=findViewById(R.id.Vlogout);
        Vedit=findViewById(R.id.VeditProfile);

        Vedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_details.this,VeditProfile.class);
                startActivity(intent);
            }
        });

        vendor = auth.getCurrentUser();
        if (vendor != null) {
            String vendorId = vendor.getUid(); // Get the logged-in vendor's UID

            // Reference to the vendor's details in the database
            databaseReference = FirebaseDatabase.getInstance().getReference("Vendors").child(vendorId);

            // Fetch vendor details from Firebase
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String stallName = snapshot.child("stallName").getValue(String.class);
                        String address = snapshot.child("address").getValue(String.class);
                        String phone = snapshot.child("phone").getValue(String.class);
                        String location = snapshot.child("location").getValue(String.class);

                        // Set values in TextViews
                        VstallNm.setText(stallName);
                        Vadd.setText(address);
                        Vphone.setText(phone);
                        Vloc.setText(location);
                    } else {
                        Toast.makeText(Profile_details.this, "Vendor details not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Profile_details.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Firebase", "Error fetching data", error.toException());
                }
            });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
        Vlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out user and redirect to UserLogin activity
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Profile_details.this, VendorLogin.class);
                startActivity(intent);
                Profile_details.this.finish();
            }
        });
    }

}