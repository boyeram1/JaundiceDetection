package edu.rosehulman.changb.boyeram1.jaundicedetection.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
<<<<<<< HEAD
import android.util.Log;

import edu.rosehulman.changb.boyeram1.jaundicedetection.Constants;

public class SharedPrefsUtils {
    private static Context mContext;
    private static String TAG = "JD-SharedPrefUtils";
=======

import edu.rosehulman.changb.boyeram1.jaundicedetection.Constants;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Child;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Family;

public class SharedPrefsUtils {
    private static Context mContext;
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121

    public static void setContext(Context context) {
        mContext = context;
    }

    public static String getCurrentFamilyKey() {
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
<<<<<<< HEAD
        String familyKey = prefs.getString(Constants.FAMILY_KEY, "");
        Log.d(TAG, "Got familyKey: " + familyKey);
        return familyKey;
    }

    public static void setCurrentFamilyKey(String familyKey) {
        Log.d(TAG, "setting familyKey: " + familyKey);
=======
        return prefs.getString(Constants.FAMILY_KEY, "");
    }

    public static void setCurrentFamilyKey(String familyKey) {
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.FAMILY_KEY, familyKey);
        editor.commit();
    }

    public static String getCurrentChildKey() {
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
<<<<<<< HEAD
        String childKey = prefs.getString(Constants.CHILD_KEY, "");
        Log.d(TAG, "Got childKey: " + childKey);
        return childKey;
    }

    public static void setCurrentChildKey(String childKey) {
        Log.d(TAG, "Setting childKey: " + childKey);
=======
        return prefs.getString(Constants.CHILD_KEY, "");
    }

    public static void setCurrentChildKey(String childKey) {
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.CHILD_KEY, childKey);
        editor.commit();
    }

}
