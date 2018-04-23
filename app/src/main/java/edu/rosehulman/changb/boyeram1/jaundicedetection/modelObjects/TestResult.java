package edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by changb on 4/22/2018.
 */

public class TestResult implements Parcelable {

    private String key;
    private int percentage;
    private TestResultTime testResultTime;

    protected TestResult(Parcel in) {
        key = in.readString();
        percentage = in.readInt();
        testResultTime = in.readParcelable(TestResultTime.class.getClassLoader());
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
        dest.writeInt(percentage);
        dest.writeParcelable(testResultTime, flags);
    }
}
