package com.example.tiplearning.model.entity;

public class User {
    private String userId;
    private String email;
    private String fullName;
    private String urlAvatar;

    public User() {
    }

    public User(String userId,String email, String fullName, String urlAvatar) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.urlAvatar = urlAvatar;
    }

    public String getUserId() {
        return userId;
    }
    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }
}
