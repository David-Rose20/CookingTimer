package com.example.company.cookingtimer.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "timer")
public class Timer {

    @PrimaryKey
    @NonNull
    private String mTimerName;

    private int mTimeInMillis;

//    private int mTimerId;
//    private int mImageResourceId;


//    public Timer(String timerName, int timeInMillis, int imageResourceId, int timerId){
//        mTimerName = timerName;
//        mTimeInMillis = timeInMillis;
//        mImageResourceId = imageResourceId;
//        mTimerId = timerId;
//    }

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

//    public int getTimerId() {
//        return mTimerId;
//    }

//    public int getImageResourceId(){
//        return mImageResourceId;
//    }
}
