package com.example.tiplearning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tiplearning.model.runtime.HttpResponse;
import com.example.tiplearning.model.runtime.RequestLogin;
import com.example.tiplearning.model.runtime.RequestRegister;
import com.example.tiplearning.model.runtime.TinyUser;
import com.example.tiplearning.retrofit.RetrofitInstance;
import com.example.tiplearning.service.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init(){
        EditText edtEmail = findViewById(R.id.etReEmail);
        EditText edtFullName = findViewById(R.id.etReFullName);
        EditText edtPassword = findViewById(R.id.etRePassword);
        EditText etReRePassword = findViewById(R.id.etReRePassword);
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(view -> {
            String email = edtEmail.getText().toString();
            String fullName = edtFullName.getText().toString();
            String password = edtPassword.getText().toString();
            String rePassword = etReRePassword.getText().toString();
            if(email.isEmpty() || fullName.isEmpty() || password.isEmpty() || rePassword.isEmpty()){
                showToast("Please fill all fields");
            }
            else if(!password.equals(rePassword)){
                showToast("Password and Re-Password are not the same");
            }
            else{
                callApiRegister(email, fullName, password);
            }

        });
        TextView txtLinkToLogin = findViewById(R.id.txtLinkToLogin);
        txtLinkToLogin.setOnClickListener(view -> finish());
    }


    private void callApiRegister(String email, String fullName, String password) {
        Call<HttpResponse> call = RetrofitInstance.getRetrofitInstance().create(AuthService.class).register(new RequestRegister(email, password, fullName, ""));
        call.enqueue(new Callback<HttpResponse>() {
            @Override
            public void onResponse(Call<HttpResponse> call, Response<HttpResponse> response) {
                if (response.isSuccessful()) {
                    HttpResponse data = response.body();
                    if(data.getCode()==200){
                        TinyUser user = data.getUser();
                        intentRegister(user.getUserId(), user.getEmail(), user.getFullName(), user.getPhotoUrl());
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

    private void intentRegister(String userId, String email, String fullName, String urlAvatar){
        saveUserData(userId, email, fullName, urlAvatar);
        Intent intent = new Intent(RegisterActivity.this, MainUserActivity.class);
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