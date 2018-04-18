package com.example.marko.app1.model;

import android.graphics.Bitmap;
import android.location.Location;

import java.util.Date;
import java.util.List;

/**
 * Created by Marko on 4/17/2018.
 */

public class Post {
    private int ID;
    private String title;
    private String description;
    private Bitmap photo;
    private User author;
    private Date date;
    private Location location;
    private List<Tag> tags;
    private List<Comment> comments;
    private int likes;
    private int dislikes;
}
