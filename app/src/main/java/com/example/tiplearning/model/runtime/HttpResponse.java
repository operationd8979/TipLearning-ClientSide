package com.example.tiplearning.model.runtime;

public class HttpResponse {
    private int code;
    private TinyUser user;
    private String errorMessage;

    public int getCode() {
        return code;
    }

    public TinyUser getUser() {
        return user;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
