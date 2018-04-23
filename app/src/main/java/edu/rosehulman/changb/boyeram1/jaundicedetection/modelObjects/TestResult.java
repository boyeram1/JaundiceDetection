package edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by changb on 4/22/2018.
 */

public class TestResult implements Parcelable {

    private String key;
    private String childName;
    private int percentage;
    private TestResultTime testResultTime;

    protected TestResult(Parcel in) {
        this.key = in.readString();
        this.childName = in.readString();
        this.percentage = in.readInt();
        this.testResultTime = in.readParcelable(TestResultTime.class.getClassLoader());
    }

    public TestResult(String key, String childName, int percentage, TestResultTime testResultTime) {
        this.key = key;
        this.childName = childName;
        this.percentage = percentage;
        this.testResultTime = testResultTime;
    }

    public String getChildName() {
        return childName;
    }

    public int getPercentage() {
        return percentage;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(childName);
        dest.writeInt(percentage);
        dest.writeParcelable(testResultTime, flags);
    }
}
