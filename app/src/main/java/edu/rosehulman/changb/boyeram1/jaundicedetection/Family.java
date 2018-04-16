package edu.rosehulman.changb.boyeram1.jaundicedetection;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Created by changb on 4/15/2018.
 */

public class Family implements Parcelable {

    private String key;
    private String name;


    public Family() {
        // Needed for JSON serialization
    }

    protected Family(Parcel in) {
        key = in.readString();
        name = in.readString();
    }

    public static final Creator<Family> CREATOR = new Creator<Family>() {
        @Override
        public Family createFromParcel(Parcel parcel) {
            return new Family(parcel);
        }

        @Override
        public Family[] newArray(int i) {
            return new Family[i];
        }
    };

    public Family(String name) {
        this.name = name;
    }

    public void setValues(Family family) {
        this.name = family.name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(name);
    }
}
