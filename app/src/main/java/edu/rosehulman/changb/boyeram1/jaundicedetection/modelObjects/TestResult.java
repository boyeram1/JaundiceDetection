package edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects;

import com.google.firebase.database.Exclude;

import com.google.firebase.database.Exclude;

/**
 * Created by changb on 4/22/2018.
 */

public class TestResult {

    private String key;
    private String childKey;
    private TestResultTime testResultTime;
    private Photo photo;
    private int result;

    public TestResult() {}
<<<<<<< HEAD

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
=======

    public TestResult(String childKey, TestResultTime testResultTime, Photo photo, int result) {
        this.childKey = childKey;
        this.testResultTime = testResultTime;
        this.photo = photo;
        this.result = result;
    }

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
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121
    }

    public Photo getPhoto() {
        return photo;
    }

    public int getResult() {
        return result;
    }

<<<<<<< HEAD
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
=======
    public void setResult(int result) {
        this.result = result;
    }

    public void setValues(TestResult testResult) {
        this.childKey = testResult.getChildKey();
        this.testResultTime = testResult.getTestResultTime();
        this.photo = testResult.getPhoto();
        this.result = testResult.getResult();
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121
    }
}
