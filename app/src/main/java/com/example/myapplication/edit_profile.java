package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class edit_profile extends AppCompatActivity {
    private EditText editName, editEmail, editPassword;
    private Button saveButton;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        // Initialize Firebase Auth

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            userId = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);// Get the unique Firebase user ID
        }else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if no user is logged in
        }



        // Initialize UI Components
        editName = findViewById(R.id.profame);
        editEmail = findViewById(R.id.profEmail);
        editPassword = findViewById(R.id.profPassword);
        saveButton = findViewById(R.id.savve);

        // Save Button Click Listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });
    }

    private void updateUserProfile() {
        String newName = editName.getText().toString().trim();
        String newEmail = editEmail.getText().toString().trim();
        String newPassword = editPassword.getText().toString().trim();

        if (TextUtils.isEmpty(newName) || TextUtils.isEmpty(newEmail) || TextUtils.isEmpty(newPassword)) {
            Toast.makeText(edit_profile.this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update in Realtime Database
        databaseReference.child("name").setValue(newName);
        databaseReference.child("email").setValue(newEmail);
        databaseReference.child("password").setValue(newPassword);

        // Update Email & Password in Firebase Authentication
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            // Update email
            user.updateEmail(newEmail).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(edit_profile.this, "Email updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(edit_profile.this, "Failed to update email!", Toast.LENGTH_SHORT).show();
                }
            });

            // Update password
            user.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(edit_profile.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(edit_profile.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        Toast.makeText(edit_profile.this, "Profile Updated!", Toast.LENGTH_SHORT).show();

    }
}