package com.example.company.cookingtimer.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.example.company.cookingtimer.R;
import com.example.company.cookingtimer.utils.TimerUtils;
import com.example.company.cookingtimer.activities.MainActivity;
import com.example.company.cookingtimer.broadcastReceivers.TimerBroadcastReceiver;


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


    private static final int NOTIFICATION_ID = 10;
    private static boolean isServiceRunning = false;
    private static int timerLengthMillis;
    private static String timerName;

    private NotificationCompat.Builder builder;
    private Notification notification;
    private NotificationManager notificationManager;
    private LocalBroadcastManager localBroadcastManager;
    private TimerUtils timerUtils;


    @Override
    public void onCreate() {
        super.onCreate();
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplication());
        timerUtils = TimerUtils.getInstance();
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
//                timerLengthMillis = (int) millisUntilFinished;
                updateNotification((int)millisUntilFinished);
                Intent intent = new Intent("service-1");
                intent.putExtra("id", millisUntilFinished);
                localBroadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onFinish() {
                stopSelf();
            }
        }.start();
    }


    private void updateNotification(int durationInMillis) {
        String formattedTime = timerUtils.getFormattedTime(durationInMillis);
        builder.setContentText("" + formattedTime);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public boolean isServiceRunning(){
        return isServiceRunning;
    }

    public String getTimerName(){
        return timerName;
    }

    public int getTimerLengthMillis(){
        return timerLengthMillis;
    }
}
