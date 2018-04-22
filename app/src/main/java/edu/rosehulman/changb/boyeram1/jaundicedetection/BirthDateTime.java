package edu.rosehulman.changb.boyeram1.jaundicedetection;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by boyeram1 on 4/15/2018.
 */

public class BirthDateTime implements Parcelable {

    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;

    public BirthDateTime() {
        // Needed for JSON serialization
    }

    protected BirthDateTime(Parcel in) {
        day = in.readInt();
        month = in.readInt();
        year = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
    }

    public static final Creator<BirthDateTime> CREATOR = new Creator<BirthDateTime>() {
        @Override
        public BirthDateTime createFromParcel(Parcel parcel) {
            return new BirthDateTime(parcel);
        }

        @Override
        public BirthDateTime[] newArray(int i) {
            return new BirthDateTime[i];
        }
    };

    public BirthDateTime(int day, int month, int year, int hour, int minute) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }

    public BirthDateTime(String monthDayYearText, String hourMinuteText) {
        String[] monthDayYear = monthDayYearText.split("/");
        this.day = Integer.parseInt(monthDayYear[0]);
        this.month = Integer.parseInt(monthDayYear[1]);
        this.year = Integer.parseInt(monthDayYear[2]);

        String[] hourMinute = hourMinuteText.split(":");
        this.hour = Integer.parseInt(hourMinute[0]);
        this.minute = Integer.parseInt(hourMinute[1]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(day);
        parcel.writeInt(month);
        parcel.writeInt(year);
        parcel.writeInt(hour);
        parcel.writeInt(minute);
    }

    public String dateToString() {
        return String.format("%d/%d/%d", day, month, year);
    }

    public String timeToString() {
        if(minute < 10) {
            return String.format("%d:0%d", hour, minute);
        } else {
            return String.format("%d:%d", hour, minute);
        }
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
