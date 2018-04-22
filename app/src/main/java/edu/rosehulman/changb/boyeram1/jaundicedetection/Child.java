package edu.rosehulman.changb.boyeram1.jaundicedetection;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.Exclude;

/**
 * Created by boyeram1 on 4/15/2018.
 */

public class Child implements Parcelable {

    private String key;
    private String name;
    private BirthDateTime birthDateTime;

    public Child() {
        // Needed for JSON serialization
    }

    protected Child(Parcel in) {
        key = in.readString();
        name = in.readString();
        birthDateTime = in.readParcelable(BirthDateTime.class.getClassLoader());
    }

    public static final Creator<Child> CREATOR = new Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel parcel) {
            return new Child(parcel);
        }

        @Override
        public Child[] newArray(int i) {
            return new Child[i];
        }
    };

    public Child(String name, BirthDateTime birthDateTime) {
        this.name = name;
        this.birthDateTime = birthDateTime;
    }

    public void setValues(Child child) {
        this.name = child.getName();
        this.birthDateTime = child.getBirthDateTime();
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

    public BirthDateTime getBirthDateTime() {
        return birthDateTime;
    }

    public void setBirthDateTime(BirthDateTime birthDateTime) {
        this.birthDateTime = birthDateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(name);
        parcel.writeParcelable(birthDateTime, 0);
    }
}
