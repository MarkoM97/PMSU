package com.example.marko.app1.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.example.marko.app1.RESTService.Service;
import com.example.marko.app1.model.User;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;

public class AppUtils {
    public static class LoggedInUser{

        private static User loggedInUser;

        public static User getLoggedInUser() {
            return loggedInUser;
        }

        public static void setLoggedInUser(User user) {
            loggedInUser = user;
        }
    }


    public static class convertDates{
        public static Date getDateFromString(String dateString) {
            Date postCreatedDate = new Date(Long.parseLong(dateString));
            return postCreatedDate;
        }

        public static Date parseDateFromString(String dateString) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = format.parse(dateString);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class permissions {

    }
}
