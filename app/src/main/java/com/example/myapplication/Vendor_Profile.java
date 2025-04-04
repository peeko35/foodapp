package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Vendor_Profile extends AppCompatActivity {


    private EditText vendorPhone, stallName, stallAddress, pincode;
    private Spinner locationSpinner;
    private Button saveButton;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_profile);
        vendorPhone = findViewById(R.id.VendorPhone);
        stallName = findViewById(R.id.Stallname);
        stallAddress = findViewById(R.id.addr);
        pincode = findViewById(R.id.pincode);
        locationSpinner = findViewById(R.id.locspin);
        saveButton = findViewById(R.id.btn_save);




        databaseReference = FirebaseDatabase.getInstance().getReference("Vendors");

        String[] locations = {"select location", "GhatKopar", "Dadar", "Matunga", "Thane","Ambivli"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVendorData();
            }
        });
    }

    private void saveVendorData() {
        String phone = vendorPhone.getText().toString().trim();
        String stall = stallName.getText().toString().trim();
        String address = stallAddress.getText().toString().trim();
        String pin = pincode.getText().toString().trim();
        String location = locationSpinner.getSelectedItem().toString();

        if (phone.isEmpty() || stall.isEmpty() || address.isEmpty() || pin.isEmpty() || location.equals("Select location")) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidAddress(address)) {
            Toast.makeText(this, "Invalid address format! Use 'Shop No, Street Name, Landmark' or 'Street Name, Landmark'", Toast.LENGTH_LONG).show();
            return;
        }

        Map<String, Object> vendorData = new HashMap<>();
        vendorData.put("phone", phone);
        vendorData.put("stallName", stall);
        vendorData.put("address", address);
        vendorData.put("pincode", pin);
        vendorData.put("location", location);

        String vendorId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child(vendorId).setValue(vendorData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Vendor data saved!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, VendorHome.class));
                finish();
            } else {
                Toast.makeText(this, "Failed to save vendor data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidAddress(String address) {
        String regex = "^(.*?),\\s*(Mumbai)?\\s*-?\\s*(\\d{6})?(?:\\s*\\((.*?)\\))?$";
        return address.matches(regex);
    }
}




