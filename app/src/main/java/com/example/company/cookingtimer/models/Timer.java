package com.example.company.cookingtimer.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

@Entity(tableName = "timer")
public class Timer {

    @PrimaryKey
    @NonNull
    private String mTimerName;

    private int mTimeInMillis;

    public void setTimerName(String timerName){
        mTimerName = timerName;
    }

    public void setTimeInMillis(int timeInMillis){
        mTimeInMillis = timeInMillis;
    }

    @NonNull
    public String getTimerName(){
        return mTimerName;
    }

    public int getTimeInMillis() {
        return mTimeInMillis;
    }

}
