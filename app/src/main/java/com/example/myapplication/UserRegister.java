package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UserRegister extends AppCompatActivity {
    EditText editTextEmail,editTextPassword,editTextname;
    Button buttoReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;
    DatabaseReference databaseReference;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(getApplicationContext(),UserHome.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        mAuth=FirebaseAuth.getInstance();
        editTextEmail=findViewById(R.id.email);
        editTextPassword=findViewById(R.id.password);
        editTextname=findViewById(R.id.UserName);
        buttoReg=findViewById(R.id.btn_register);
        progressBar=findViewById(R.id.progressBar);
        textView=findViewById(R.id.loginNow);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),UserLogin.class);
                startActivity(intent);
                finish();
            }
        });

        buttoReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email,password,nam;
                email=String.valueOf(editTextEmail.getText());
                password=String.valueOf(editTextPassword.getText());
                nam=String.valueOf(editTextname.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(UserRegister.this,"Enter the Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(UserRegister.this,"Enter the password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(nam)){
                    Toast.makeText(UserRegister.this,"Enter the Name",Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        String userId = user.getUid();

                                        // Create a map to store user details
                                        Map<String, Object> userDetails = new HashMap<>();
                                        userDetails.put("name", nam);
                                        userDetails.put("email", email);
                                        userDetails.put("id", System.currentTimeMillis()); // Use timestamp as unique ID

                                        // Save user details in the Realtime Database
                                        databaseReference.child(userId).setValue(userDetails)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressBar.setVisibility(View.GONE);
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(UserRegister.this, "Account Created", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(getApplicationContext(), UserLogin.class);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(UserRegister.this, "Failed to save user details", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(UserRegister.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}