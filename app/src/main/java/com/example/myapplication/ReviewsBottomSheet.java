package com.example.myapplication;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewsBottomSheet extends BottomSheetDialogFragment {
    private RecyclerView reviewRecyclerview;
    private ReviewsAdapter adapter;
    private List<Rating> reviewList;
    private DatabaseReference databaseReference;
    private String vendorId;
    public ReviewsBottomSheet(String vendorId) {
        this.vendorId = vendorId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews_bottom_sheet, container, false);
        reviewRecyclerview = view.findViewById(R.id.reviewRecyclerview);
        reviewRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        reviewList = new ArrayList<>();
        adapter = new ReviewsAdapter(getContext(), reviewList);
        databaseReference = FirebaseDatabase.getInstance().getReference("Vendors").child(vendorId).child("Ratings");
        reviewRecyclerview.setAdapter(adapter);

        fetchReviews();

        return view;
    }

    private void fetchReviews() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewList.clear();

                for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                    String name = reviewSnapshot.child("name").getValue(String.class);
                    String comment = reviewSnapshot.child("comment").getValue(String.class);
                    float rating = reviewSnapshot.child("rating").getValue(Float.class);

                    // Create a Rating object
                    Rating ratingObj = new Rating(rating, comment, name);

                    // Add it to the review list
                    reviewList.add(ratingObj);
                }

                // Notify adapter about the changes in the data
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}


