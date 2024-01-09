package com.example.tiplearning.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiplearning.R;
import com.example.tiplearning.model.entity.Record;
import com.example.tiplearning.model.runtime.MarkRecord;
import com.example.tiplearning.model.runtime.QuizResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizzesAdapter extends RecyclerView.Adapter<QuizzesAdapter.QuizViewHolder> {
    private List<QuizResponse> quizResponseList;
    private MarkRecord markRecord;
    private Context context;
    private TextView txtChecks;
    private int count = 0;

    //RecyclerView tái sử dụng ViewHolder nên có thể bị lỗi khi giữ các tham chiếu Button khi lấy trên 3 quizzes//
    //C1 để nút submit bên trong Recycler đảm bảo khi submit đã load toàn bộ View
    //C2 add từng view rồi notify
    private Map<String,List<Button>> mapButton;
    private boolean isDone = false;


    public QuizzesAdapter(Context context, List<QuizResponse> quizResponseList, TextView txtChecks) {
        this.context = context;
        this.txtChecks = txtChecks;
        this.quizResponseList = quizResponseList;
        mapButton = new HashMap<>();
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
        mapButton.put(quizResponse.quizId, Arrays.asList(holder.btnAnswer1,holder.btnAnswer2,holder.btnAnswer3,holder.btnAnswer4));
        holder.txtTitle.setText(quizResponse.title);
        holder.btnAnswer1.setText(quizResponse.answers.get(0));
        holder.btnAnswer2.setText(quizResponse.answers.get(1));
        holder.btnAnswer3.setText(quizResponse.answers.get(2));
        holder.btnAnswer4.setText(quizResponse.answers.get(3));
        holder.btnAnswer1.setOnClickListener(v -> handleAnswerClick(quizResponse.quizId, holder.btnAnswer1, holder));
        holder.btnAnswer2.setOnClickListener(v -> handleAnswerClick(quizResponse.quizId, holder.btnAnswer2, holder));
        holder.btnAnswer3.setOnClickListener(v -> handleAnswerClick(quizResponse.quizId, holder.btnAnswer3, holder));
        holder.btnAnswer4.setOnClickListener(v -> handleAnswerClick(quizResponse.quizId, holder.btnAnswer4, holder));
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
        if(mapButton.size()==recordList.size()){
            this.isDone = true;
            Log.e("size",Integer.toString(endMarkRecord.recordList.size()));
            for(Record record: recordList){
                List<Button> buttonList = mapButton.get(record.getQuizId());
                for(Button button : buttonList){
                    if(button.getText().toString().equals(record.getAnswer())){
                        Log.e("button",button.getText().toString());
                        Log.e("answer",record.getAnswer());
                        button.setBackgroundColor(ContextCompat.getColor(context, R.color.correct));;
                        break;
                    }
                }
            }
        }
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitleQuizz);
            btnAnswer1 = itemView.findViewById(R.id.btnAnswer1);
            btnAnswer2 = itemView.findViewById(R.id.btnAnswer2);
            btnAnswer3 = itemView.findViewById(R.id.btnAnswer3);
            btnAnswer4 = itemView.findViewById(R.id.btnAnswer4);
        }
    }
}