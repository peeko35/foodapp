package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileFragment extends Fragment {
    FirebaseAuth auth;
    Button button,editProfile;
    TextView textView;
    DatabaseReference databaseReference;
    TextView profileNam,profilepaswd;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        auth = FirebaseAuth.getInstance();
        button = view.findViewById(R.id.logout);
        textView = view.findViewById(R.id.profileEmail);
        profileNam=view.findViewById(R.id.profileName);
        profilepaswd=view.findViewById(R.id.profilePassword);
        editProfile=view.findViewById(R.id.editProfile);
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        if (user == null) {
            // Redirect to UserLogin activity
            Intent intent = new Intent(requireActivity(), UserLogin.class);
            startActivity(intent);
            requireActivity().finish();
        } else {
            String userId = user.getUid();
            Log.d("FirebaseUserID", "User UID: " + userId);
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String password=snapshot.child("password").getValue(String.class);

                        Log.d("FirebaseData", "Name: " + name + ", Email: " + email); // Debugging Log

                        profileNam.setText(name != null ? name : "No Name Found");
                        textView.setText(email != null ? email : "No Email Found");
                        profilepaswd.setText(password!=null ? password: "No password Found");
                    } else {
                        Log.d("FirebaseData", "Snapshot does not exist for user ID: " + userId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseError", "Failed to read user data", error.toException());
                }
            });
        }

        // Set logout button listener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out user and redirect to UserLogin activity
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(requireActivity(), UserLogin.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        return view;
    }
}