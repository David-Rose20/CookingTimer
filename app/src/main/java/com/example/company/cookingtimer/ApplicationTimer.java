package com.example.company.cookingtimer;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.company.cookingtimer.models.Timer;
import com.example.company.cookingtimer.services.TimerService;
import com.github.lzyzsd.circleprogress.DonutProgress;

public class ApplicationTimer {

    private static final String TAG = "ApplicationTimer";
    public static final String TIMER_LENGTH = "timer-length";
    public static final String TIMER_TITLE = "timer-title";
    public static final String TIMER_IMAGE_URI = "timer-image-uri";

    private Context mContext;
    ServiceBridge serviceBridge;

    public ApplicationTimer(Context context){
        mContext = context;
        serviceBridge = ServiceBridge.getInstance();
    }

    public void startTimer(View view, Timer currentTimer) {
        Intent intent = new Intent(mContext, TimerService.class);

        String timerName = currentTimer.getTimerName();
        int timeInMillis = currentTimer.getTimeInMillis();
        String imageUri = currentTimer.getTimerImageUriString();

        intent.putExtra(TIMER_TITLE, timerName);
        intent.putExtra(TIMER_LENGTH, timeInMillis);
        intent.putExtra(TIMER_IMAGE_URI, imageUri);

        bindNewServiceWithUI(view, currentTimer);
        mContext.startService(intent);
    }

    private void bindNewServiceWithUI(View view, Timer currentTimer){

        String serviceAction = currentTimer.getTimerName();

        DonutProgress donutProgress = view.findViewById(R.id.timer_donut_progress);
        TextView timeTextView = view.findViewById(R.id.timer_time_left_text);

        serviceBridge.bindServiceAndUI(mContext, donutProgress,
                timeTextView, currentTimer.getTimeInMillis(), serviceAction);
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
