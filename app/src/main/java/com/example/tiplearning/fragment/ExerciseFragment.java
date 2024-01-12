package com.example.tiplearning.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiplearning.R;
import com.example.tiplearning.model.QuizzesAdapter;
import com.example.tiplearning.model.runtime.MarkRecord;
import com.example.tiplearning.model.runtime.QuizResponse;
import com.example.tiplearning.retrofit.RetrofitInstance;
import com.example.tiplearning.service.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseFragment extends Fragment {

    private QuizzesAdapter quizzesAdapter;

    private RecyclerView recyclerQuizzes;
    private View view;
    private String userId;
    private String type = "ALL";

    public ExerciseFragment(String userId) {
        this.userId = userId;
    }

    public static ExerciseFragment newInstance(String userId) {
        ExerciseFragment fragment = new ExerciseFragment(userId);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        this.view = view;
        init();
        return view;
    }

    private void init(){
        RadioGroup radioGroup = view.findViewById(R.id.rdGroupType);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdAll) {
                    type = "ALL";
                } else if (checkedId == R.id.rdEnglist) {
                    type = "ENGLISH";
                } else if (checkedId == R.id.rdIt) {
                    type = "IT";
                } else if (checkedId == R.id.rdDifferent) {
                    type = "DIFFERENT";
                }
            }
        });

        recyclerQuizzes = view.findViewById(R.id.recyclerQuizzes);
        recyclerQuizzes.setLayoutManager(new LinearLayoutManager(getContext()));
        quizzesAdapter = new QuizzesAdapter(getContext(),new ArrayList<>(),view.findViewById(R.id.txtChecks),userId);
        recyclerQuizzes.setAdapter(quizzesAdapter);

        Button btnRandQuizzes = view.findViewById(R.id.btnGetQuizzes);
        btnRandQuizzes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApiGetQuizzes();
            }
        });
        Button btnSubmit = view.findViewById(R.id.btnMark);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quizzesAdapter!=null){
                    if(quizzesAdapter.isDone()){
                        return;
                    }
                    if(!quizzesAdapter.isFull()){
                        showAlert("Vẫn còn đáp án bỏ trống, bạn có chắc muốn kết thúc bài làm?");
                        return;
                    }
                    callApiMark(quizzesAdapter.getMarkRecord());
                }
            }
        });
    }


    private void showAlert(String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Cảnh báo")
                .setMessage(content)
                .setPositiveButton("OK, vẫn gửi!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        callApiMark(quizzesAdapter.getMarkRecord());
                        dialog.dismiss();
                    }
                })
                .show();
    }


    private void addQuizzes(List<QuizResponse> list){
        quizzesAdapter = null;
        TextView txtChecks = view.findViewById(R.id.txtChecks);
        TextView txtScore = view.findViewById(R.id.txtScore);
        txtChecks.setText("0/"+list.size());
        txtScore.setText("Điểm: 0/100");
        quizzesAdapter = new QuizzesAdapter(getContext(),list,view.findViewById(R.id.txtChecks),userId);
        recyclerQuizzes.setAdapter(quizzesAdapter);
    }

    private void callApiGetQuizzes() {
        Call<List<QuizResponse>> call = RetrofitInstance.getRetrofitInstance().create(UserService.class).getQuizzes(type,userId);
        call.enqueue(new Callback<List<QuizResponse>>() {
            @Override
            public void onResponse(Call<List<QuizResponse>> call, Response<List<QuizResponse>> response) {
                if (response.isSuccessful()) {
                    List<QuizResponse> data = response.body();
                    addQuizzes(data);
                } else {
                    Toast.makeText(getContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<QuizResponse>> call, Throwable t) {
                Log.e("Network Request", "Error", t);
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

}