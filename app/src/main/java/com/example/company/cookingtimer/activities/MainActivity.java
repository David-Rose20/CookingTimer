package com.example.company.cookingtimer.activities;

import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.company.cookingtimer.R;
import com.example.company.cookingtimer.adapters.TimerAdapter;
import com.example.company.cookingtimer.adapters.ViewPagerAdapter;
import com.example.company.cookingtimer.fragments.TimerFragment;
import com.example.company.cookingtimer.models.Timer;
import com.example.company.cookingtimer.services.TimerService2;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.text.SimpleDateFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ViewPager viewPager;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0f);

        initializeViews();
        setupAdapters();
//        localBroadcastReceiver();
//        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("custom"));
    }



    private void initializeViews() {
        viewPager = findViewById(R.id.view_pager);
    }

    private void setupAdapters() {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

//    private void localBroadcastReceiver(){
//
//        broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                long id = intent.getLongExtra("service-1", 0);
//                Log.d(TAG, "onReceive: id: " + id);
//            }
//        };
//    }

    public class Bridge extends TimerAdapter{

        public Bridge(@NonNull Context context, List<Timer> timerList) {
            super(context, timerList);
        }

        @Override
        public long getItemId(int position) {
            Timer timer = getItem(position);
            Log.d(TAG, "getItemId: " + timer.getTimeInMillis());
            return super.getItemId(position);

        }
    }

    /**
     * Testing Service & UI communication
     *
     * TODO will be improved as I learn more.
     */
    public void testFunctionality(final DonutProgress progress,
                                  final TextView timeTextView,
                                  final int timerLengthInMilliSeconds,
                                  int id) {


        progress.setMax(timerLengthInMilliSeconds);

        switch (id) {

            case 1:
//                final TimerService timerService = new TimerService();
//                new Thread() {
//                    public void run() {
//                        do {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    float progressRemaining = (float) timerLengthInMilliSeconds - timerService.getTimeRemainingFromService();
//                                    progress.setProgress(progressRemaining);
//
//                                    int timeRemaining = (int) timerService.getTimeRemainingFromService()/1000;
//                                    timeTextView.setText(getFormattedTime(timeRemaining));
//                                }
//                            });
//                            threadSleep(300);
//                        }
//                        while (timerService.isServiceRunning());
//
//                    }
//                }.start();
                break;

            case 2:
                final TimerService2 timerService2 = new TimerService2();
                new Thread() {
                    public void run() {
                        do {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    float progressRemaining = (float) timerLengthInMilliSeconds - timerService2.getTimeRemainingFromService();
                                    progress.setProgress(progressRemaining);

                                    int timeRemaining = (int) timerService2.getTimeRemainingFromService()/1000;
                                    timeTextView.setText(formatTime(timeRemaining));
                                }
                            });
                            threadSleep(300);
                        }
                        while (timerService2.isServiceRunning());

                    }
                }.start();
                break;
        }
    }

    private void threadSleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Log.d(TAG, "threadSleep: ", e);
        }
    }

    private String formatTime(int timeLeft){
        if (timeLeft <= 9 && timeLeft > 1){
            return ("00:" + "00:" + "0" + timeLeft);
        } else if (timeLeft >= 10){
            return ("00:" + "00:" + timeLeft);
        } else {
            return ("00:" + "00:" + "00");
        }
    }

    //    @Override
//    protected void onDestroy() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
//        super.onDestroy();
//    }
}