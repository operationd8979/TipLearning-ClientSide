package com.example.tiplearning.model.entity;

public class Record {
    private String quizId;
    private String answer;

    public Record(){

    }

    public Record(String quizId,String answer){
        this.quizId = quizId;
        this.answer = answer;
    }

    public String getQuizId() {
        return quizId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
