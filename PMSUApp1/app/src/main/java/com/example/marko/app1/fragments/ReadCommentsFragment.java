package com.example.marko.app1.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marko.app1.R;



public class ReadCommentsFragment extends Fragment {

    private static final String TAG = "read comments";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = getLayoutInflater().inflate(R.layout.fragment_read_comments, container, false);

        //Sav neseseri kod

        return view;
    }
}
