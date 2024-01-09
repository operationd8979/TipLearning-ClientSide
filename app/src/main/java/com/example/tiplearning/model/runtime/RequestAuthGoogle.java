package com.example.tiplearning.model.runtime;

public class RequestAuthGoogle {
    private String email;
    private String fullName;
    private String urlAvatar;

    public RequestAuthGoogle() {
    }

    public RequestAuthGoogle(String email, String fullName, String urlAvatar) {
        this.email = email;
        this.fullName = fullName;
        this.urlAvatar = urlAvatar;
    }
}
