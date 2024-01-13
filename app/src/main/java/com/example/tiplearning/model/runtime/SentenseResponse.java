package com.example.tiplearning.model.runtime;

import java.util.List;

public class SentenseResponse {
    private String questions;
    private String answer;

    public SentenseResponse(String questions, String answer) {
        this.questions = questions;
        this.answer = answer;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
