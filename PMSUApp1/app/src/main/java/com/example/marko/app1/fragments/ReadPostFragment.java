package com.example.marko.app1.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.marko.app1.R;
import com.example.marko.app1.model.Post;
import com.example.marko.app1.model.User;

import org.w3c.dom.Text;

import java.io.File;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;


public class ReadPostFragment extends Fragment {
    public static final String TAG = "read post";

    //public static String path = "C:\Users\Marko\Desktop\test123";




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_read_post,container,false);


        File fajl = new File(System.getProperty("user.dir") + "/storage/emulated/");
        File[] pictures = fajl.listFiles();

        for(File f  : pictures) {
            Log.d("nejmz",f.getName() + f.getAbsolutePath());
        }

        User author = new User();
        author.setUsername("Markic");
        //author.setPhoto(BitmapFactory.decodeFile(path));


        Post firstPost = new Post();
        firstPost.setAuthor(author);


        firstPost.setDate(Calendar.getInstance().getTime());

        Location location = new Location("");
        location.setLatitude(0.00);
        location.setLongitude(0.00);

        firstPost.setLocation(location);

        firstPost.setDescription("This is new picture of me!");

        firstPost.setTitle("Picture update ... ");

        firstPost.setLikes(4);
        firstPost.setDislikes(6);

        //firstPost.setPhoto(BitmapFactory.decodeFile("C://Users//Marko//Desktop//newPhoto"));


        TextView title = (TextView) view.findViewById(R.id.lblTitle);
        title.setText(firstPost.getTitle());

        Log.d("logz", "proso1");

        TextView description = (TextView) view.findViewById(R.id.lblDescription);
        description.setText(firstPost.getDescription());

        Log.d("logz", "proso2");

        TextView likes = (TextView) view.findViewById(R.id.numLikes);
        likes.setText(String.valueOf(firstPost.getLikes()));

        //ImageView image = (ImageView) view.findViewById(R.id.bitmapImage);
        //image.setImageBitmap(firstPost.getPhoto());

        Log.d("logz", "proso3");

        TextView dislikes = (TextView) view.findViewById(R.id.numDislikes);
        dislikes.setText(String.valueOf(firstPost.getDislikes()));

        Log.d("logz", "proso5");

        TextView authorUsername = (TextView) view.findViewById(R.id.authorUsername);
        authorUsername.setText(firstPost.getAuthor().getUsername());

        Log.d("logz", "proso6");

        TextView date = (TextView) view.findViewById(R.id.posted);
        date.setText(String.valueOf(firstPost.getDate()));

        Log.d("logz", "proso7");

        TextView itsLocation = (TextView) view.findViewById(R.id.location);
        itsLocation.setText("Novi Sad");

//        ImageView imageAuthor = (ImageView) view.findViewById(R.id.authorPhoto);
//        imageAuthor.setImageBitmap(firstPost.getAuthor().getPhoto());
//


        return view;
    }
}
