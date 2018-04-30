package com.example.marko.app1.model;

import java.util.List;

/**
 * Created by Marko on 4/17/2018.
 */

public class Tag {
    private int ID;
    private String name;
    private List<Post> posts;

    public Tag(int ID, String name, List<Post> posts) {
        this.ID = ID;
        this.name = name;
        this.posts = posts;
    }

    public Tag() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
