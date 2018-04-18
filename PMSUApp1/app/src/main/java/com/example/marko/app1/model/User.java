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

}
