package com.example.marko.app1.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marko.app1.R;
import com.example.marko.app1.model.Post;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Marko on 4/30/2018.
 */

public class PostsAdapter extends ArrayAdapter<Post> {


    Context context;
    int resource;
    ArrayList<Post> posts = new ArrayList<Post>();

    public PostsAdapter(@NonNull Context context, int resource, ArrayList<Post> posts) {
        super(context, resource, posts);
        this.context = context;
        this.resource = resource;
        this.posts = posts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Post post = posts.get(position);

        //Samo za prvi view
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }


        ImageView authorImage = (ImageView) convertView.findViewById(R.id.postAuthorImage);
        TextView postTitle = (TextView) convertView.findViewById(R.id.postTitle);
        TextView authorName = (TextView) convertView.findViewById(R.id.postAuthorUsername);
        //TextView postDate = (TextView) convertView.findViewById(R.id.postDate);


        postTitle.setText(post.getTitle());
        authorName.setText("Mita");
        //postDate.setText(String.valueOf(post.getDate()));
        Picasso.get().load("https://scontent.fbeg2-1.fna.fbcdn.net/v/t1.0-9/29512171_1865457216799828_1883088454853879528_n.jpg?_nc_cat=0&oh=26b6bf1c65ff8cdd8b080c7423ec1fc7&oe=5B58EEA9").into(authorImage);


        return convertView;
    }
}
