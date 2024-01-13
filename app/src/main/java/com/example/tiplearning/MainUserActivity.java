package com.example.tiplearning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tiplearning.model.FragmentAdapter;
import com.example.tiplearning.model.entity.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainUserActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private User user;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient  = GoogleSignIn.getClient(this, gso);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String email = intent.getStringExtra("email");
        String fullName = intent.getStringExtra("fullName");
        String urlAvatar = intent.getStringExtra("urlAvatar");
        user = new User(userId, email, fullName, urlAvatar);

        init();
    }

    private void init(){
        initViewPager();
    }

    private void initViewPager(){
        viewPager = findViewById(R.id.viewPagerMainUser);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(this,user.getUserId());
        viewPager.setAdapter(fragmentAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(item.getItemId()==R.id.navigation_home){
                viewPager.setCurrentItem(0, true);
                return true;
            } else if(item.getItemId()==R.id.navigation_exercise){
                viewPager.setCurrentItem(1, true);
                return true;
            } else if(item.getItemId()==R.id.navigation_quiz){
                viewPager.setCurrentItem(2, true);
                return true;
            } else if(item.getItemId()==R.id.navigation_result){
                viewPager.setCurrentItem(3, true);
                return true;
            } else if(item.getItemId()==R.id.navigation_setting){
                viewPager.setCurrentItem(4, true);
                return true;
            }
            return false;
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void logout(){
        SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(MainUserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public User getUserData() {
        return this.user;
    }

}
