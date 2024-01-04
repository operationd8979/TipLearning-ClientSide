package com.example.tiplearning;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init(){
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(view -> showToast("Register clicked!"));
        TextView txtLinkToLogin = findViewById(R.id.txtLinkToLogin);
        txtLinkToLogin.setOnClickListener(view -> finish());
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}