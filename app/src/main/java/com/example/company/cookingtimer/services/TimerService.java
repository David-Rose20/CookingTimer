package com.example.company.cookingtimer.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.company.cookingtimer.ApplicationTimer;
import com.example.company.cookingtimer.R;
import com.example.company.cookingtimer.utils.TimerUtils;
import com.example.company.cookingtimer.activities.MainActivity;
import com.example.company.cookingtimer.broadcastReceivers.TimerBroadcastReceiver;

import static com.example.company.cookingtimer.baseApp.BaseApp.CHANNEL_ID;


public class TimerService extends Service {

    private static final String TAG = "TimerService";
    public static final String TIME_REMAINING_KEY = "time-remaining";
    private static int numberOfTimers = 0;

    private LocalBroadcastManager localBroadcastManager;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;


    @Override
    public void onCreate() {
        super.onCreate();
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplication());
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this, CHANNEL_ID);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int timerLengthMillis = intent.getIntExtra(ApplicationTimer.TIMER_LENGTH, 0);
        String timerName = intent.getStringExtra(ApplicationTimer.TIMER_TITLE);
        String timerImageUri = intent.getStringExtra(ApplicationTimer.TIMER_IMAGE_URI);

        numberOfTimers++;
        createNotification(timerName, timerLengthMillis, timerImageUri, startId);

        return START_NOT_STICKY;
    }

    private void createNotification(String timerName, int timerLengthMillis,
                                    String timerImageUri, int startId) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        // If this is the first timer it will create the foreground service.
        if (numberOfTimers <= 1) {

            NotificationCompat.Builder builder1 = new NotificationCompat.Builder(this, CHANNEL_ID);

            Notification notificationSummary = builder1
                    .setColor(getResources().getColor(R.color.blueGreyColor))
                    .setSmallIcon(R.drawable.ic_action_clock)
                    /*.setStyle(new NotificationCompat.InboxStyle()
                            .addLine(timerName + " running"))*/
                    .setGroup("timer_group")
                    .setGroupSummary(true)
                    .setOnlyAlertOnce(true)
                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(1001, notificationSummary);
            createCountDownTimer(timerLengthMillis, timerName, timerImageUri, startId);

            // else it creates a notification that will group with the above
            // foreground notification (notificationSummary).
        } else {

            Log.d(TAG, "createNotification: else ");
            Glide.with(this)
                    .asBitmap()
                    .load(timerImageUri)
                    .apply(new RequestOptions().override(120))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            builder.setLargeIcon(resource);
                        }
                    });

            builder
                    .setColor(getResources().getColor(R.color.blueGreyColor))
                    .setSmallIcon(R.drawable.ic_action_clock)
                    .setContentTitle(timerName)
                    .setContentIntent(pendingIntent)
                    .setGroup("timer_group")
                    .setOnlyAlertOnce(true)
                    .build();

            notificationManager.notify(startId, builder.build());
            createCountDownTimer(timerLengthMillis, timerName, timerImageUri, startId);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void createCountDownTimer(final long timerLengthInMillis,
                                      final String timerName,
                                      final String timerImageUri,
                                      final int startId) {
        new CountDownTimer(timerLengthInMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                updateNotificationContent(timerName, (int) millisUntilFinished, timerImageUri,  startId);

                Intent intent = new Intent(timerName);
                intent.putExtra(TIME_REMAINING_KEY, millisUntilFinished);
                localBroadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onFinish() {
                numberOfTimers--;
                finish(timerName, startId);
                if (numberOfTimers <= 0) {
                    stopForeground(true);
                    stopSelf();
                }
            }
        }.start();
    }


    private void finish(String timerName, int startId) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pasta_placeholder);
        Notification notification1 = builder
                .setColor(getResources().getColor(R.color.blueGreyColor))
                .setSmallIcon(R.drawable.ic_action_clock)
                .setContentTitle(timerName)
                .setContentText("Timer finished!")
                .setGroup("timer_group")
                .setLargeIcon(bitmap)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(startId, notification1);

        Intent broadcastIntent = new Intent(this, TimerBroadcastReceiver.class);
        sendBroadcast(broadcastIntent);

    }

    /**
     * This updates the notification's time every second.
     *
     * @param timerName             Timer's name
     * @param durationInMillis      Time in millis that gets converted using the TimerUtils.
     * @param startId               The startId is used to know which notification to update.
     *                              This is a unique id from onStartCommand()
     *                              ~Each time the service is called it is incremented.
     *                              E.g 30th time called value will be 30.
     */
    private void updateNotificationContent(String timerName, int durationInMillis,
                                           String timerImageUri, int startId) {
        String formattedTime = TimerUtils.getFormattedTime(durationInMillis);


        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        builder
                .setColor(getResources().getColor(R.color.blueGreyColor))
                .setSmallIcon(R.drawable.ic_action_clock)
                .setContentTitle(timerName)
                .setContentText(formattedTime)
                .setGroup("timer_group")
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .build();

        Glide.with(this)
                .asBitmap()
                .load(timerImageUri)
                .apply(new RequestOptions().override(120))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        builder.setLargeIcon(resource);
                    }
                });

        notificationManager.notify(startId, builder.build());
    }

}
