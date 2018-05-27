package com.example.marko.app1.model;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Marko on 4/17/2018.
 */

public class User {
    private int id;
    private String photoURL;
    private String username;
    private String password;

    public User() {
    }

    public User(int id, String photoURL, String username, String password) {
        this.id = id;
        this.photoURL = photoURL;
        this.username = username;
        this.password = password;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
