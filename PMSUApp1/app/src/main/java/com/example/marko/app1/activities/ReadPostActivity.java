package com.example.marko.app1.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.app1.R;
import com.example.marko.app1.RESTService.Service;
import com.example.marko.app1.fragments.ReadCommentsFragment;
import com.example.marko.app1.fragments.ReadPostFragment;
import com.example.marko.app1.model.User;
import com.example.marko.app1.utils.AppUtils;
import com.example.marko.app1.utils.SectionsPageAdapter;
import com.squareup.picasso.Picasso;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadPostActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private static int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mSectionsPageAdapter.addFragment(new ReadPostFragment(), "readPostFragment");
        mSectionsPageAdapter.addFragment(new ReadCommentsFragment(), "readCommentsFragment");

        String postID = getIntent().getStringExtra("postID");
        postId = Integer.valueOf(postID);

        Bundle readPostBundle = new Bundle();
        readPostBundle.putString("postID", postID);
        mSectionsPageAdapter.getItem(0).setArguments(readPostBundle);
        Bundle readCommentsBundle = new Bundle();
        readCommentsBundle.putString("postID",postID);
        mSectionsPageAdapter.getItem(1).setArguments(readCommentsBundle);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPageAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

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

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
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
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_single_post, menu);

        Call<User> userCall = Service.service.restApi.getUserByPostId(postId);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if(user != null) {
                    if(user.getId() == AppUtils.LoggedInUser.getLoggedInUser().getId()) {
                        MenuItem item = menu.findItem(R.id.toolbar_delete_post_button);
                        item.setVisible(true);
                    }

                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.toolbar_create_post_button) {
            startActivity(new Intent(this, CreatePostActivity.class));
            return true;
        } else if(id == R.id.toolbar_delete_post_button) {
            Call<ResponseBody> deleteCall = Service.service.restApi.deletePost(postId);
            deleteCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(ReadPostActivity.this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), PostsActivity.class));
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(this, PostsActivity.class));
        } else if(id == R.id.createPostNavButton) {
            startActivity(new Intent(this, CreatePostActivity.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(this,SettingsActivity.class));
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
}
