package com.example.company.cookingtimer;

import android.content.Context;
import android.view.View;

import com.example.company.cookingtimer.models.Timer;
import com.example.company.cookingtimer.models.ViewContainer;
import com.example.company.cookingtimer.services.ServiceManager;

public class MainTimer {

    private Context mContext;

    public MainTimer(Context context){
        mContext = context;
    }

    public void startTimer(Timer timer, ViewContainer viewContainer){
        ServiceManager serviceGateKeeper = new ServiceManager(mContext, timer, viewContainer);
        serviceGateKeeper.startOpenService();
    }

    public void stopTimer(){

    }

    public void resumeTimer(){
        // TODO instead of getting timer.getTimeInMillis use paused value from database.
    }

    public void pauseTimer(){
        // TODO pause timer and save time in database to use in resumeTimer();
    }
}
