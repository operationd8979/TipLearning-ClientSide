package com.example.tiplearning.model.runtime;

public class UpdateInfoRequest {
    private String fullName;
    private String password;
    private String newPassword;

    public UpdateInfoRequest(String fullName,String password, String newPassword) {
        this.fullName = fullName;
        this.password = password;
        this.newPassword = newPassword;
    }
    public UpdateInfoRequest(){

    }
}
