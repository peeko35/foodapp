package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Vendor_Profile extends AppCompatActivity {
    EditText vendorPh,stallname,addr,pincode;
    Spinner locspin;
    Button btn_save;
   // FirebaseDatabase db;

    private DatabaseReference databaseReference;
    private DatabaseReference idReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_profile);
        vendorPh=findViewById(R.id.VendorPhone);
        locspin=findViewById(R.id.locspin);
        stallname=findViewById(R.id.Stallname);
        addr=findViewById(R.id.addr);
        pincode=findViewById(R.id.pincode);
        btn_save=findViewById(R.id.btn_save);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Vendors");
        idReference = FirebaseDatabase.getInstance().getReference("VendorIDs");

        String[] locations={"select location","GhatKopar","Dadar","Matunga","Thane"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locspin.setAdapter(adapter);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedLocation = locspin.getSelectedItem().toString();
                String address = addr.getText().toString().trim();
                String pinCode = pincode.getText().toString();
                String phoneNumber=vendorPh.getText().toString();
                String stallName = stallname.getText().toString().trim();

                if (phoneNumber.isEmpty()) {
                    vendorPh.setError("Phone number is required");
                    vendorPh.requestFocus();
                    return;
                } else if (!phoneNumber.matches("\\d{10}")) {
                    vendorPh.setError("Phone number must be a 10-digit number");
                    vendorPh.requestFocus();
                    return;
                }
                if (selectedLocation.equals("Select Location")) {
                    Toast.makeText(Vendor_Profile.this, "Please select a location", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (stallName.isEmpty()) {
                    stallname.setError("Stall name is required");
                    stallname.requestFocus();
                    return;
                }
                // Validate Address Details
                if (address.isEmpty()) {
                    Toast.makeText(Vendor_Profile.this, "Please enter address", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate Pincode (Ensure it is a 6-digit number)
                if (pinCode.isEmpty()) {
                    pincode.setError("Pincode is required");
                    pincode.requestFocus();
                    return;
                } else if (!pinCode.matches("\\d{6}")) {
                    pincode.setError("Pincode must be a 6-digit number");
                    pincode.requestFocus();
                    return;
                }
                // Generate unique ID for the vendor
                idReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        long vendorId = snapshot.exists() ? snapshot.getValue(Long.class) : 1000; // Start from 1000
                        idReference.setValue(vendorId + 1); // Increment ID for the next vendor

                        Vendors vendor = new Vendors(String.valueOf(vendorId), phoneNumber, stallName, selectedLocation, address, pinCode);

                        databaseReference.child(String.valueOf(vendorId)).setValue(vendor).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(Vendor_Profile.this, "Profile saved successfully", Toast.LENGTH_SHORT).show();

                                // Pass vendorId to VendorHome
                                Intent intent = new Intent(Vendor_Profile.this, VendorHome.class);
                                intent.putExtra("vendorId", String.valueOf(vendorId));
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Vendor_Profile.this, "Failed to save profile. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(Vendor_Profile.this, "Error generating Vendor ID. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public static class Vendors {
        public String vendorId;
        public String phoneNumber;
        public String stallName;
        public String selectedLocation;
        public String address;
        public String pinCode;

        public Vendors() {
            // Default constructor required for Firebase
        }

        public Vendors(String vendorId, String phoneNumber, String stallName, String selectedLocation, String address, String pinCode) {
            this.vendorId = vendorId;
            this.phoneNumber = phoneNumber;
            this.stallName = stallName;
            this.selectedLocation = selectedLocation;
            this.address = address;
            this.pinCode = pinCode;
        }
    }
}



