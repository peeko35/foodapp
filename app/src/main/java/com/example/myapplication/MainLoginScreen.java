package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainLoginScreen extends AppCompatActivity {
    private Button User;
    private Button vendor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login_screen);
        User=findViewById(R.id.user);
        User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainLoginScreen.this,UserLogin.class);
                startActivity(intent);
            }
        });
        vendor=findViewById(R.id.vendor);
        vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainLoginScreen.this,VendorLogin.class);
                startActivity(intent);
            }
        });

    }
}


