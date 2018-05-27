package com.example.marko.app1.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Size;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.app1.R;
import com.example.marko.app1.RESTService.Service;
import com.example.marko.app1.model.Tag;
import com.example.marko.app1.utils.AppUtils;
import com.example.marko.app1.utils.ImageUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by Marko on 4/17/2018.
 */

public class CreatePostActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FusedLocationProviderClient client;
    private Geocoder geocoder;
    private LocationRequest locationRequest;
    private static int numberOfTags;
    private static ArrayList<String> knownTags = new ArrayList<String>();
    private static double locationLat;
    private static double locationLng;
    private static int iterationsCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        numberOfTags = 0;
        iterationsCount = 0;

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


        Button button = (Button) findViewById(R.id.choose_image_from_gallery);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(requestImagePermission()){
                    selectPhotoFromGallery();
                }

            }
        });

        Button addTagBtn = (Button) findViewById(R.id.addTagBtn);
        addTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTag();
            }
        });

        getTagsREST();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        client = LocationServices.getFusedLocationProviderClient(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        obtainLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();

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
        getMenuInflater().inflate(R.menu.toolbar_menu_create_post, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.toolbar_create_post_positive) {
            Bitmap imageBitmap = ImageUtils.imageUtils.getBitmap();
            if(imageBitmap == null) {
                Toast.makeText(getApplication(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                return false;
            }
            ByteArrayOutputStream baot = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baot);
            byte[] imageBytes = baot.toByteArray();
            imageBitmap.recycle();

            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageBytes );
            MultipartBody.Part body = MultipartBody.Part.createFormData("image","uploadImage" , requestBody);

            int postAuthorId = AppUtils.LoggedInUser.getLoggedInUser().getId();

            EditText newPostTitle = (EditText) findViewById(R.id.createPostTitle);
            if(newPostTitle.getText().toString() == null || newPostTitle.getText().toString().equals("")) {
                Toast.makeText(getApplication(), "You need to fill are fields", Toast.LENGTH_SHORT).show();
                return false;
            }
            String postTitle = newPostTitle.getText().toString();

            EditText newPostDescription = (EditText) findViewById(R.id.createPostDescription);
            if(newPostDescription.getText().toString() == null || newPostDescription.getText().toString().equals("")) {
                Toast.makeText(getApplication(), "You need to fill are fields", Toast.LENGTH_SHORT).show();
                return false;
            }
            String postDescription = newPostDescription.getText().toString();
            String postLocation = String.valueOf(locationLat) + "/" + String.valueOf(locationLng);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.createPostLinLayout);
            final int[] numberOfTags = {linearLayout.getChildCount()};

            StringBuilder builder = new StringBuilder();
            for(int i = 0; i< numberOfTags[0]; i++) {
                EditText tagByIndex = (EditText) linearLayout.getChildAt(i);
                String tagContent = tagByIndex.getText().toString();

                if(tagContent.equals("")) {
                    Toast.makeText(getApplication(), "You need to fill are fields",Toast.LENGTH_SHORT).show();
                    return false;
                }
                builder.append(tagContent + "/");
            }

            String[] postTags = builder.toString().split("/");

            Call<ResponseBody> call2 = Service.service.restApi.createPost(body, postAuthorId,postTitle, postDescription, postLocation,postTags);
            call2.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ResponseBody rb = response.body();
                    if(rb == null) {
                       Log.d("Response body", "NULL");
                    } else{
                        Toast.makeText(getApplicationContext(), "Post uploaded successfully", Toast.LENGTH_LONG).show();
                        ImageUtils.imageUtils.clearBitmap();
                        startActivity(new Intent(getApplicationContext(), PostsActivity.class));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Response body", t.getMessage());
                    Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_LONG).show();
                }
            });



            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(this, PostsActivity.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(this, SettingsActivity.class));
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


    public void selectPhotoFromGallery() {
        requestImagePermission();
        Intent intent = new Intent(Intent.ACTION_PICK);
        File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirPath = pictureDir.getPath();
        Uri dataUri = Uri.parse(pictureDirPath);
        intent.setDataAndType(dataUri, "image/*");
        startActivityForResult(intent, 30);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Request codes", String.valueOf(requestCode));
        if (resultCode == RESULT_OK) {
            if (requestCode == 30) {
                if (data != null) {
                    Uri imageUri = data.getData();
                    ImageView fetchedImage = (ImageView) findViewById(R.id.display_image_from_gallery);
                    int rotation = ImageUtils.imageUtils.fixRotation(this,imageUri);
                    Size size = ImageUtils.imageUtils.getBitmapSize();
                    int targetWidth = size.getWidth();
                    int targetHeight = size.getHeight();

                    if(rotation == 90) {
                        Picasso.get().load(imageUri).rotate(90).resize(targetHeight,targetWidth).into(fetchedImage);
                    } else if( rotation == 180) {
                        Picasso.get().load(imageUri).rotate(0).resize(targetHeight,targetWidth).into(fetchedImage);
                    } else if(rotation == 270){
                        Picasso.get().load(imageUri).rotate(270).resize(targetHeight,targetWidth).into(fetchedImage);
                    } else {
                        Picasso.get().load(imageUri).resize(targetWidth,targetWidth).into(fetchedImage);
                    }
                }
            } else {
                Log.d("Request codes", String.valueOf(requestCode));
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtainLocation();
            } else {
                Toast.makeText(getApplication(), "Location required to upload post", Toast.LENGTH_SHORT).show();
            }
            return;
        } else if (requestCode == 2) {
             if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectPhotoFromGallery();
             }

        }
    }
    private boolean requestImagePermission() {

            int permission = ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
            if(permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2 );
                return false;
            }
            return true;
   }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(gps_enabled || network_enabled) {
                return true;
            }
        } catch (Exception e) {e.printStackTrace();}

        if(!gps_enabled && !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("This app needs to have location permission to operate");
            dialog.setTitle("Activate location");
            dialog.setPositiveButton("Activate", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            });
            dialog.show();

        }
        return false;
    }

    public void obtainLocation() {

        if (ActivityCompat.checkSelfPermission(CreatePostActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(CreatePostActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},     1);
            return;
        }
            client.getLastLocation().addOnSuccessListener(CreatePostActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        locationLat = location.getLatitude();
                        locationLng = location.getLongitude();

                        Address myAddress;
                        geocoder = new Geocoder(CreatePostActivity.this);
                        try {
                            List<Address> matches = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            if (!matches.isEmpty()) {
                                myAddress = matches.get(0);
                                Log.d("application Location", String.valueOf(locationLat) + String.valueOf(locationLng));
                                TextView textView = (TextView) findViewById(R.id.location_textview);
                                textView.setText(String.valueOf(myAddress.getAddressLine(0)));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if(isLocationEnabled()) {
                            obtainLocation();
                        } else {
                            Toast.makeText(getApplicationContext(), "Location required to upload post", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

    private void addTag() {
        numberOfTags ++ ;
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.createPostLinLayout);
        AutoCompleteTextView tag = new AutoCompleteTextView(this);
        tag.setHint("Tag" + String.valueOf(numberOfTags));
        tag.setId(numberOfTags);
        tag.setWidth(25);
        ArrayAdapter<String> tagAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, knownTags);
        tag.setAdapter(tagAdapter);
        linearLayout.addView(tag);
    }

    public void getTagsREST() {
        Call<List<Tag>> tagsCall = Service.service.restApi.getTags();
        knownTags.clear();
        tagsCall.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                List<Tag> tags = response.body();
                for(Tag t : tags) {
                    knownTags.add(t.getContent());
                }
            }

            @Override
            public void onFailure(Call<List<Tag>> call, Throwable t) {

            }
        });
    }




}
