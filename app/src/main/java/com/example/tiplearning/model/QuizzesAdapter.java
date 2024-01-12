package com.example.tiplearning.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiplearning.R;
import com.example.tiplearning.model.entity.Record;
import com.example.tiplearning.model.runtime.MarkRecord;
import com.example.tiplearning.model.runtime.QuizResponse;
import com.example.tiplearning.retrofit.RetrofitInstance;
import com.example.tiplearning.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizzesAdapter extends RecyclerView.Adapter<QuizzesAdapter.QuizViewHolder> {
    private List<QuizResponse> quizResponseList;
    private MarkRecord markRecord;
    private Context context;
    private TextView txtChecks;
    private int count = 0;
    private String userId;

    //RecyclerView tái sử dụng ViewHolder nên có thể bị lỗi khi giữ các tham chiếu Button khi lấy trên 3 quizzes//
    //C1 để nút submit bên trong Recycler đảm bảo khi submit đã load toàn bộ View
    //C2 add từng view rồi notify
//    private Map<String,List<Button>> mapButton;
    private Map<String,List<QuizViewHolder>> mapViewHolder;
    private boolean isDone = false;


    public QuizzesAdapter(Context context, List<QuizResponse> quizResponseList, TextView txtChecks, String userId) {
        this.context = context;
        this.txtChecks = txtChecks;
        this.quizResponseList = quizResponseList;
        this.userId = userId;
//        mapButton = new HashMap<>();
        mapViewHolder = new HashMap<>();
        List<Record> recordList = new ArrayList<>();
        for (QuizResponse quizResponse:quizResponseList){
            recordList.add(new Record(quizResponse.quizId,""));
        }
        this.markRecord = new MarkRecord(recordList,0F);
    }

    public void getQuizzes(List<QuizResponse> quizResponseList){
        count = 0;
        this.quizResponseList = quizResponseList;
        List<Record> recordList = new ArrayList<>();
        for (QuizResponse quizResponse:quizResponseList){
            recordList.add(new Record(quizResponse.quizId,""));
        }
        markRecord = new MarkRecord(recordList,0F);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz, parent, false);
        return new QuizViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        QuizResponse quizResponse = quizResponseList.get(position);
//        mapButton.put(quizResponse.quizId, Arrays.asList(holder.btnAnswer1,holder.btnAnswer2,holder.btnAnswer3,holder.btnAnswer4));
        mapViewHolder.put(quizResponse.quizId, Arrays.asList(holder));
        holder.txtTitle.setText(quizResponse.title);
        Button btnAnswer1 = holder.btnAnswer1;
        Button btnAnswer2 = holder.btnAnswer2;
        Button btnAnswer3 = holder.btnAnswer3;
        Button btnAnswer4 = holder.btnAnswer4;
        btnAnswer1.setText(quizResponse.answers.get(0));
        btnAnswer2.setText(quizResponse.answers.get(1));
        btnAnswer3.setText(quizResponse.answers.get(2));
        btnAnswer4.setText(quizResponse.answers.get(3));
//        holder.btnAnswer1.setOnClickListener(v -> handleAnswerClick(quizResponse.quizId, holder.btnAnswer1, holder));
//        holder.btnAnswer2.setOnClickListener(v -> handleAnswerClick(quizResponse.quizId, holder.btnAnswer2, holder));
//        holder.btnAnswer3.setOnClickListener(v -> handleAnswerClick(quizResponse.quizId, holder.btnAnswer3, holder));
//        holder.btnAnswer4.setOnClickListener(v -> handleAnswerClick(quizResponse.quizId, holder.btnAnswer4, holder));
        btnAnswer1.setOnClickListener(v -> handleAnswerClick(quizResponse.quizId, holder.btnAnswer1.getText().toString()));
        btnAnswer2.setOnClickListener(v -> handleAnswerClick(quizResponse.quizId, holder.btnAnswer2.getText().toString()));
        btnAnswer3.setOnClickListener(v -> handleAnswerClick(quizResponse.quizId, holder.btnAnswer3.getText().toString()));
        btnAnswer4.setOnClickListener(v -> handleAnswerClick(quizResponse.quizId, holder.btnAnswer4.getText().toString()));
    }

    private void handleAnswerClick(String quizId, Button clickedButton, QuizViewHolder holder) {
        if(isDone){
            return;
        }
        Record record = markRecord.recordList.stream().filter(r->r.getQuizId().equals(quizId)).findFirst().orElse(null);
        String newAnswer = clickedButton.getText().toString();
        if(record!=null){
            if(record.getAnswer().equals("")){
                count++;
                txtChecks.setText(count+"/"+quizResponseList.size());
            }
            if(record.getAnswer().equals(newAnswer)){
                return;
            }
            else{
                unselectAnswer(holder);
                record.setAnswer(newAnswer);
            }
            updateAnswerButtonsUI(clickedButton);
        }
    }

    private void handleAnswerClick(String quizId, String newAnswer) {
        if(isDone){
            return;
        }
        QuizViewHolder holder = mapViewHolder.get(quizId).get(0);
        Record record = markRecord.recordList.stream().filter(r->r.getQuizId().equals(quizId)).findFirst().orElse(null);
        Button clickedButton = null;
        if(newAnswer.equals(holder.btnAnswer1.getText().toString())){
            clickedButton = holder.btnAnswer1;
        }
        else if(newAnswer.equals(holder.btnAnswer2.getText().toString())){
            clickedButton = holder.btnAnswer2;
        }
        else if(newAnswer.equals(holder.btnAnswer3.getText().toString())){
            clickedButton = holder.btnAnswer3;
        }
        else if(newAnswer.equals(holder.btnAnswer4.getText().toString())){
            clickedButton = holder.btnAnswer4;
        }
        if(record!=null){
            if(record.getAnswer().equals("")){
                count++;
                txtChecks.setText(count+"/"+quizResponseList.size());
            }
            if(record.getAnswer().equals(newAnswer)){
                return;
            }
            else{
                unselectAnswer(holder);
                record.setAnswer(newAnswer);
            }
            updateAnswerButtonsUI(clickedButton);
        }
    }


    private void unselectAnswer(QuizViewHolder holder) {
        holder.btnAnswer1.setBackgroundColor(ContextCompat.getColor(context, R.color.non_checked));
        holder.btnAnswer2.setBackgroundColor(ContextCompat.getColor(context, R.color.non_checked));
        holder.btnAnswer3.setBackgroundColor(ContextCompat.getColor(context, R.color.non_checked));
        holder.btnAnswer4.setBackgroundColor(ContextCompat.getColor(context, R.color.non_checked));
        notifyDataSetChanged();
    }
    private void updateAnswerButtonsUI(Button clickedButton) {
        clickedButton.setBackgroundColor(ContextCompat.getColor(context, R.color.checked));
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return quizResponseList.size();
    }

    public boolean isFull(){
        return count==quizResponseList.size();
    }

    public boolean isDone(){
        return this.isDone;
    }

    public MarkRecord getMarkRecord(){
        return this.markRecord;
    }

    public void doneMarkRecord(MarkRecord endMarkRecord){
        List<Record> recordList = endMarkRecord.recordList;
//        if(mapButton.size()==recordList.size()){
//            this.isDone = true;
//            Log.e("size",Integer.toString(endMarkRecord.recordList.size()));
//            for(Record record: recordList){
//                List<Button> buttonList = mapButton.get(record.getQuizId());
//                for(Button button : buttonList){
//                    if(button.getText().toString().equals(record.getAnswer())){
//                        Log.e("button",button.getText().toString());
//                        Log.e("answer",record.getAnswer());
//                        button.setBackgroundColor(ContextCompat.getColor(context, R.color.correct));;
//                        break;
//                    }
//                }
//            }
//        }
        if(mapViewHolder.size()==recordList.size()){
            this.isDone = true;
            Log.e("size",Integer.toString(endMarkRecord.recordList.size()));
            for(Record record: recordList){
                List<Button> buttonList = new ArrayList<>();
                QuizViewHolder quizViewHolder = mapViewHolder.get(record.getQuizId()).get(0);
                buttonList.add(quizViewHolder.btnAnswer1);
                buttonList.add(quizViewHolder.btnAnswer2);
                buttonList.add(quizViewHolder.btnAnswer3);
                buttonList.add(quizViewHolder.btnAnswer4);
                for(Button button : buttonList){
                    if(button.getText().toString().equals(record.getAnswer())){
                        Log.e("button",button.getText().toString());
                        Log.e("answer",record.getAnswer());
                        button.setBackgroundColor(ContextCompat.getColor(context, R.color.correct));;
                        break;
                    }
                }
                Button btnReport = quizViewHolder.btnReport;
                btnReport.setVisibility(View.VISIBLE);
                btnReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callApiReport(record.getQuizId());
                    }
                });
            }
        }
    }

    private void callApiReport(String quizId) {
        Call<String> call = RetrofitInstance.getRetrofitInstance().create(UserService.class).reportQuiz(quizId,userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                showToast("Đã gửi báo cáo thành công");
//                if (response.isSuccessful()) {
//                    showToast("Đã gửi báo cáo thành công");
//                } else {
//                    showToast("Đã gửi báo cáo thất bại");
//                }
            }
            @Override//                Log.e("Network Request", "Error", t);

            public void onFailure(Call<String> call, Throwable t) {
                showToast("Đã gửi báo cáo thành công");
            }
        });
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4, btnReport;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitleQuizz);
            btnAnswer1 = itemView.findViewById(R.id.btnAnswer1);
            btnAnswer2 = itemView.findViewById(R.id.btnAnswer2);
            btnAnswer3 = itemView.findViewById(R.id.btnAnswer3);
            btnAnswer4 = itemView.findViewById(R.id.btnAnswer4);
            btnReport = itemView.findViewById(R.id.btnReport);
        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}