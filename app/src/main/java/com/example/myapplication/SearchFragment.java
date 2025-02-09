package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
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
    searchAdapter adapter;
    ValueEventListener eventListener;
    SearchView searchvieew;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView=view.findViewById(R.id.searchfoodstall);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        dataList = new ArrayList<>();
        adapter = new searchAdapter(getContext(), dataList);
        recyclerView.setAdapter(adapter);
        searchvieew=view.findViewById(R.id.searchvieew);
        searchvieew.clearFocus();
        databaseReference = FirebaseDatabase.getInstance().getReference("Vendors");
        searchvieew.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (adapter != null) {
                    adapter.getFilter().filter(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        fetchVendorData();
        return view;

    }

    private void fetchVendorData() {
       eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataList.clear();
                for (DataSnapshot vendorSnapshot : dataSnapshot.getChildren()) {
                    // Fetch the stall name
                    String stallName = vendorSnapshot.child("stallName").getValue(String.class);
                    String location =vendorSnapshot.child("location").getValue(String.class);

                    // Fetch the stallDetails for the vendor
                    DataSnapshot stallDetailsSnapshot = vendorSnapshot.child("stallDetails");
                    String vendorId = vendorSnapshot.getKey();

                    DataSnapshot ratingsSnapshot = vendorSnapshot.child("Ratings");
                    float totalRating = 0;
                    int count = 0;
                    for (DataSnapshot rating : ratingsSnapshot.getChildren()) {
                        try {
                            Object ratingValueObj = rating.child("rating").getValue();
                            if (ratingValueObj instanceof Number) {
                                totalRating += ((Number) ratingValueObj).floatValue();
                                count++;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    float averageRating = (count > 0) ? totalRating / count : 0;


                    // Fetch each food item details
                    for (DataSnapshot foodSnapshot : stallDetailsSnapshot.getChildren()) {
                        vendorId=vendorSnapshot.getKey();

                        String foodName = foodSnapshot.child("foodName").getValue(String.class);
                        String imageUrl = foodSnapshot.child("imageUrl").getValue(String.class);
                        String description = foodSnapshot.child("description").getValue(String.class);
                        String price = foodSnapshot.child("price").getValue(String.class);

                        // Add data to the list
                        searchmainModel model = new searchmainModel(imageUrl,stallName, foodName, location,price, description,  vendorId, averageRating);
                        dataList.add(model);

                    }
                }

                if (dataList.isEmpty()) {
                    Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Data Loaded: " + dataList.size(), Toast.LENGTH_SHORT).show();
                }

                // Notify adapter that data has changed


               searchAdapter searchadapter=new searchAdapter(requireContext(),dataList);
                recyclerView.setAdapter(searchadapter);
                searchadapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void onDestroyView() {
        super.onDestroyView();
        if (databaseReference != null && eventListener != null) {
            databaseReference.removeEventListener(eventListener);
        }
    }


}
