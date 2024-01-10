package com.example.tiplearning.model.runtime;

import java.util.List;

public class RequestFindQuiz {
    private List<String> questions;

    public RequestFindQuiz(List<String> questions) {
        this.questions = questions;
    }

    public RequestFindQuiz(){

   }
}
