package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VeditProfile extends AppCompatActivity {
    EditText stallnme,Vadd,Vloc,phne;
    Button Vsave;
    FirebaseAuth auth;
    DatabaseReference dbref;
    String vendorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedit_profile);
        auth =FirebaseAuth.getInstance();
        FirebaseUser vendor = auth.getCurrentUser();

        if(vendor!=null){
            vendorId = vendor.getUid();
            dbref = FirebaseDatabase.getInstance().getReference("Vendors").child(vendorId);
        }else {
            Toast.makeText(this, "Vendor not logged in!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if no user is logged in
        }
        stallnme=findViewById(R.id.stallnme);
        Vadd=findViewById(R.id.Vadd);
        Vloc=findViewById(R.id.Vloc);
        phne=findViewById(R.id.phne);
        Vsave=findViewById(R.id.VSave);

        Vsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateVendorProfile();
            }
        });

    }

    private void updateVendorProfile() {
        String newname=stallnme.getText().toString().trim();
        String newAdd=Vadd.getText().toString().trim();
        String newLoc=Vloc.getText().toString().trim();
        String newphne=phne.getText().toString().trim();


        dbref.child("stallName").setValue(newname);
        dbref.child("address").setValue(newAdd);
        dbref.child("location").setValue(newLoc);
        dbref.child("phone").setValue(newphne)
         .addOnSuccessListener(aVoid -> Toast.makeText(VeditProfile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(VeditProfile.this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());


    }
}