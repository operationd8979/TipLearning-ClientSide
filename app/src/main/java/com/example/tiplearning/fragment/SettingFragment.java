package com.example.tiplearning.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tiplearning.MainUserActivity;
import com.example.tiplearning.R;
import com.example.tiplearning.model.entity.User;


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
        ImageView imgAvatar = view.findViewById(R.id.imgAvatarUser);
        TextView txtFullName = view.findViewById(R.id.txtFullNameUser);
        TextView txtEmail = view.findViewById(R.id.txtEmailUser);
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