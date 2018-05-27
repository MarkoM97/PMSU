package com.example.marko.app1.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.app1.R;
import com.example.marko.app1.RESTService.Service;
import com.example.marko.app1.adapters.PostsAdapter;
import com.example.marko.app1.model.Post;
import com.example.marko.app1.utils.AppUtils;
import com.squareup.picasso.Picasso;
//import com.example.marko.app1.utils.Mockup;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        isInternetAccessEnabled();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.inflateHeaderView(R.layout.navigation_headers);

        TextView userInNavDrawer = headerView.findViewById(R.id.navigationHeaderText);
        userInNavDrawer.setText(AppUtils.LoggedInUser.getLoggedInUser().getUsername());

        final ImageView userImageInNavDrawer = headerView.findViewById(R.id.navigationHeaderImage);
        Picasso.get().load(AppUtils.LoggedInUser.getLoggedInUser().getPhotoURL()).resize(100,100).into(userImageInNavDrawer, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                Bitmap imageBitmap = ((BitmapDrawable) userImageInNavDrawer.getDrawable()).getBitmap();
                RoundedBitmapDrawable roundedBitmap = RoundedBitmapDrawableFactory.create(getApplication().getResources(), imageBitmap);
                roundedBitmap.setCircular(true);
                roundedBitmap.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                userImageInNavDrawer.setImageDrawable(roundedBitmap);
            }

            @Override
            public void onError(Exception e) {

            }
        });



    }


    @Override
    protected void onResume() {
         super.onResume();
    }




    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sortPostsEntry = myPrefs.getString("sort_posts", null );
        String sortPostsDateEntry = myPrefs.getString("date_filter", null);
        if(sortPostsEntry == null) {
            sortPostsEntry = "1";
        }
        populatePosts(Integer.valueOf(sortPostsEntry), sortPostsDateEntry, null);

    }

    private void populatePosts(final int sortParameter, @Nullable final String dateFilter, @Nullable final String filterParameter) {
        final ListView listView = (ListView)findViewById(R.id.allPostsListView);
        Call<List<Post>> call;
        if(filterParameter != null) {
            call = Service.service.restApi.getFilteredPosts(filterParameter);
        } else {
            call = Service.service.restApi.getPosts();
        }
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                List<Post> postsDTO = response.body();
                postsDTO = sortPosts(postsDTO, sortParameter, dateFilter);
                PostsAdapter pAdapter = new PostsAdapter(getApplicationContext(), R.layout.row_view_posts, postsDTO);
                listView.setAdapter(pAdapter);
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                //Obrada ako se REST servis ne odaziva
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Post item = (Post) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(PostsActivity.this, ReadPostActivity.class);
                intent.putExtra("postID", String.valueOf(item.getId()));
                startActivity(intent);
            }
        });

    }
    private List<Post> sortPosts(List<Post> oldList, int val, @Nullable  String dateFilter) {
        List<Post> newList = new ArrayList<Post>();
        Post newestPost = new Post();
        if(val == 1 ){
            while (!oldList.isEmpty()) {
                Post postForDelete = findHighestByCreated(oldList);
                newList.add(postForDelete);
                oldList.remove(postForDelete);
            }
        } else if(val == 2) {
            while(!oldList.isEmpty()) {
                Post postForDelete = findHighestByRating(oldList);
                newList.add(postForDelete);
                oldList.remove(postForDelete);
            }
        } else {
            newList = oldList;
        }
        if(dateFilter != null) {
            ArrayList<Post> newFilteredList = new ArrayList<Post>();

            Date minDateVal = AppUtils.convertDates.parseDateFromString(dateFilter);
            for(Post post : newList) {
                Date postDate = AppUtils.convertDates.getDateFromString(post.getCreated());
                if(postDate.after(minDateVal)){
                    newFilteredList.add(post);
                }
            }
            return newFilteredList;
        }

        return newList;
    }


    private Post findHighestByRating(List<Post> list) {
        Post highest = new Post();
        for(int i = 0;i < list.size(); i++) {
            highest = list.get(i);
            for (int j = 0; j < list.size(); j++) {
                int highestPostRating = highest.getNumberLikes() - highest.getNumberDislikes();
                int secondPostRating = list.get(j).getNumberLikes() - list.get(j).getNumberDislikes();
                if(secondPostRating > highestPostRating) {
                    highest = list.get(j);
                }
            }
        }
        return highest;
    }

    private Post findHighestByCreated(List<Post> list) {
        Post highest = new Post();
        for (int i = 0; i < list.size(); i++) {
            highest = list.get(i);
            for (int j = 0; j < list.size(); j++) {
                Date highestPostCreated = AppUtils.convertDates.getDateFromString(highest.getCreated());
                Date secondPostCreated = AppUtils.convertDates.getDateFromString(list.get(j).getCreated());
                if (secondPostCreated.after(highestPostCreated)) {
                    highest = list.get(j);
                }
            }
        }
        return highest;
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

    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_posts, menu);

        final MenuItem searchActionMenuItem = menu.findItem(R.id.toolbar_search_button);
        SearchView searchView = (SearchView) searchActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext() );
                String sortPostsEntry = myPrefs.getString("sort_posts", null );
                String sortPostsDateEntry = myPrefs.getString("date_filter", null);
                if(sortPostsEntry == null) {
                    sortPostsEntry = "1";
                }

                populatePosts(Integer.valueOf(sortPostsEntry), sortPostsDateEntry, query.toString());
                Toast.makeText(getApplicationContext(), "Posts filtered",Toast.LENGTH_SHORT).show();
                searchActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.toolbar_search_button) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            onStart();
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if(id == R.id.createPostNavButton) {
            startActivity(new Intent(this, CreatePostActivity.class));
        } else if(id == R.id.nav_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void isInternetAccessEnabled() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo == null) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("This app needs internet access for regular usage");
            dialog.setTitle("Provide internet connection");
            dialog.setPositiveButton("Turn on", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dialog.show();
        }
    }
}
