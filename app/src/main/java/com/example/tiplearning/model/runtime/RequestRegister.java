package com.example.tiplearning.model.runtime;

public class RequestRegister {
    private String email;
    private String password;
    private String fullName;
    private String urlAvatar;

    public RequestRegister() {
    }

    public RequestRegister(String email, String password, String fullName, String urlAvatar) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.urlAvatar = urlAvatar;
    }
}
