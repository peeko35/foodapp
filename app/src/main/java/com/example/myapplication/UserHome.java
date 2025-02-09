package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class UserHome extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        bottomNavigationView=findViewById(R.id.bottomNavView);
        frameLayout=findViewById(R.id.framelayout);

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