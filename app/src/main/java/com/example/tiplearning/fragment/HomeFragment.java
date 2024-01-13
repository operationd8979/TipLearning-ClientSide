package com.example.tiplearning.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.tiplearning.LoginActivity;
import com.example.tiplearning.MainUserActivity;
import com.example.tiplearning.R;
import com.example.tiplearning.SentenceActivity;
import com.example.tiplearning.model.QuizzesAdapter;
import com.example.tiplearning.model.runtime.MarkRecord;
import com.example.tiplearning.retrofit.RetrofitInstance;
import com.example.tiplearning.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private QuizzesAdapter quizzesAdapter;
    private View view;
    private String userId;
    public HomeFragment(String userId){
        this.userId = userId;
    }
    public static HomeFragment newInstance(String userId) {
        HomeFragment fragment = new HomeFragment(userId);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.view = view;
        init();
        return view;
    }
    private void init(){
        LinearLayout containerLayout = view.findViewById(R.id.btn_sentence);
        containerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Sentence Click");
                Intent intent = new Intent(getActivity(), SentenceActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }
    private void callApiMark(MarkRecord markRecord) {
        Call<MarkRecord> call = RetrofitInstance.getRetrofitInstance().create(UserService.class).mark(markRecord,userId);
        call.enqueue(new Callback<MarkRecord>() {
            @Override
            public void onResponse(Call<MarkRecord> call, Response<MarkRecord> response) {
                if (response.isSuccessful()) {
                    MarkRecord data = response.body();
                    quizzesAdapter.doneMarkRecord(data);
                    TextView txtScore = view.findViewById(R.id.txtScore);
                    txtScore.setText("Điểm: "+data.score+"/100");
                } else {
                    Toast.makeText(getContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<MarkRecord> call, Throwable t) {
                Log.e("Network Request", "Error", t);
            }
        });
    }
    private void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
