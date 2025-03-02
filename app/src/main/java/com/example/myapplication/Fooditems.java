package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Fooditems extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FooditemAdapter foodAdapter;
    private List<catModel> foodlist;
    private DatabaseReference databaseReference;
    private String currentVendorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fooditems);
        recyclerView = findViewById(R.id.recycleItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        foodlist = new ArrayList<>();
        foodAdapter = new FooditemAdapter(foodlist);
        recyclerView.setAdapter(foodAdapter);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            currentVendorId = auth.getCurrentUser().getUid();
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference("Vendors")
                    .child(currentVendorId)
                    .child("stallDetails");

            fetchFoodItems();
        } else {
            Log.e("FirebaseData", "User not logged in");
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }
    private void fetchFoodItems() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodlist.clear();
                Log.d("FirebaseData", "Total food items found: " + snapshot.getChildrenCount());

                if (!snapshot.exists() || snapshot.getChildrenCount() == 0) {
                    Log.e("FirebaseData", "No data found in database!");
                    Toast.makeText(Fooditems.this, "No food items found!", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (DataSnapshot stallSnapshot : snapshot.getChildren()) {
                    // Get food item fields directly
                    String foodName = stallSnapshot.child("foodName").getValue(String.class);
                    String price = stallSnapshot.child("price").getValue(String.class);
                    String description = stallSnapshot.child("description").getValue(String.class);
                    String imageUrl = stallSnapshot.child("imageUrl").getValue(String.class);

                    // Debugging logs
                    Log.d("FirebaseData", "Food Name: " + foodName);
                    Log.d("FirebaseData", "Price: " + price);
                    Log.d("FirebaseData", "Description: " + description);
                    Log.d("FirebaseData", "Image URL: " + imageUrl);

                    if (foodName != null && price != null && imageUrl != null) {
                        catModel foodItem = new catModel(foodName, imageUrl, price, description);
                        foodlist.add(foodItem);
                    }
                }

                Log.d("FirebaseData", "Total items in list: " + foodlist.size());

                if (!foodlist.isEmpty()) {
                    foodAdapter.notifyDataSetChanged();
                } else {
                    Log.e("FirebaseData", "No food items added to the list!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseData", "Database error: " + error.getMessage());
                Toast.makeText(Fooditems.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}