package com.example.quizfirebase.Model;

public class User {
    private String userName;
    private String userKelas;

    public User() {
    }

    public User(String userName, String userKelas) {
        this.userName = userName;
        this.userKelas = userKelas;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserKelas() {
        return userKelas;
    }

    public void setUserKelas(String userKelas) {
        this.userKelas = userKelas;
    }
}
