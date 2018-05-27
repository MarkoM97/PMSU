package com.example.marko.app1.model;


import java.util.Date;

/**
 * Created by Marko on 4/17/2018.
 */

public class Comment {
    private int id;
    private String title;
    private String description;
    private String created;
    private int author_id;
    private int post_id;
    private int numberLikes;
    private int numberDislikes;
    //Implementirati metodu
//    private Date date;


    public Comment() {

    }

    public Comment(int id, String title, String description, int author_id, String created, int post_id, int numberLikes, int numberDislikes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author_id = author_id;
        this.created = created;
        this.post_id = post_id;
        this.numberLikes = numberLikes;
        this.numberDislikes = numberDislikes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getNumberLikes() {
        return numberLikes;
    }

    public void setNumberLikes(int numberLikes) {
        this.numberLikes = numberLikes;
    }

    public int getNumberDislikes() {
        return numberDislikes;
    }

    public void setNumberDislikes(int numberDislikes) {
        this.numberDislikes = numberDislikes;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", created='" + created + '\'' +
                ", author_id=" + author_id +
                ", post_id=" + post_id +
                ", numberLikes=" + numberLikes +
                ", numberDislikes=" + numberDislikes +
                '}';
    }
}
