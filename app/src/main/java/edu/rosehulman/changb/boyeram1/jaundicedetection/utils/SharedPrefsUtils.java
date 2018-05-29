package edu.rosehulman.changb.boyeram1.jaundicedetection.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import edu.rosehulman.changb.boyeram1.jaundicedetection.Constants;

public class SharedPrefsUtils {
    private static Context mContext;

    public static void setContext(Context context) {
        mContext = context;
    }

    public static String getCurrentFamilyKey() {
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        return prefs.getString(Constants.FAMILY_KEY, "");
    }

    public static void setCurrentFamilyKey(String familyKey) {
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.FAMILY_KEY, familyKey);
        editor.commit();
    }

    public static String getCurrentChildKey() {
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        return prefs.getString(Constants.CHILD_KEY, "");
    }

    public static void setCurrentChildKey(String childKey) {
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.CHILD_KEY, childKey);
        editor.commit();
    }

}
