package com.example.tiplearning.model;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tiplearning.MainUserActivity;
import com.example.tiplearning.fragment.ExerciseFragment;
import com.example.tiplearning.fragment.QuizFragment;
import com.example.tiplearning.fragment.ResultFragment;
import com.example.tiplearning.fragment.SettingFragment;

public class FragmentAdapter extends FragmentStateAdapter {

    public FragmentAdapter(@NonNull MainUserActivity fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ExerciseFragment();
            case 1:
                return new QuizFragment();
            case 2:
                return new ResultFragment();
            default:
                return new SettingFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
