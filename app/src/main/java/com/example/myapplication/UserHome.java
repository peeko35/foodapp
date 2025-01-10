package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserHome extends AppCompatActivity {
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        auth=FirebaseAuth.getInstance();
        button=findViewById(R.id.logout);
        textView=findViewById(R.id.user_details);
        user= auth.getCurrentUser();
        bottomNavigationView=findViewById(R.id.bottomNavView);
        frameLayout=findViewById(R.id.framelayout);

        if(user==null){
            Intent intent = new Intent(getApplicationContext(),UserLogin.class);
            startActivity(intent);
            finish();
        }
        else{
            textView.setText(user.getEmail());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),UserLogin.class);
                startActivity(intent);
                finish();

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId=item.getItemId();
                if(itemId==R.id.navHome){
                    loadFragment(new HomeFragment(),false);


                }else if(itemId==R.id.navExplore){
                    loadFragment(new SearchFragment(),false);

                }else if(itemId==R.id.navafav){
                    loadFragment(new FavFragment(),false);

                }else {// user profile
                    loadFragment(new UserProfileFragment(),false);

                }

                return true;
            }
        });
        loadFragment(new HomeFragment(),true);

    }
    private void loadFragment(Fragment fragment,boolean isAppInitialized){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        if(isAppInitialized){
            fragmentTransaction.add(R.id.framelayout,fragment);
        }
        else{
            fragmentTransaction.replace(R.id.framelayout,fragment);

        }

        fragmentTransaction.commit();
    }
}