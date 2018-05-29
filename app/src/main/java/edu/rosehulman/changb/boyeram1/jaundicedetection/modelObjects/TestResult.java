package edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Created by changb on 4/22/2018.
 */

public class TestResult implements Parcelable {

    private String key;
    private String childKey;
    private TestResultTime testResultTime;
    private Photo photo;
    private int result;

    public TestResult() {}

    public TestResult(String childKey, TestResultTime testResultTime, Photo photo, int result) {
        this.childKey = childKey;
        this.testResultTime = testResultTime;
        this.photo = photo;
        this.result = result;
    }

    protected TestResult(Parcel in) {
        key = in.readString();
        childKey = in.readString();
        testResultTime = in.readParcelable(TestResultTime.class.getClassLoader());
        result = in.readInt();
    }

    public static final Creator<TestResult> CREATOR = new Creator<TestResult>() {
        @Override
        public TestResult createFromParcel(Parcel in) {
            return new TestResult(in);
        }

        @Override
        public TestResult[] newArray(int size) {
            return new TestResult[size];
        }
    };

    @Exclude
    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getChildKey() {
        return this.childKey;
    }

    public void setChildKey(String key) {
        this.childKey = key;
    }

    public TestResultTime getTestResultTime() {
        return testResultTime;
    }

    public void setTestResultTime(TestResultTime testResultTime) {
        this.testResultTime = testResultTime;
    }

    public Photo getPhoto() {
        return photo;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void setValues(TestResult testResult) {
        this.childKey = testResult.getChildKey();
        this.testResultTime = testResult.getTestResultTime();
        this.photo = testResult.getPhoto();
        this.result = testResult.getResult();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(childKey);
        dest.writeParcelable(testResultTime, flags);
        dest.writeInt(result);
    }
}
