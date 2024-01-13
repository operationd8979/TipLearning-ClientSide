package com.example.tiplearning.model;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tiplearning.MainUserActivity;
import com.example.tiplearning.fragment.ExerciseFragment;
import com.example.tiplearning.fragment.HomeFragment;
import com.example.tiplearning.fragment.QuizFragment;
import com.example.tiplearning.fragment.ResultFragment;
import com.example.tiplearning.fragment.SettingFragment;

public class FragmentAdapter extends FragmentStateAdapter {

    private String userId;

    public FragmentAdapter(@NonNull MainUserActivity fragment,String userId) {
        super(fragment);
        this.userId = userId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment(userId);
            case 1:
                return new ExerciseFragment(userId);
            case 2:
                return new QuizFragment(userId);
            case 3:
                return new ResultFragment(userId);
            default:
                return new SettingFragment(userId);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
