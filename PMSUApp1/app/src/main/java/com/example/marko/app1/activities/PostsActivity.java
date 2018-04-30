package com.example.marko.app1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import com.example.marko.app1.R;
import com.example.marko.app1.adapters.PostsAdapter;
import com.example.marko.app1.model.Post;
import com.example.marko.app1.model.User;

import java.util.ArrayList;

public class PostsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ListView listView = (ListView)findViewById(R.id.allPostsListView);

        ArrayList<Post> posts = new ArrayList<Post>();
        User author1 = new User();
        author1.setUsername("Marko");

        Post post1 = new Post();
        post1.setTitle("This is first post");
        post1.setAuthor(author1);

        Post post2 = new Post();
        post2.setTitle("This is sec post ");
        post2.setAuthor(author1);

        Post post3 = new Post();
        post3.setTitle("This is th post ");
        post3.setAuthor(author1);

        Post post4 = new Post();
        post4.setTitle("This is fou post ");
        post4.setAuthor(author1);

        posts.add(post1);
        posts.add(post2);
        posts.add(post3);
        posts.add(post4);

        PostsAdapter pAdapter = new PostsAdapter(this, R.layout.row_view_posts, posts);
        listView.setAdapter(pAdapter);

//
//        Button button4 = (Button) findViewById(R.id.startEksperiement);
//        button3.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent intent = new Intent(PostsActivity.this, SinglePostActivity.class);
//                startActivity(intent);
//            }
//        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.posts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(this,DetailsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
