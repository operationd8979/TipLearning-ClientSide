package com.example.tiplearning.model.entity;

public class User {
    private String email;
    private String name;
    private String photoUrl;

    public User() {
    }

    public User(String email, String name, String photoUrl) {
        this.email = email;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
