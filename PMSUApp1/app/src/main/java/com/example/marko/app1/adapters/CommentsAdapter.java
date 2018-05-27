package com.example.marko.app1.adapters;

import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.app1.R;
import com.example.marko.app1.RESTService.Service;
import com.example.marko.app1.model.Comment;
import com.example.marko.app1.model.LikeDislike;
import com.example.marko.app1.model.User;
import com.example.marko.app1.utils.AppUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class CommentsAdapter extends ArrayAdapter<Comment> {

    Context context;
    int resource;
    List<Comment> comments = new ArrayList<Comment>();

    public CommentsAdapter(@NonNull Context context, int resource, List<Comment> comments) {
        super(context, resource, comments);
        this.context = context;
        this.resource = resource;
        this.comments = comments;
    }
    @Override
    public void remove(@Nullable Comment object) {
        super.remove(object);
    }
    @Override
    public void add(@Nullable Comment object) {
        super.add(object);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Comment comment = comments.get(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }
        final TextView commentRating = (TextView) convertView.findViewById(R.id.commentRating);
        final TextView commentDescription = (TextView) convertView.findViewById(R.id.commentDescription);
        final TextView commentCreated = (TextView) convertView.findViewById(R.id.commentCreated);

        final ImageView authorImage = (ImageView) convertView.findViewById(R.id.commentAuthorImage);
        final TextView authorUsername = (TextView) convertView.findViewById(R.id.commentAuthorUsername);

        final Button deleteCommentBtn = (Button) convertView.findViewById(R.id.deleteCommentBtn);

        commentDescription.setText(comment.getDescription());
        commentRating.setText(String.valueOf(Integer.valueOf(comment.getNumberLikes()) - Integer.valueOf(comment.getNumberDislikes())));

        Date commentCreatedDate = new Date(Long.parseLong(comment.getCreated()));
        final String commentCreatedDateString = commentCreatedDate.toString();
        String commentCreatedDateStringShow = commentCreatedDateString.substring(0,10) + " " +  commentCreatedDateString.substring(commentCreatedDateString.length()-4,commentCreatedDateString.length());
        commentCreated.setText(commentCreatedDateStringShow);

        Call<User> userCall = Service.service.restApi.getUserById(comment.getAuthor_id());
        userCall.enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();

                authorUsername.setText(user.getUsername());
                Picasso.get().load(user.getPhotoURL()).resize(100,100).into(authorImage, new Callback() {
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
                        Toast.makeText(getContext(), "Image load failed", Toast.LENGTH_SHORT).show();
                    }
                });
                if(user.getId() == AppUtils.LoggedInUser.getLoggedInUser().getId()) {
                    deleteCommentBtn.setVisibility(View.VISIBLE);
                } else {
                    deleteCommentBtn.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "REST comments failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        final int commentId = comment.getId();
        final View rview = convertView;

        deleteCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseBody> deleteCall = Service.service.restApi.deleteComment(commentId);
                deleteCall.enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(getContext(), "Comment delete successfully", Toast.LENGTH_SHORT).show();
                        remove(comment);
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
        final ImageView likeCommentBtn = (ImageView) convertView.findViewById(R.id.likeCommentBtn);
        final ImageView dislikeCommentBtn = (ImageView) convertView.findViewById(R.id.dislikeCommentBtn);
        final TextView commentLikeDislikeId = (TextView) convertView.findViewById(R.id.CommentFragmentLikeDislikeId);

        Call<LikeDislike> likeDislikeCall = Service.service.restApi.getLikeDislikeComment(AppUtils.LoggedInUser.getLoggedInUser().getId(), commentId);
        likeDislikeCall.enqueue(new retrofit2.Callback<LikeDislike>() {
            @Override
            public void onResponse(Call<LikeDislike> call, Response<LikeDislike> response) {
                LikeDislike ld = response.body();
                commentLikeDislikeId.setText(String.valueOf(ld.getId()));
                if(ld.isLike() == true) {
                    likeCommentBtn.setImageResource(R.drawable.ic_liked);
                    likeCommentBtn.setTag(R.drawable.ic_liked);
                    dislikeCommentBtn.setImageResource(R.drawable.ic_dislike);
                    dislikeCommentBtn.setTag(R.drawable.ic_dislike);
                } else {

                    dislikeCommentBtn.setImageResource(R.drawable.ic_disliked);
                    dislikeCommentBtn.setTag(R.drawable.ic_disliked);
                    likeCommentBtn.setImageResource(R.drawable.ic_like);
                    likeCommentBtn.setTag(R.drawable.ic_like);
                    }
                }

            @Override
            public void onFailure(Call<LikeDislike> call, Throwable t) {
                likeCommentBtn.setImageResource(R.drawable.ic_like);
                likeCommentBtn.setTag(R.drawable.ic_like);
                dislikeCommentBtn.setImageResource(R.drawable.ic_dislike);
                dislikeCommentBtn.setTag(R.drawable.ic_dislike);
            }
        });







        final int commentAuthorId = comment.getAuthor_id();
        likeCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //OGRANICENJE NE KORISNIKA
                if(AppUtils.LoggedInUser.getLoggedInUser().getId() == commentAuthorId) {
                    Toast.makeText(getContext(), "You can't like your own stuff", Toast.LENGTH_SHORT).show();
                    return;
                }



                if(likeCommentBtn.getTag().equals(R.drawable.ic_like)) {
                    likeCommentBtn.setImageResource(R.drawable.ic_liked);
                    likeCommentBtn.setTag(R.drawable.ic_liked);

                    //ako je pritisnut dislajk na komentar
                    if(dislikeCommentBtn.getTag().equals(R.drawable.ic_disliked)) {
                        dislikeCommentBtn.setImageResource(R.drawable.ic_dislike);
                        dislikeCommentBtn.setTag(R.drawable.ic_dislike);
                        commentRating.setText(String.valueOf(Integer.valueOf(commentRating.getText().toString())  + 2));
                        Call<String> updateCall = Service.service.restApi.updateLikeDislike(Integer.valueOf(commentLikeDislikeId.getText().toString()));
                        updateCall.enqueue(new retrofit2.Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    } else {

                        int authorId = AppUtils.LoggedInUser.getLoggedInUser().getId();
                        Call<ResponseBody> createCall = Service.service.restApi.createLikeDislike(true, false, authorId, commentId);
                        createCall.enqueue(new retrofit2.Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    int newLikeDislikeId = Integer.valueOf(response.body().string());
                                    commentLikeDislikeId.setText(String.valueOf(newLikeDislikeId));

                                    int currentRating = Integer.valueOf(commentRating.getText().toString());
                                    commentRating.setText(String.valueOf(currentRating + 1));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                Toast.makeText(getContext(), "Comment liked", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                } else {
                    likeCommentBtn.setImageResource(R.drawable.ic_like);
                    likeCommentBtn.setTag(R.drawable.ic_like);

                    int likeDislikeId = Integer.valueOf(commentLikeDislikeId.getText().toString());
                    Call<ResponseBody> deleteLikeDislike = Service.service.restApi.deleteLikeDislike(likeDislikeId);
                    deleteLikeDislike.enqueue(new retrofit2.Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            int currentCommentRating = Integer.valueOf(commentRating.getText().toString());
                            commentRating.setText(String.valueOf(currentCommentRating - 1));
                            //Toast.makeText(getContext(), "Comment like removed" + String.valueOf(commentId),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }

            }
        });


        dislikeCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ogranicenje korisnik
                if(AppUtils.LoggedInUser.getLoggedInUser().getId() == commentAuthorId) {
                    Toast.makeText(getContext(), "You can't dislike your own stuff", Toast.LENGTH_SHORT).show();
                    return;
                }




                if(dislikeCommentBtn.getTag().equals(R.drawable.ic_dislike)) {
                    dislikeCommentBtn.setImageResource(R.drawable.ic_disliked);
                    dislikeCommentBtn.setTag(R.drawable.ic_disliked);

                    //ako je pritisnut dislajk na komentar
                    if(likeCommentBtn.getTag().equals(R.drawable.ic_liked)) {
                        likeCommentBtn.setImageResource(R.drawable.ic_like);
                        likeCommentBtn.setTag(R.drawable.ic_like);
                        commentRating.setText(String.valueOf(Integer.valueOf(commentRating.getText().toString()) - 2));
                        Call<String> updateCall = Service.service.restApi.updateLikeDislike(Integer.valueOf(commentLikeDislikeId.getText().toString()));
                        updateCall.enqueue(new retrofit2.Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    } else {

                        int authorId = AppUtils.LoggedInUser.getLoggedInUser().getId();
                        Call<ResponseBody> createCall = Service.service.restApi.createLikeDislike(false, false, authorId, commentId);
                        createCall.enqueue(new retrofit2.Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    int newLikeDislikeId = Integer.valueOf(response.body().string());
                                    commentLikeDislikeId.setText(String.valueOf(newLikeDislikeId));

                                    int currentRating = Integer.valueOf(commentRating.getText().toString());
                                    commentRating.setText(String.valueOf(currentRating - 1));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                Toast.makeText(getContext(), "Comment disliked", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                } else {
                    dislikeCommentBtn.setImageResource(R.drawable.ic_dislike);
                    dislikeCommentBtn.setTag(R.drawable.ic_dislike);

                    int likeDislikeId = Integer.valueOf(commentLikeDislikeId.getText().toString());
                    Call<ResponseBody> deleteLikeDislike = Service.service.restApi.deleteLikeDislike(likeDislikeId);
                    deleteLikeDislike.enqueue(new retrofit2.Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            int currentCommentRating = Integer.valueOf(commentRating.getText().toString());
                            commentRating.setText(String.valueOf(currentCommentRating + 1));
                            //Toast.makeText(getContext(), "Comment like removed" + String.valueOf(commentId),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        });


        return convertView;
    }



}
