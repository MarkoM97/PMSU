package com.example.marko.app1.model;


import java.util.Date;

/**
 * Created by Marko on 4/17/2018.
 */

public class Comment {
    private int ID;
    private String title;
    private String description;
    private User author;
    private Date date;
    private Post post;
    private int likes;
    private int dislikes;

    public Comment() {

    }

    public Comment(int ID, String title, String description, User author, Date date, Post post, int likes, int dislikes) {
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.author = author;
        this.date = date;
        this.post = post;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public String getTitle() {

        return title;
    }

    public String getDescription() {
        return description;
    }

    public User getAuthor() {
        return author;
    }

    public Date getDate() {
        return date;
    }

    public Post getPost() {
        return post;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    //private Status status;
}
