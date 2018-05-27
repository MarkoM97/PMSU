package com.example.marko.app1.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.marko.app1.R;
import com.example.marko.app1.adapters.CommentsAdapter;
import com.example.marko.app1.RESTService.Service;
import com.example.marko.app1.model.Comment;
import com.example.marko.app1.utils.AppUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReadCommentsFragment extends Fragment {
    private static List<Comment> postComments = new ArrayList<Comment>();

    private static CommentsAdapter cAdapter;
    private static int postId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = getLayoutInflater().inflate(R.layout.fragment_read_comments, container, false);

        final String postIdString = getArguments().getString("postID");
        postId = Integer.valueOf(postIdString);
        final Button submitButton = (Button) view.findViewById(R.id.submitComment);
        final EditText commentContentEditText = (EditText) view.findViewById(R.id.writeComment);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(commentContentEditText == null || commentContentEditText.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Comment must have content", Toast.LENGTH_SHORT).show();
                    return;
                }
                String commentContent = commentContentEditText.getText().toString();
                final int authorId = AppUtils.LoggedInUser.getLoggedInUser().getId();
                int postId = Integer.valueOf(postIdString);

                Call<Comment> uploadCommentCall = Service.service.restApi.createComment("title?", commentContent, authorId, postId );
                uploadCommentCall.enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        Comment comment = response.body();
                        if(comment == null) {
                            Log.d("Response body", "null");
                        } else {
                            cAdapter.add(comment);
                            commentContentEditText.setText("");
                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(commentContentEditText.getRootView().getWindowToken(), 0);

                            Toast.makeText(getContext(), "Coment upload sucessfull", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortCommentsEntry = myPrefs.getString("sort_comments", null );
        if(sortCommentsEntry == null) {
            sortCommentsEntry = "1";

        }
        populateComments(this.getView(), Integer.valueOf(sortCommentsEntry));
    }

    private void populateComments(View view, final int sortVal) {
        final ListView listView = (ListView) view.findViewById(R.id.commentsListView);

        Call<List<Comment>> getComentsForPostId = Service.service.restApi.getCommentsByPostId(postId);
        getComentsForPostId.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                List<Comment> comments = response.body();
                comments = sortComments(comments, sortVal);
                cAdapter = new CommentsAdapter(getActivity(), R.layout.row_view_comments,comments);
                listView.setAdapter(cAdapter);
            }
            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
            }
        });

    }

    private List<Comment> sortComments(List<Comment> oldComments, int val) {
        ArrayList<Comment> newList = new ArrayList<Comment>();
        Comment newestComment = new Comment();
        if(val == 1 ){
            while (!oldComments.isEmpty()) {
                Comment postForDelete = findHighestByCreated(oldComments);
                newList.add(postForDelete);
                oldComments.remove(postForDelete);
            }
        } else if(val == 2) {
            while(!oldComments.isEmpty()) {
                Comment postForDelete = findHighestByRating(oldComments);
                newList.add(postForDelete);
                oldComments.remove(postForDelete);
            }
        } else if(val == 3) {
            return oldComments;
        }

        return newList;
    }


    private Comment findHighestByCreated(List<Comment> list) {
        Comment highest = new Comment();
        for (int i = 0; i < list.size(); i++) {
            highest = list.get(i);
            for (int j = 0; j < list.size(); j++) {
                Date highestCommentCreated = AppUtils.convertDates.getDateFromString(highest.getCreated());
                Date secondCommentCreated = AppUtils.convertDates.getDateFromString(list.get(j).getCreated());
                if (secondCommentCreated.after(highestCommentCreated)) {
                    highest = list.get(j);
                }
            }
        }
        return highest;
    }

    private Comment findHighestByRating(List<Comment> list) {
        Comment highest = new Comment();
        for(int i = 0;i < list.size(); i++) {
            highest = list.get(i);
            for (int j = 0; j < list.size(); j++) {
                int highestCommentRating = highest.getNumberLikes() - highest.getNumberDislikes();
                int secondCommentRating = list.get(j).getNumberLikes() - list.get(j).getNumberDislikes();
                if(secondCommentRating > highestCommentRating) {
                    highest = list.get(j);
                }
            }
        }
        return highest;




    }
}
