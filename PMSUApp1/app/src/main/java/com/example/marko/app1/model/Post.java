package com.example.marko.app1.model;

import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Marko on 4/17/2018.
 */

public class Post {
    private int id;
    private String title;
    private String description;
    private String photoURL;
    private int author_id;
    private String created;

    private String location;

    //metode za konvertovanje
//    private Date date;
//    private Location location;
    private int numberLikes;
    private int numberDislikes;

    public Post() {
    }

    public Post(int id, String title, String description, String photoURL, int author_id, String created, String location, int numberLikes, int numberDislikes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.photoURL = photoURL;
        this.author_id = author_id;
        this.created = created;
        this.location = location;
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

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", author_id=" + author_id +
                ", created='" + created + '\'' +
                ", location='" + location + '\'' +
                ", numberLikes=" + numberLikes +
                ", numberDislikes=" + numberDislikes +
                '}';
    }

    //HMM DA LI SME?
//    public void setDateAsString(String stringDate) {
//        /**format is dd/mm/yyyy*/
//        //Log.d("vreme", Calendar.getInstance().getTime().toString());
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//
//        Date postCreated = null;
//        try {
//            postCreated = formatter.parse(stringDate);
//            this.date = postCreated;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

}
