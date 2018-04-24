package edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Created by changb on 4/22/2018.
 */

public class TestResult {

    private String key;
    private TestResultTime testResultTime;
    private Photo eyePhoto;
    private Photo palmPhoto;
    private Photo footPhoto;
    private int result;


    public TestResult(TestResultTime testResultTime, Photo eyePhoto, Photo palmPhoto, Photo footPhoto, int result) {
        this.testResultTime = testResultTime;
        this.eyePhoto = eyePhoto;
        this.palmPhoto = palmPhoto;
        this.footPhoto = footPhoto;
        this.result = result;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public TestResultTime getTestResultTime() {
        return testResultTime;
    }

    public void setTestResultTime(TestResultTime testResultTime) {
        this.testResultTime = testResultTime;
    }

    public Photo getEyePhoto() {
        return eyePhoto;
    }

    public void setEyePhoto(Photo eyePhoto) {
        this.eyePhoto = eyePhoto;
    }

    public Photo getPalmPhoto() {
        return palmPhoto;
    }

    public void setPalmPhoto(Photo palmPhoto) {
        this.palmPhoto = palmPhoto;
    }

    public Photo getFootPhoto() {
        return footPhoto;
    }

    public void setFootPhoto(Photo footPhoto) {
        this.footPhoto = footPhoto;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void setValues(TestResult testResult) {
        this.testResultTime = testResult.getTestResultTime();
        this.eyePhoto = testResult.getEyePhoto();
        this.palmPhoto = testResult.getPalmPhoto();
        this.footPhoto = testResult.getFootPhoto();
        this.result = testResult.getResult();
    }
}
