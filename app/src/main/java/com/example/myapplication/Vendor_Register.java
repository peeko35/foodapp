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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Vendor_Register extends AppCompatActivity {
    EditText pwwd,Vemail;
    Button btn_reg;
    TextView text1;
    ProgressBar Pbar;
    FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),VendorHome.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_register);
        mAuth= FirebaseAuth.getInstance();


        pwwd=findViewById(R.id.pwwd);
        Vemail=findViewById(R.id.venemail);
        btn_reg=findViewById(R.id.btn_reg);
        text1=findViewById(R.id.text1);
        Pbar=findViewById(R.id.bbar);

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),VendorLogin.class);
                startActivity(intent);
                finish();
            }
        });
        
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pbar.setVisibility(View.VISIBLE);
                String email,passwd,vname;
                email=String.valueOf(Vemail.getText());
                passwd=String.valueOf(pwwd.getText());


                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Vendor_Register.this,"Enter email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(passwd)){
                    Toast.makeText(Vendor_Register.this,"Enter password",Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,passwd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Pbar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(Vendor_Register.this, "Account Created",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(getApplicationContext(),Vendor_Profile.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(Vendor_Register.this, "Authentication failed.",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });


            }
        });

    }
}