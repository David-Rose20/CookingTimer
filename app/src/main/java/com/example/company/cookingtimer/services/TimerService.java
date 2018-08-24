package com.example.company.cookingtimer.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;

import com.example.company.cookingtimer.R;
import com.example.company.cookingtimer.activities.MainActivity;
import com.example.company.cookingtimer.broadcastReceivers.TimerBroadcastReceiver;
import com.example.company.cookingtimer.models.ViewContainer;
import com.github.lzyzsd.circleprogress.DonutProgress;


import static com.example.company.cookingtimer.baseApp.BaseApp.CHANNEL_ID;


/**
 *
 *
 *
 * NEEDS ATTENTION: basic implementation, will change in future.
 *
 *
 *
 */

public class TimerService extends Service {

    private static final String TAG = "TimerService";

    private static final int NOTIFICATION_ID = 10;
    private static boolean isServiceRunning = false;
    private static int timerLengthMillis;
    private static int timerLength;
    private static String timerName;

    private NotificationCompat.Builder builder;
    private Notification notification;
    private NotificationManager notificationManager;


    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        getIntentExtras(intent);
        createNotification();
        isServiceRunning = true;

        return START_NOT_STICKY;
    }

    private void getIntentExtras(Intent intent){
        timerLengthMillis = intent.getIntExtra(ServiceManager.TIMER_LENGTH, 0);
        timerName = intent.getStringExtra(ServiceManager.TIMER_TITLE);

    }

    private void createNotification(){

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = builder
                .setContentTitle(timerName)
                .setContentText("")
                .setSmallIcon(R.drawable.ic_action_clock)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(NOTIFICATION_ID, notification);

        createCountDownTimer(timerLengthMillis);
    }

    @Override
    public void onDestroy() {
        Intent broadcastIntent = new Intent(this, TimerBroadcastReceiver.class);
        sendBroadcast(broadcastIntent);
        isServiceRunning = false;
        super.onDestroy();
    }

    private void createCountDownTimer(final long timerLengthInMillis){
        new CountDownTimer(timerLengthInMillis, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                timerLengthMillis = (int) millisUntilFinished;
                int remainingTime = (int) millisUntilFinished/1000;
                updateNotification(remainingTime);

            }

            @Override
            public void onFinish() {
                stopSelf();
            }
        }.start();
    }

    private void updateNotification(long secondsLeft){
        if (secondsLeft <= 9){
            builder
                    .setContentText("Time left: " +  "00:" + "00:" + "0" + secondsLeft).build();
        }
        else {
            builder
                    .setContentText("Time left: " +  "00:" + "00:"  + secondsLeft).build();
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public boolean isServiceRunning(){
        return isServiceRunning;
    }

    public long getTimeRemainingFromService(){
        return timerLengthMillis;
    }
}
