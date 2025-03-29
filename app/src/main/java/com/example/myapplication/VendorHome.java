package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VendorHome extends AppCompatActivity {
    FirebaseAuth auth;
    Button sigout;
    FirebaseUser Vuser;
    CardView cardViewacc;
    CardView cardViewprofile,carditem;
    ImageView imaggeback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_home);
        auth=FirebaseAuth.getInstance();

        cardViewprofile=findViewById(R.id.cardViewprofile);
        cardViewacc=findViewById(R.id.cardViewacc);
        carditem=findViewById(R.id.carditem);
        Vuser=auth.getCurrentUser();
        imaggeback= findViewById(R.id.imggbackk);
        if(Vuser==null){
            Intent intent = new Intent(getApplicationContext(),VendorLogin.class);
            startActivity(intent);
            finish();
        }
        imaggeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainLoginScreen.class);
                startActivity(intent);
                finish();
            }
        });

        cardViewacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Stall_Account.class);
                startActivity(intent);
                finish();

            }
        });
        cardViewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Profile_details.class);
                startActivity(intent);
                finish();

            }
        });
        carditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Fooditems.class);
                startActivity(intent);
                finish();
            }
        });
    }
}