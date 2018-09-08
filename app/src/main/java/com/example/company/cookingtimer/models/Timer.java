package com.example.company.cookingtimer.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.List;

@Entity(tableName = "timer")
public class Timer {

    @PrimaryKey
    @NonNull
    private String mTimerName;

    private int mTimeInMillis;

    private String mTimerImageUriString;

    public void setTimerName(String timerName){
        mTimerName = timerName;
    }

    public void setTimeInMillis(int timeInMillis){
        mTimeInMillis = timeInMillis;
    }

    public void setTimerImageUriString(String timerImageUri){
        mTimerImageUriString = timerImageUri;
    }

    @NonNull
    public String getTimerName(){
        return mTimerName;
    }

    public int getTimeInMillis() {
        return mTimeInMillis;
    }

    public String getTimerImageUriString(){
        return mTimerImageUriString;
    }
}
