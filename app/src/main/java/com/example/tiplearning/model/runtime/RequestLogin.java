package com.example.tiplearning.model.runtime;

public class RequestLogin {
    private String email;
    private String password;

    public RequestLogin() {
    }

    public RequestLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
