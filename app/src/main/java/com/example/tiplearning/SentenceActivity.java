package com.example.tiplearning;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiplearning.R;
import com.example.tiplearning.model.runtime.QuizResponse;
import com.example.tiplearning.model.runtime.SentenseResponse;
import com.example.tiplearning.retrofit.RetrofitInstance;
import com.example.tiplearning.service.UserService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SentenceActivity extends AppCompatActivity {
    private List<QuizResponse> quizResponseList;
    private RecyclerView recyclerQuizzes;
    private int position = 0;
    private int point = 0;
    List<SentenseResponse> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence);
        Intent intent = getIntent();
        init();
    }
    private void init(){
        //callApiGetQuizzes();
        ImageView imgButton = findViewById(R.id.btn_exit);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mList = new ArrayList<>();
        SentenseResponse response1 = new SentenseResponse("Đâu là thủ đổ của Pháp?", "What is the capital of France?");
        SentenseResponse response2 = new SentenseResponse("Hành tinh lớn nhất trong hệ mặt trời của chúng ta là gì?", "What is the largest planet in our solar system?");
        SentenseResponse response3 = new SentenseResponse("Ai đã viết Romeo và Juliet?", "Who wrote Romeo and Juliet?");
        SentenseResponse response4 = new SentenseResponse("Ai đã viết Romeo và Juliet?", "Who wrote Romeo and Juliet?");
        SentenseResponse response5 = new SentenseResponse("Ai đã viết Romeo và Juliet?", "Who wrote Romeo and Juliet?");
        SentenseResponse response6 = new SentenseResponse("Ai đã viết Romeo và Juliet?", "Who wrote Romeo and Juliet?");

        mList.add(response1);
        mList.add(response2);
        mList.add(response3);
        mList.add(response4);
        mList.add(response5);
        mList.add(response6);
        updateQuestion(mList.get(0));
    }

    public void updateQuestion(QuizResponse data){
        TextView txtTitle = findViewById(R.id.txtTitle);
        TextView txtAnswer = findViewById(R.id.txtFullAnswer);

        txtTitle.setText(data.title);
        txtAnswer.setText(data.answers.get(position));
        GridLayout linearButtonSplitLayout = findViewById(R.id.ll_wrapper_button);
        for (String answer : data.answers) {
            String[] text = answer.split(" ");
            for(String s : text) {
                Button btn = new Button(this);
                btn.setText(s);
                btn.setBackgroundResource(R.drawable.btn_split);
                linearButtonSplitLayout.addView(btn);
            }
        }
    }
    public void updateQuestion(SentenseResponse data){
        TextView txtTitle = findViewById(R.id.txtTitle);
        TextView txtAnswer = findViewById(R.id.txtFullAnswer);
        txtTitle.setText(data.getQuestions());
        GridLayout linearButtonSplitLayout = findViewById(R.id.ll_wrapper_button);
        linearButtonSplitLayout.removeAllViews();
        txtAnswer.setText("");
        TextView txtPosition = findViewById(R.id.txt_stt);
        txtPosition.setText((position+1)+"/"+mList.size());

        TextView txtPoint = findViewById(R.id.txtPoint);
        txtPoint.setText(point+"/100");
        String[] text = data.getAnswer().split(" ");
        shuffleArray(text);
        for(String s : text) {
            Button btn = new Button(this);
            btn.setText(s);
            btn.setBackgroundResource(R.drawable.btn_split);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn.setEnabled(false);
                    btn.setBackgroundResource(R.drawable.button_disabled);
                    txtAnswer.setText(txtAnswer.getText()+" "+s);
                }
            });

            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.setMargins(20,20,20,20);
            layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1);
            btn.setLayoutParams(layoutParams);
            linearButtonSplitLayout.addView(btn);
        }

        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kq = txtAnswer.getText().toString();
                if(kq.trim().equals(data.getAnswer().trim())){
                    point+=10;
                }
                position++;
                if(position<mList.size())
                {
                    updateQuestion(mList.get(position));
                }
                else {
                    finish();
                }
            }
        });
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void shuffleArray(String[] array) {
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            String temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}
