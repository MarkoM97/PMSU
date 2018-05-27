package com.example.marko.app1.model;

import java.util.List;

/**
 * Created by Marko on 4/17/2018.
 */

public class Tag {
    private int id;
    private String content;

    public Tag(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public Tag() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
