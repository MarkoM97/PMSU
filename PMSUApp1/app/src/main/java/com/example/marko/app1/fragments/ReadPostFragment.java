package com.example.marko.app1.fragments;
////
////
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//
//
import com.example.marko.app1.R;
import com.example.marko.app1.RESTService.Service;
//import com.example.marko.app1.activities.MapsActivity;
import com.example.marko.app1.activities.MapsActivity;
import com.example.marko.app1.model.LikeDislike;
import com.example.marko.app1.model.Post;
import com.example.marko.app1.model.User;
//import com.example.marko.app1.utils.Mockup;
import com.example.marko.app1.utils.AppUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadPostFragment extends Fragment {
    public static double loclat;
    public static double loclng;

    private static int postAuthorId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.fragment_read_post,container,false);

        final TextView postTitle = (TextView) view.findViewById(R.id.lblTitle);
        final TextView postDescription = (TextView) view.findViewById(R.id.lblDescription);
        final TextView postCreated = (TextView) view.findViewById(R.id.post_created);
        final TextView postRating = (TextView) view.findViewById(R.id.readPost_postRating);
        final TextView postLocation = (TextView) view.findViewById(R.id.post_location);
        final ImageView postPhoto = (ImageView) view.findViewById(R.id.postImage);
        final ImageView authorPhoto = (ImageView) view.findViewById(R.id.authorPhoto);
        final String postID = getArguments().getString("postID");

        Call<Post> postCall = Service.service.restApi.getPostById(Integer.valueOf(postID));
        postCall.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Post post = response.body();

                postTitle.setText(post.getTitle());
                postDescription.setText(post.getDescription());
                Picasso.get().load(post.getPhotoURL()).into(postPhoto);

                Date postCreatedDate = new Date(Long.parseLong(post.getCreated()));
                String postCreatedDateString = postCreatedDate.toString();
                String postCreatedDateStringShow = postCreatedDateString.substring(0,10) + " " +  postCreatedDateString.substring(postCreatedDateString.length()-4,postCreatedDateString.length());
                postCreated.setText(postCreatedDateStringShow);

                postRating.setText(String.valueOf(post.getNumberLikes() - post.getNumberDislikes()));

                String latlng = post.getLocation();
                double lat = Double.valueOf(latlng.split("/")[0]);
                double lng = Double.valueOf(latlng.split("/")[1]);
                loclat = lat;
                loclng = lng;
                Location location = new Location("");
                location.setLatitude(lat);
                location.setLongitude(lng);

                Geocoder geocoder;
                List<Address> addressList;
                geocoder = new Geocoder(getContext(), Locale.getDefault());
                try {
                    addressList = geocoder.getFromLocation(lat, lng, 1);
                    String address = addressList.get(0).getAddressLine(0);
                    postLocation.setText(address);
                    postLocation.setPaintFlags(postLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(getContext(), "REST post failed "  + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Call<User> userCall = Service.service.restApi.getUserByPostId(Integer.valueOf(postID));
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                postAuthorId = user.getId();

                Picasso.get().load(user.getPhotoURL()).resize(150,150).into(authorPhoto, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) authorPhoto.getDrawable()).getBitmap();
                        RoundedBitmapDrawable roundedBitmap = RoundedBitmapDrawableFactory.create(getActivity().getResources(), imageBitmap);
                        roundedBitmap.setCircular(true);
                        roundedBitmap.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        authorPhoto.setImageDrawable(roundedBitmap);
                    }
                    @Override
                    public void onError(Exception e) {

                    }
                });
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        int loggedInUserId = AppUtils.LoggedInUser.getLoggedInUser().getId();
        getLikeDislikeUser(view,loggedInUserId, Integer.valueOf(postID));

        postLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("lat", String.valueOf(loclat));
                intent.putExtra("lng", String.valueOf(loclng));
                startActivity(intent);
            }
        });

        final TextView likeDislikeIdTextView = (TextView) view.findViewById(R.id.likeDislikeId);
        final ImageView likePost = (ImageView) view.findViewById(R.id.likePostBtn);
        final ImageView dislikePost = (ImageView) view.findViewById(R.id.dislikePostBtn);

        likePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(AppUtils.LoggedInUser.getLoggedInUser().getId() == postAuthorId) {
                    Toast.makeText(getActivity(), "You can't like your own stuff", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(Integer.valueOf(likePost.getTag().toString()).equals((Integer)R.drawable.ic_like)) {
                    likePost.setImageResource(R.drawable.ic_liked);
                    likePost.setTag(R.drawable.ic_liked);
                    if(dislikePost.getTag().equals(R.drawable.ic_disliked)) {
                        dislikePost.setImageResource(R.drawable.ic_dislike);
                        dislikePost.setTag(R.drawable.ic_dislike);
                        postRating.setText(String.valueOf(Integer.valueOf(postRating.getText().toString()) + 2));
                        Call<String> deleteCall = Service.service.restApi.updateLikeDislike(Integer.valueOf(likeDislikeIdTextView.getText().toString()));
                        deleteCall.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {}

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {}
                        });
                    } else {
                        Call<ResponseBody> call = Service.service.restApi.createLikeDislike(true, true, AppUtils.LoggedInUser.getLoggedInUser().getId(), Integer.valueOf(postID));
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                ResponseBody rb = response.body();
                                if (rb != null) {
                                    try {
                                        int likeDislikeId = Integer.valueOf(response.body().string());
                                        likeDislikeIdTextView.setText(String.valueOf(likeDislikeId));
                                        int currentPostRating = Integer.valueOf(postRating.getText().toString());
                                        int newPostRating = currentPostRating + 1;
                                        postRating.setText(String.valueOf(newPostRating));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.d("Response body ld", t.getMessage());
                            }
                        });
                    }
                } else {
                    likePost.setImageResource(R.drawable.ic_like);
                    likePost.setTag(R.drawable.ic_like);
                    int oldRating = Integer.valueOf(postRating.getText().toString());
                    int newRating = oldRating - 1;
                    postRating.setText(String.valueOf(newRating));

                    int likeDislikeId = Integer.valueOf(likeDislikeIdTextView.getText().toString());
                    Call<ResponseBody> deleteCall = Service.service.restApi.deleteLikeDislike(likeDislikeId);
                    deleteCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response != null) {
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }

            }
        });
        dislikePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AppUtils.LoggedInUser.getLoggedInUser().getId() == postAuthorId) {
                    Toast.makeText(getActivity(), "You can't dislike your own stuff", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(Integer.valueOf(dislikePost.getTag().toString()).equals((Integer)R.drawable.ic_dislike)) {
                    dislikePost.setImageResource(R.drawable.ic_disliked);
                    dislikePost.setTag(R.drawable.ic_disliked);
                    if(likePost.getTag().equals(R.drawable.ic_liked)) {
                        likePost.setImageResource(R.drawable.ic_like);
                        likePost.setTag(R.drawable.ic_like);
                        postRating.setText(String.valueOf(Integer.valueOf(postRating.getText().toString()) - 2));
                        Call<String> deleteCall = Service.service.restApi.updateLikeDislike(Integer.valueOf(likeDislikeIdTextView.getText().toString()));
                        deleteCall.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });

                    } else {
                        Call<ResponseBody> call = Service.service.restApi.createLikeDislike(false, true, AppUtils.LoggedInUser.getLoggedInUser().getId(), Integer.valueOf(postID));
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                ResponseBody rb = response.body();
                                if (rb != null) {
                                    try {
                                        int likeDislikeId = Integer.valueOf(response.body().string());
                                        likeDislikeIdTextView.setText(String.valueOf(likeDislikeId));
                                        int currentPostRating = Integer.valueOf(postRating.getText().toString());
                                        int newPostRating = currentPostRating - 1;
                                        postRating.setText(String.valueOf(newPostRating));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.d("Response body ld", t.getMessage());
                            }
                        });
                    }

                } else {
                    dislikePost.setImageResource(R.drawable.ic_dislike);
                    dislikePost.setTag(R.drawable.ic_dislike);
                    int oldRating = Integer.valueOf(postRating.getText().toString());
                    int newRating = oldRating +  1;
                    postRating.setText(String.valueOf(newRating));

                    int likeDislikeId = Integer.valueOf(likeDislikeIdTextView.getText().toString());
                    Call<ResponseBody> deleteCall = Service.service.restApi.deleteLikeDislike(likeDislikeId);
                    deleteCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response != null) {
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        });



        return view;
    }

    private void getLikeDislikeUser(View view,int userId, int postId) {
        final ImageView likeButton = (ImageView) view.findViewById(R.id.likePostBtn);
        final ImageView dislikeButton = (ImageView) view.findViewById(R.id.dislikePostBtn);

        final TextView likeDislikeId = (TextView) view.findViewById(R.id.likeDislikeId);

        Call<LikeDislike> likeDislikeCall = Service.service.restApi.getLikeDislike(userId, postId);
        likeDislikeCall.enqueue(new Callback<LikeDislike>() {
            @Override
            public void onResponse(Call<LikeDislike> call, Response<LikeDislike> response) {
                LikeDislike ld = response.body();
                likeDislikeId.setText(String.valueOf(ld.getId()));
                if(ld.isLike() == true) {
                    likeButton.setImageResource(R.drawable.ic_liked);
                    likeButton.setTag(R.drawable.ic_liked);
                    dislikeButton.setImageResource(R.drawable.ic_dislike);
                    dislikeButton.setTag(R.drawable.ic_dislike);
                } else {
                    dislikeButton.setImageResource(R.drawable.ic_disliked);
                    dislikeButton.setTag(R.drawable.ic_disliked);
                    likeButton.setImageResource(R.drawable.ic_like);
                    likeButton.setTag(R.drawable.ic_like);
                }
            }

            @Override
            public void onFailure(Call<LikeDislike> call, Throwable t) {
                likeButton.setImageResource(R.drawable.ic_like);
                likeButton.setTag(R.drawable.ic_like);

                dislikeButton.setImageResource(R.drawable.ic_dislike);
                dislikeButton.setTag(R.drawable.ic_dislike);

            }
        });
    }
}
