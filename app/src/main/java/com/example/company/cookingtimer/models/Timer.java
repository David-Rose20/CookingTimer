package com.example.company.cookingtimer.models;

public class Timer {

    private String mTimerName;

    public Timer(String timerName){
        mTimerName = timerName;
    }

    public String getTimerName(){
        return mTimerName;
    }
}
