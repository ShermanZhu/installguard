package com.example.installguard;

import android.content.Context;
import android.content.SharedPreferences;

public class PasswordManager {
    private static final String PREFS_NAME = "install_guard_prefs";
    private static final String KEY_PASSWORD = "password";
    private static final String DEFAULT_PASSWORD = "123456";

    public static String getPassword(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_PASSWORD, DEFAULT_PASSWORD);
    }

    public static void setPassword(Context context, String password) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_PASSWORD, password).apply();
    }
}
