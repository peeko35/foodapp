package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    List<searchmainModel> dataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView=view.findViewById(R.id.searchfoodstall);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Vendors");

        fetchVendorData();
        return view;

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

                    // Fetch each food item details
                    for (DataSnapshot foodSnapshot : stallDetailsSnapshot.getChildren()) {
                        String location=foodSnapshot.child("location").getValue(String.class);
                        String foodName = foodSnapshot.child("foodName").getValue(String.class);
                        String imageUrl = foodSnapshot.child("imageUrl").getValue(String.class);


                        // Add data to the list
                        searchmainModel model = new searchmainModel(stallName, foodName, imageUrl,location);
                        dataList.add(model);
                    }
                }

                // Update the adapter with new data
                searchAdapter Adapter = new searchAdapter(getContext(), dataList); // Fixed context here
                recyclerView.setAdapter(Adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
