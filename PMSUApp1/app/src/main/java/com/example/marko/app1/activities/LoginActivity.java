package com.example.marko.app1.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.app1.R;
import com.example.marko.app1.RESTService.Service;
import com.example.marko.app1.model.Post;
import com.example.marko.app1.model.User;
import com.example.marko.app1.utils.AppUtils;
import com.squareup.picasso.Picasso;


import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText username = (EditText) findViewById(R.id.loginUsername);
        final EditText password = (EditText) findViewById(R.id.loginPassword);
        final TextView badCredidentials = (TextView) findViewById(R.id.badLogin);

        final ImageView userImage = (ImageView) findViewById(R.id.navigationHeaderImage);
        final TextView userUsername = (TextView) findViewById(R.id.navigationHeaderText);



        Button button = (Button) findViewById(R.id.startPostsBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(username.getText().toString().equals("") && password.getText().toString().equals("")) {
                    Intent intent = new Intent(LoginActivity.this, PostsActivity.class);
                    startActivity(intent);
                    finish();
                }

                Call<User> userCall = Service.service.restApi.authenticate(username.getText().toString(),password.getText().toString());
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User user = response.body();
                            if(user != null) {
                                AppUtils.LoggedInUser.setLoggedInUser(user);
                                Intent intent = new Intent(LoginActivity.this, PostsActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                badCredidentials.setVisibility(View.VISIBLE);
                            }
                        }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "There is no internet connection", Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });






    }


}
