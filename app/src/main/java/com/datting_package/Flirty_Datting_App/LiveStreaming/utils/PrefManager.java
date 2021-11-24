package com.datting_package.Flirty_Datting_App.LiveStreaming.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.datting_package.Flirty_Datting_App.LiveStreaming.Constants;


public class PrefManager {
    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }
}
