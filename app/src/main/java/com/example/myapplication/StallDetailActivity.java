package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class StallDetailActivity extends AppCompatActivity {

    TextView desctext, foodprice;
    ImageView detailimg, ratingstar;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stall_detail);

        desctext = findViewById(R.id.descptext);
        foodprice = findViewById(R.id.foodprice);
        detailimg = findViewById(R.id.detailimg);
        ratingstar = findViewById(R.id.ratingstar);
        databaseReference = FirebaseDatabase.getInstance().getReference("Ratings");

        ratingstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogRating();
            }
        });

        String imageUrl = getIntent().getStringExtra("imageUrl");
        String description = getIntent().getStringExtra("description");
        String price = getIntent().getStringExtra("price");


        Log.d("StallDetailActivity", "Image URL: " + imageUrl);
        Log.d("StallDetailActivity", "Description: " + description);
        Log.d("StallDetailActivity", "Price: " + price);


        if (imageUrl != null) {
            String secureImageUrl = imageUrl.replace("http://", "https://");
            Glide.with(this).load(secureImageUrl).into(detailimg);
        } else {
            Log.e("StallDetailActivity", "Image URL is null");
        }

        if (description != null) {
            Log.d("StallDetailActivity", "Setting Description: " + description);
            desctext.setText("Description:" + description);
        } else {
            desctext.setText("Description not available");
            Log.e("StallDetailActivity", "Description is null");
        }

        if (price != null) {
            Log.d("StallDetailActivity", "Setting Price: " + price);
            foodprice.setText("Price: ₹" + price);
        } else {
            foodprice.setText("Price not available");
            Log.e("StallDetailActivity", "Price is null");
        }

    }


    private void showDialogRating() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rating Food");
        builder.setMessage("comments");

        View itemView = LayoutInflater.from(this).inflate(R.layout.layout_rating, null);

        RatingBar ratingBar = (RatingBar) itemView.findViewById(R.id.ratebr);
        EditText editText = (EditText) itemView.findViewById(R.id.commentEditText);

        builder.setView(itemView);

        builder.setNegativeButton("Cancle", ((dialogInterface, i) -> {
            dialogInterface.dismiss();

        }));
        builder.setPositiveButton("OK", ((dialogInterface, i) -> {
            float ratingValue = ratingBar.getRating();
            String comment = editText.getText().toString().trim();
            saveRatingToFirebase(ratingValue, comment);
        }));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveRatingToFirebase(float ratingValue, String comment) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "You need to log in to submit a rating.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid(); // Get logged-in user ID
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String customUserId = task.getResult().child("id").getValue(String.class); // Get stored custom ID

                if (customUserId == null) {
                    customUserId = userId;
                }

                String ratingId = databaseReference.push().getKey();
                Map<String, Object> ratingData = new HashMap<>();
                ratingData.put("userId", customUserId); // Always store a valid user ID
                ratingData.put("rating", ratingValue);
                ratingData.put("comment", comment);
                ratingData.put("timestamp", System.currentTimeMillis());

                if (ratingId != null) {
                    databaseReference.child(ratingId).setValue(ratingData)
                            .addOnSuccessListener(aVoid ->
                                    Toast.makeText(this, "Rating Submitted!", Toast.LENGTH_SHORT).show()
                            )
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Failed to submit rating.", Toast.LENGTH_SHORT).show()
                            );
                }
            } else {
                Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

