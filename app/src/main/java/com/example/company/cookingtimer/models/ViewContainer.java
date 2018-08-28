package com.example.company.cookingtimer.models;

import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

public class ViewContainer {

    private DonutProgress mDonutProgress;
    private TextView mTimeTextView;
    private long mTimerId;

    public ViewContainer(DonutProgress donutProgress, TextView timeTextView, long timerId){
        mDonutProgress = donutProgress;
        mTimeTextView = timeTextView;
        mTimerId = timerId;
    }

    public DonutProgress getDonutProgressView() {
        return mDonutProgress;
    }

    public TextView getTimeTextView() {
        return mTimeTextView;
    }

    public long getmTimerId(){
        return mTimerId;
    }
}
