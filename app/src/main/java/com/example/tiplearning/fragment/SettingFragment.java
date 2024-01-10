package com.example.tiplearning.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tiplearning.LoginActivity;
import com.example.tiplearning.MainUserActivity;
import com.example.tiplearning.R;
import com.example.tiplearning.model.entity.User;
import com.example.tiplearning.model.runtime.HttpResponse;
import com.example.tiplearning.model.runtime.RequestLogin;
import com.example.tiplearning.model.runtime.TinyUser;
import com.example.tiplearning.model.runtime.UpdateInfoRequest;
import com.example.tiplearning.retrofit.RetrofitInstance;
import com.example.tiplearning.service.AuthService;
import com.example.tiplearning.service.UserService;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingFragment extends Fragment {

    private String userId;
    private EditText edChangeFullName;
    private EditText edNewPassword;
    private EditText edReNewPassword;
    private EditText edOldPassword;
    private TextView txtFullName;

    public SettingFragment(String userId) {
        this.userId = userId;
    }

    public static SettingFragment newInstance(String userId) {
        SettingFragment fragment = new SettingFragment(userId);
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
        txtFullName = view.findViewById(R.id.txtFullNameUser);
        TextView txtEmail = view.findViewById(R.id.txtEmailUser);
        edChangeFullName = view.findViewById(R.id.edChangeFullName);
        edNewPassword = view.findViewById(R.id.edNewPassword);
        edReNewPassword = view.findViewById(R.id.edReNewPassword);
        edOldPassword = view.findViewById(R.id.edOldPassword);
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
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = edChangeFullName.getText().toString().trim();
                String newPassword = edNewPassword.getText().toString().trim();
                String reNewPassword = edReNewPassword.getText().toString().trim();
                String oldPassword = edOldPassword.getText().toString().trim();
                if(!newPassword.isEmpty() && !reNewPassword.isEmpty() && !newPassword.equals(reNewPassword)){
                    showToast("New password not match!");
                    return;
                }
                if(oldPassword.isEmpty()){
                    showToast("Please enter old password!");
                    return;
                }
                UpdateInfoRequest updateInfoRequest = new UpdateInfoRequest(fullName,oldPassword,newPassword);
                callApiUpdateInfo(updateInfoRequest);
            }
        });
        Glide.with(this)
                .load(getUserData().getUrlAvatar())
                .centerCrop()
                .into(imgAvatar);
        txtFullName.setText(getUserData().getFullName());
        txtEmail.setText(getUserData().getEmail());
        return view;
    }


    private void callApiUpdateInfo(UpdateInfoRequest updateInfoRequest) {
        Call<HttpResponse> call = RetrofitInstance.getRetrofitInstance().create(UserService.class).updateInfo(updateInfoRequest, userId);
        call.enqueue(new Callback<HttpResponse>() {
            @Override
            public void onResponse(Call<HttpResponse> call, Response<HttpResponse> response) {
                if (response.isSuccessful()) {
                    HttpResponse data = response.body();
                    if(data.getCode()==200){
                        showToast("Update profile successfully!");
                        TinyUser user = data.getUser();
                        txtFullName.setText(user.getFullName());
                        getUserData().setFullName(user.getFullName());
                        edChangeFullName.setText("");
                        edNewPassword.setText("");
                        edReNewPassword.setText("");
                        edOldPassword.setText("");
                    }
                    else{
                        showToast(data.getErrorMessage());
                    }
                } else {
                    Toast.makeText(getContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<HttpResponse> call, Throwable t) {
                Log.e("Network Request", "Error", t);
            }
        });
    }

    private User getUserData(){
        return ((MainUserActivity) getActivity()).getUserData();
    }

    private void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}