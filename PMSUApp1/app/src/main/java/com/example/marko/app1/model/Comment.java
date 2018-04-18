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
    //private Status status;
}
