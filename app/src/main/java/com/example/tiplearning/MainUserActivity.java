package com.example.tiplearning;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tiplearning.model.FragmentAdapter;
import com.example.tiplearning.model.entity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainUserActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String name = intent.getStringExtra("name");
        String photoUrl = intent.getStringExtra("photoUrl");
        user = new User(email, name, photoUrl);



        init();
    }

    private void init(){
        initViewPager();
    }

    private void initViewPager(){
        viewPager = findViewById(R.id.viewPagerMainUser);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(this);
        viewPager.setAdapter(fragmentAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(item.getItemId()==R.id.navigation_exercise){
                viewPager.setCurrentItem(0, true);
                return true;
            } else if(item.getItemId()==R.id.navigation_quiz){
                viewPager.setCurrentItem(1, true);
                return true;
            } else if(item.getItemId()==R.id.navigation_result){
                viewPager.setCurrentItem(2, true);
                return true;
            } else if(item.getItemId()==R.id.navigation_setting){
                viewPager.setCurrentItem(3, true);
                return true;
            }
            return false;
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
