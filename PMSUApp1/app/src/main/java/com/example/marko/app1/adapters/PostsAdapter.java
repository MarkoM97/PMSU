package com.example.marko.app1.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.app1.R;
import com.example.marko.app1.RESTService.Service;
import com.example.marko.app1.model.Post;
import com.example.marko.app1.model.PostTags;
import com.example.marko.app1.model.Tag;
import com.example.marko.app1.model.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Marko on 4/30/2018.
 */

public class PostsAdapter extends ArrayAdapter<Post> {

    Context context;
    int resource;
    List<Post> posts = new ArrayList<Post>();

    public PostsAdapter(@NonNull Context context, int resource, List<Post> posts) {
        super(context, resource, posts);
        this.context = context;
        this.resource = resource;
        this.posts = posts;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Post post = posts.get(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView postTitle = (TextView) convertView.findViewById(R.id.postTitle);
        TextView postCreated = (TextView) convertView.findViewById(R.id.postUploadDate);
        TextView postRating = (TextView) convertView.findViewById(R.id.postRating);

        final ImageView authorImage = (ImageView) convertView.findViewById(R.id.postAuthorImage);
        final TextView authorUsername = (TextView) convertView.findViewById(R.id.postAuthorUsername);

        postTitle.setText(post.getTitle());
        Date postCreatedDate = new Date(Long.parseLong(post.getCreated()));
        String postCreatedDateString = postCreatedDate.toString();
        String postCreatedDateStringShow = postCreatedDateString.substring(0,10) + " " +  postCreatedDateString.substring(postCreatedDateString.length()-4,postCreatedDateString.length());
        postCreated.setText(postCreatedDateStringShow);
        postRating.setText("Rating:" + String.valueOf(Integer.valueOf(post.getNumberLikes()) - Integer.valueOf(post.getNumberDislikes())));

        Call<User> postAuthor = Service.service.restApi.getUserById(post.getAuthor_id());
        postAuthor.enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User author = response.body();
                authorUsername.setText(author.getUsername());
                Picasso.get().load(author.getPhotoURL()).resize(150,150).into(authorImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) authorImage.getDrawable()).getBitmap();
                        RoundedBitmapDrawable roundedBitmap = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                        roundedBitmap.setCircular(true);
                        roundedBitmap.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        authorImage.setImageDrawable(roundedBitmap);
                    }
                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getContext(), "Image failed to load", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        final LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.thirdLinearLayout);
        linearLayout.removeAllViews();

        Call<List<Tag>> postTags = Service.service.restApi.getTagsByPostId(post.getId());
        postTags.enqueue(new retrofit2.Callback<List<Tag>>() {
            @Override
            public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                List<Tag> tags = response.body();
                for(Tag t : tags) {
                    TextView tagView = new TextView(getContext());
                    tagView.setText(t.getContent());
                    tagView.setTextColor(Color.BLUE);
                    tagView.setTextSize(12);
                    tagView.setPadding(0,0,10,0);
                    linearLayout.addView(tagView);
                }
            }
            @Override
            public void onFailure(Call<List<Tag>> call, Throwable t) {
                Toast.makeText(getContext(), "REST tag failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }



}
