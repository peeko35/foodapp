package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileFragment extends Fragment {
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        auth = FirebaseAuth.getInstance();
        button = view.findViewById(R.id.logout);
        textView = view.findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        if (user == null) {
            // Redirect to UserLogin activity
            Intent intent = new Intent(requireActivity(), UserLogin.class);
            startActivity(intent);
            requireActivity().finish();
        } else {
            // Display user's email
            textView.setText(user.getEmail());
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