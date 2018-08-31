package com.example.company.cookingtimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

import com.example.company.cookingtimer.fragments.TimerFragment;
import com.example.company.cookingtimer.utils.TimerUtils;
import com.github.lzyzsd.circleprogress.DonutProgress;

/**
 *
 * This class acts as a bridge to the Service(s) & TimerFragment's UI.
 *
 */

public class ServiceBridge {

    private static ServiceBridge serviceBridgeInstance;

    TimerUtils timerUtils;

    private BroadcastReceiver broadcastReceiver;
    public BridgeToService1 bridgeToService1;
    public BridgeToService2 bridgeToService2;



    public static ServiceBridge getInstance(){
        if (serviceBridgeInstance == null){
            serviceBridgeInstance = new ServiceBridge();
        }
        return serviceBridgeInstance;
    }

    /**
     * This updates the UI for TimerService1
     */
    public static class BridgeToService1 extends TimerFragment{

        DonutProgress progress1;
        TextView timeTextView1;
        long timerLengthInMilliSeconds1;

        public void BridgeToService1(Context context, DonutProgress progress, TextView timeTextView,
                                     long timerLengthInMilliSeconds){
            progress1 = progress;
            timeTextView1 = timeTextView;
            timerLengthInMilliSeconds1 = timerLengthInMilliSeconds;
            progress.setMax((int)timerLengthInMilliSeconds);

        }

        /**
         * updateViews is called from the localBroadcastReceiver() to
         * update the views
         * @param timeLeftInMillis
         */
        private void updateViews(long timeLeftInMillis) {
            if (progress1 != null && timeTextView1 != null){
                progress1.setProgress(timeLeftInMillis);
                timeTextView1.setText(formatTime((int)timeLeftInMillis));
            }
        }

        private String formatTime(int durationInMillis) {
            return TimerUtils.getFormattedTime(durationInMillis);
        }
    }

    /**
     * This updates the UI for TimerService2
     */
    public static class BridgeToService2 extends TimerFragment{

        DonutProgress progress1;
        TextView timeTextView1;
        long timerLengthInMilliSeconds1;

        public void BridgeToService2(Context context, DonutProgress progress, TextView timeTextView,
                                     long timerLengthInMilliSeconds){
            progress1 = progress;
            timeTextView1 = timeTextView;
            timerLengthInMilliSeconds1 = timerLengthInMilliSeconds;
            progress.setMax((int)timerLengthInMilliSeconds);

        }

        /**
         * updateViews is called from the localBroadcastReceiver() to
         * update the views
         * @param timeLeftInMillis
         */
        private void updateViews(long timeLeftInMillis) {
            if (progress1 != null && timeTextView1 != null){
                progress1.setProgress(timeLeftInMillis);
                timeTextView1.setText(formatTime((int)timeLeftInMillis));
            }
        }

        private String formatTime(int durationInMillis) {
            return TimerUtils.getFormattedTime(durationInMillis);
        }
    }

    /**
     *
     * @param context
     * @param progress
     * @param timeTextView
     * @param timerLengthInMilliSeconds
     * @param stringIntentFilter
     */
    public void bindServiceAndUI(Context context, DonutProgress progress,
                               TextView timeTextView, long timerLengthInMilliSeconds, String stringIntentFilter) {
        if (stringIntentFilter.equals("service-1")){
            bridgeToService1 = new BridgeToService1();
            bridgeToService1.BridgeToService1(context, progress, timeTextView,
                    timerLengthInMilliSeconds);
        } else {
            bridgeToService2 = new BridgeToService2();
            bridgeToService2.BridgeToService2(context, progress, timeTextView,
                    timerLengthInMilliSeconds);
        }

        timerUtils = TimerUtils.getInstance();


        localBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("service-1");
        intentFilter.addAction("service-2");
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, intentFilter);
    }


    private void localBroadcastReceiver() {

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = "";
                if (intent.getAction() != null){
                    action = intent.getAction();
                }
                long timeLeftInMillis;
                switch (action){
                    case "service-1":
                        timeLeftInMillis = intent.getLongExtra("id", 0);
                        bridgeToService1.updateViews(timeLeftInMillis);
                        break;
                    case "service-2":
                        timeLeftInMillis = intent.getLongExtra("id", 0);
                        bridgeToService2.updateViews(timeLeftInMillis);
                        break;
                }
            }
        };
    }
}

