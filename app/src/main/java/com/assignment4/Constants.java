package com.assignment4;

import android.content.Intent;

import com.google.gson.Gson;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;

public class Constants
{
    public static float radius=2;
    public static String user_name;
    public static String user_email;
    public static final String TEST_URL = "http://192.168.49.6:8080";

    public static Intent getIntent(String s)
    {
        Intent i=new Intent(s);
        return i;
    }

    public static PostSvcApi postService = new RestAdapter.Builder()
            .setEndpoint(TEST_URL)
            .setLogLevel(LogLevel.FULL)
            .setConverter(new LenientGsonConverter(new Gson()))
            .build()
            .create(PostSvcApi.class);


}
