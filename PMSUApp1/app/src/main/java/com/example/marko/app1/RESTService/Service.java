package com.example.marko.app1.RESTService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Singleton pattern
public class Service {

    public static class service{
        public static Retrofit retrofit = new Retrofit.Builder().baseUrl(RESTApi.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        public static RESTApi restApi = retrofit.create(RESTApi.class);
    }
}
