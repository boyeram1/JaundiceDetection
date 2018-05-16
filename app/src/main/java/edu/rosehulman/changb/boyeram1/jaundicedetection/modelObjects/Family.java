package edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Created by changb on 4/15/2018.
 */

public class Family {

    private String key;
    private String name;


    public Family() {
        // Needed for JSON serialization
    }

    public Family(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValues(Family family) {
        this.name = family.getName();
    }
}
