package com.example.tiplearning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init(){

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        String name = sharedPreferences.getString("name", null);
        String photoUrl = sharedPreferences.getString("photoUrl", null);

        if(email!=null && name!=null && photoUrl!=null){
            Intent intent = new Intent(LoginActivity.this, MainUserActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("name", name);
            intent.putExtra("photoUrl", photoUrl);
            startActivity(intent);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient  = GoogleSignIn.getClient(this, gso);


        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Login clicked!");
            }
        });
        Button btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent , RC_SIGN_IN);
            }
        });
        TextView txtLinkToRegister = findViewById(R.id.txtLinkToRegister);
        txtLinkToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task){
        try{
            GoogleSignInAccount account = task.getResult();
            if(account!=null){
                String email = account.getEmail();
                String name = account.getDisplayName();
                String photoUrl = String.valueOf(account.getPhotoUrl());

                SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                editor.putString("email", email);
                editor.putString("name", name);
                editor.putString("photoUrl", photoUrl);
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, MainUserActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("photoUrl", photoUrl);
                startActivity(intent);
            }
        }catch (Exception e){
            e.printStackTrace();
            showToast("Login failed!");
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}