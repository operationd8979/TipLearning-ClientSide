package com.example.tiplearning.model.runtime;

import java.util.List;

public class QuizResponse {
    public String quizId;
    public String title;
    public List<String> answers;

    public QuizResponse(){

    }

    public QuizResponse(String quizId,String title, List<String> answers){
        this.quizId = quizId;
        this.title = title;
        this.answers = answers;
    }
}