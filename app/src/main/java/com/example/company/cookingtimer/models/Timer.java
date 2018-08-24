package com.example.company.cookingtimer.models;

public class Timer {

    private String mTimerName;
    private int mTimeInMillis;
    private int mTimerId;
    private int mImageResourceId;

    public Timer(String timerName, int timeInMillis, int imageResourceId, int timerId){
        mTimerName = timerName;
        mTimeInMillis = timeInMillis;
        mImageResourceId = imageResourceId;
        mTimerId = timerId;
    }

    public String getTimerName(){
        return mTimerName;
    }

    public int getTimeInMillis() {
        return mTimeInMillis;
    }

    public int getTimerId() {
        return mTimerId;
    }

    public int getImageResourceId(){
        return mImageResourceId;
    }
}
