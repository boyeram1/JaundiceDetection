package edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.Exclude;

/**
 * Created by boyeram1 on 4/15/2018.
 */

public class Child {

    private String key;
    private String familyKey;
    private String name;
    private BirthDateTime birthDateTime;

    public Child() {
        // Needed for JSON serialization
    }

    public Child(String familyKey, String name, BirthDateTime birthDateTime) {
        this.familyKey = familyKey;
        this.name = name;
        this.birthDateTime = birthDateTime;
    }

    public void setValues(Child child) {
        this.familyKey = child.getFamilyKey();
        this.name = child.getName();
        this.birthDateTime = child.getBirthDateTime();
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFamilyKey() {
        return this.familyKey;
    }

    public void setFamilyKey(String familyKey) {
        this.familyKey = familyKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BirthDateTime getBirthDateTime() {
        return birthDateTime;
    }

    public void setBirthDateTime(BirthDateTime birthDateTime) {
        this.birthDateTime = birthDateTime;
    }
}
