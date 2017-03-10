package com.app.test.airbnb.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Juan on 09/03/2017.
 */
public class Settings {

    public static final String PREFS_NAME = "AirBnbPreferences";

    private static Settings ourInstance;
    private SharedPreferences sharedPreferences;
    private static final String FB_SESSION_TOKEN = "fb_session_token";

    public static Settings getInstance(Context context) {

        if (ourInstance == null) {
            ourInstance = new Settings(context);
        }
        return ourInstance;
    }

    private Settings(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void clearPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void setSessionToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FB_SESSION_TOKEN, token);
        editor.commit();
    }

    public String getSessionToken() {
        return sharedPreferences.getString(FB_SESSION_TOKEN, null);
    }

    public boolean isUserLoggedIn() {
        return (getSessionToken() != null);
    }

}
