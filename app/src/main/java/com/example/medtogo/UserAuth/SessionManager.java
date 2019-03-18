package com.example.medtogo.UserAuth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.medtogo.MainActivity;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String PHONE = "PHONE";
    public static final String USERID ="USERID";
    public static final String ADDRESS ="ADDRESS";
    public static final String CITY ="CITY";
    public static final String LAT ="LAT";
    public static final String LNG ="LNG";

    public SessionManager (Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }
    public void createSession(String name, String email,String phone, String userid,String address, String city, String lat, String lng){
        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(PHONE, phone);
        editor.putString(USERID, userid);
        editor.putString(ADDRESS, address);
        editor.putString(CITY, city);
        editor.putString(LAT, lat);
        editor.putString(LNG, lng);
        editor.apply();
    }

    public boolean isLogin(){
        return sharedPreferences.getBoolean(LOGIN,false);
    }

    public void checkLogin(){
        if(!this.isLogin()){
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((MainActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(NAME,sharedPreferences.getString(NAME, null));
        user.put(EMAIL,sharedPreferences.getString(EMAIL, null));
        user.put(PHONE,sharedPreferences.getString(PHONE, null));
        user.put(USERID,sharedPreferences.getString(USERID, null));
        user.put(ADDRESS,sharedPreferences.getString(ADDRESS, null));
        user.put(CITY,sharedPreferences.getString(CITY, null));
        user.put(LAT,sharedPreferences.getString(LAT, null));
        user.put(LNG,sharedPreferences.getString(LNG, null));
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        ((MainActivity) context).finish();
    }
}
