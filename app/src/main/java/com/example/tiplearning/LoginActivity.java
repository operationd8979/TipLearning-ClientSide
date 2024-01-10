package com.example.tiplearning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiplearning.model.runtime.HttpResponse;
import com.example.tiplearning.model.runtime.RequestAuthGoogle;
import com.example.tiplearning.model.runtime.RequestLogin;
import com.example.tiplearning.model.runtime.TinyUser;
import com.example.tiplearning.retrofit.RetrofitInstance;
import com.example.tiplearning.service.AuthService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        SharedPreferences sharedPreferences = getSharedPreferences("userdata", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);
        String email = sharedPreferences.getString("email", null);
        String fullName = sharedPreferences.getString("fullName", null);
        String urlAvatar = sharedPreferences.getString("urlAvatar", null);

        if(userId!=null && email!=null && fullName!=null){
            Intent intent = new Intent(LoginActivity.this, MainUserActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("email", email);
            intent.putExtra("fullName", fullName);
            intent.putExtra("urlAvatar", urlAvatar);
            startActivity(intent);
            finish();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient  = GoogleSignIn.getClient(this, gso);

        EditText edEmailLogin = findViewById(R.id.etEmail);
        EditText etPasswordLogin = findViewById(R.id.etPassword);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmailLogin.getText().toString();
                String password = etPasswordLogin.getText().toString();
                if(email.isEmpty() || password.isEmpty()){
                    showToast("Please enter email and password!");
                    return;
                }
                callApiLogin(email, password);
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
                String fullName = account.getDisplayName();
                String urlAvatar = String.valueOf(account.getPhotoUrl());
                RequestAuthGoogle requestAuthGoogle = new RequestAuthGoogle(email, fullName, urlAvatar);
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callApiAuthGoogle(requestAuthGoogle);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
            showToast("Login failed!");
        }
    }

    private void callApiAuthGoogle(RequestAuthGoogle requestAuthGoogle) {
        Call<HttpResponse> call = RetrofitInstance.getRetrofitInstance().create(AuthService.class).authGoogle(requestAuthGoogle);
        call.enqueue(new Callback<HttpResponse>() {
            @Override
            public void onResponse(Call<HttpResponse> call, Response<HttpResponse> response) {
                if (response.isSuccessful()) {
                    HttpResponse data = response.body();
                    if(data.getCode()==200){
                        TinyUser user = data.getUser();
                        intentLogin(user.getUserId(), user.getEmail(), user.getFullName(), user.getPhotoUrl());
                    }
                    else{
                        showToast(data.getErrorMessage());
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<HttpResponse> call, Throwable t) {
                Log.e("Network Request", "Error", t);
            }
        });
    }

    private void callApiLogin(String email, String password) {
        Call<HttpResponse> call = RetrofitInstance.getRetrofitInstance().create(AuthService.class).login(new RequestLogin(email, password));
        call.enqueue(new Callback<HttpResponse>() {
            @Override
            public void onResponse(Call<HttpResponse> call, Response<HttpResponse> response) {
                if (response.isSuccessful()) {
                    HttpResponse data = response.body();
                    if(data.getCode()==200){
                        TinyUser user = data.getUser();
                        intentLogin(user.getUserId(), user.getEmail(), user.getFullName(), user.getPhotoUrl());
                    }
                    else{
                        showToast(data.getErrorMessage());
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<HttpResponse> call, Throwable t) {
                Log.e("Network Request", "Error", t);
            }
        });
    }

    private void intentLogin(String userId, String email, String fullName, String urlAvatar){
        saveUserData(userId, email, fullName, urlAvatar);
        Intent intent = new Intent(LoginActivity.this, MainUserActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("email", email);
        intent.putExtra("fullName", fullName);
        intent.putExtra("urlAvatar", urlAvatar);
        startActivity(intent);
        finish();

    }

    private void saveUserData(String userId, String email, String fullName, String urlAvatar){
        SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_PRIVATE).edit();
        editor.putString("userId", userId);
        editor.putString("email", email);
        editor.putString("fullName", fullName);
        editor.putString("urlAvatar", urlAvatar);
        editor.apply();
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}