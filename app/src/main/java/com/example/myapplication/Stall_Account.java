package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Stall_Account extends AppCompatActivity {
    private static final String TAG = "StallAccount";
    private static int IMAGE_REQ=1;
    private Uri imagePath;
    private String imageUrl = null;
    ImageView imgBack,selectimg;
    EditText editviewname,descption,price;
    Button uplo,save;
    DatabaseReference vendorDbRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String vendorId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stall_account);

        imgBack = findViewById(R.id.imgback);
        selectimg = findViewById(R.id.selectimg);
        uplo=findViewById(R.id.uplo);
        editviewname=findViewById(R.id.editviewname);
        descption=findViewById(R.id.descption);
        price=findViewById(R.id.price);
        save=findViewById(R.id.save);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, MainLoginScreen.class));
            finish();
            return;
        }
        vendorId = currentUser.getUid();

        initConfig();

        vendorDbRef = FirebaseDatabase.getInstance().getReference("Vendors");

        selectimg.setOnClickListener(view -> requestPermissions());

        uplo.setOnClickListener(view -> {
            if (imagePath != null) {
                MediaManager.get().upload(imagePath).callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Log.d(TAG, "onStart: Upload started");
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        Log.d(TAG, "onProgress: Uploading...");
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        Log.d(TAG, "onSuccess: Upload successful");
                        imageUrl = (String) resultData.get("url");
                        Log.d(TAG, "Image URL: " + imageUrl);
                        // Update the ImageView with the uploaded image URL
                        Picasso.get().load(imageUrl).into(selectimg);

                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Log.e(TAG, "onError: Upload failed - " + error.getDescription());
                        Toast.makeText(Stall_Account.this, "Image upload failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        Log.e(TAG, "onReschedule: Upload rescheduled");
                    }
                }).dispatch();
            } else {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
            }
        });
        save.setOnClickListener(view -> {
            if (imageUrl == null) {
                Toast.makeText(this, "Please upload an image first", Toast.LENGTH_SHORT).show();
                return;
            }
            savefoodDetails();
        });



        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),VendorHome.class);
                startActivity(intent);
                finish();
            }
        });

    }



    private void initConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dytcsiqv5");
        config.put("api_key", "798546793531394");
        config.put("api_secret", "ZpfhSbCjyOG1mK-krYrya-UJgC0");
        //config.put("secure", true);
        MediaManager.init(this, config);
    }
    private void savefoodDetails() {
        String FoodName = editviewname.getText().toString().trim();
        String description = descption.getText().toString().trim();
        String vprice = price.getText().toString().trim();

        if (FoodName.isEmpty() || description.isEmpty() || vprice.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }



        // Create a map for the stall details
        Map<String, Object> stallDetails = new HashMap<>();
        stallDetails.put("FoodName", FoodName);
        stallDetails.put("description", description);
        stallDetails.put("price", vprice);
        stallDetails.put("imageUrl", imageUrl);


        String stallId =  vendorDbRef.child(vendorId).child("stallDetails").push().getKey();

        if (stallId != null) {
            vendorDbRef.child(vendorId).child("stallDetails").child(stallId).setValue(stallDetails)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(Stall_Account.this, "Stall details saved successfully!", Toast.LENGTH_SHORT).show();
                        // Clear input fields after saving
                        editviewname.setText("");
                        descption.setText("");
                        price.setText("");
                        selectimg.setImageResource(R.drawable.addimg); // Replace with your placeholder image
                        imageUrl = null; // Reset image URL
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "saveFoodDetails: Failed to save stall details", e);
                        Toast.makeText(Stall_Account.this, "Failed to save stall details", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Error generating unique stall ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES}, IMAGE_REQ);
            } else {
                selectImage();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, IMAGE_REQ);
            } else {
                selectImage();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMAGE_REQ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Log.e(TAG, "Permission denied!");
            }
        }
    }


    private void selectImage() {
        Log.d(TAG, "Selecting image...");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        imagePath = data.getData();
                        Log.d(TAG, "Image selected: " + imagePath.toString());
                        Picasso.get().load(imagePath).into(selectimg); // Load the selected image into the ImageView
                    } else {
                        Log.e(TAG, "Image selection failed: Data is null");
                    }
                } else {
                    Log.e(TAG, "Result not OK");
                }
            }
    );

}