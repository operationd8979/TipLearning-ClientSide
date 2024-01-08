package com.example.tiplearning.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tiplearning.LoginActivity;
import com.example.tiplearning.MainUserActivity;
import com.example.tiplearning.R;
import com.example.tiplearning.model.entity.User;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class SettingFragment extends Fragment {

    public SettingFragment() {
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        Button btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        Button btnLogout = view.findViewById(R.id.btnLogout);
        ImageView imgAvatar = view.findViewById(R.id.imgAvatarUser);
        TextView txtFullName = view.findViewById(R.id.txtFullNameUser);
        TextView txtEmail = view.findViewById(R.id.txtEmailUser);
        EditText edChangeFullName = view.findViewById(R.id.edChangeFullName);
        EditText edNewPassword = view.findViewById(R.id.edNewPassword);
        EditText edReNewPassword = view.findViewById(R.id.edReNewPassword);
        EditText edOldPassword = view.findViewById(R.id.edOldPassword);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainUserActivity activity = (MainUserActivity) getActivity();
                activity.logout();
            }
        });
        edChangeFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String fullName = charSequence.toString().trim();
                if(!edNewPassword.getText().toString().isEmpty()||!fullName.isEmpty()){
                    btnUpdateProfile.setEnabled(true);
                    edOldPassword.setVisibility(View.VISIBLE);
                }
                else{
                    btnUpdateProfile.setEnabled(false);
                    edOldPassword.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String newPassword = charSequence.toString().trim();
                edReNewPassword.setVisibility(!newPassword.isEmpty()?View.VISIBLE:View.GONE);
                if(!edChangeFullName.getText().toString().isEmpty()||!newPassword.isEmpty()){
                    btnUpdateProfile.setEnabled(true);
                    edOldPassword.setVisibility(View.VISIBLE);
                }
                else{
                    btnUpdateProfile.setEnabled(false);
                    edOldPassword.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        Glide.with(this)
                .load(getUserData().getPhotoUrl())
                .centerCrop()
                .into(imgAvatar);
        txtFullName.setText(getUserData().getName());
        txtEmail.setText(getUserData().getEmail());
        return view;
    }

    private User getUserData(){
        return ((MainUserActivity) getActivity()).getUserData();
    }
}