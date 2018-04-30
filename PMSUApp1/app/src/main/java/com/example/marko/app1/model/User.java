package com.example.marko.app1.model;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Marko on 4/17/2018.
 */

public class User {
    private int ID;
    private Bitmap photo;
    private String username;
    private String password;
    private List<Post> posts;
    private List<Comment> comments;

    public User() {
    }

    public User(int ID, Bitmap photo, String username, String password, List<Post> posts, List<Comment> comments) {
        this.ID = ID;
        this.photo = photo;
        this.username = username;
        this.password = password;
        this.posts = posts;
        this.comments = comments;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
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

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
