package com.example.company.cookingtimer.services;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.company.cookingtimer.activities.MainActivity;
import com.example.company.cookingtimer.models.Timer;
import com.example.company.cookingtimer.models.ViewContainer;

/**
 *
 * This manages which TimerService will be used.
 *
 */

public class ServiceManager {

    private static final String TAG = "ServiceManager";

    public static final String TIMER_LENGTH = "timer-length";
    public static final String TIMER_TITLE = "timer-title";

    private Context context;
    private Timer timer;
    private ViewContainer viewContainer;

    public ServiceManager(Context context, Timer timer, ViewContainer viewContainer){
        this.context = context;
        this.timer = timer;
        this.viewContainer = viewContainer;
    }

    public void startOpenService(){

        TimerService timerService = new TimerService();
        TimerService2 timerService2 = new TimerService2();

        MainActivity mainActivity = new MainActivity();
        if (!timerService.isServiceRunning()){
            Intent intent = new Intent(context, TimerService.class);

            String timerName = timer.getTimerName();
            int timeInMillis = timer.getTimeInMillis();

            intent.putExtra(TIMER_TITLE, timerName);
            intent.putExtra(TIMER_LENGTH, timeInMillis);

            mainActivity.testFunctionality(viewContainer.getDonutProgressView(),
                    viewContainer.getTimeTextView(), timeInMillis, 1);

            context.startService(intent);
        } else if (!timerService2.isServiceRunning()){
            Intent intent = new Intent(context, TimerService2.class);
            int timeInMillis = timer.getTimeInMillis();
            String timerName = timer.getTimerName();

            intent.putExtra(TIMER_TITLE, timerName);
            intent.putExtra(TIMER_LENGTH, timeInMillis);

            mainActivity.testFunctionality(viewContainer.getDonutProgressView(),
                    viewContainer.getTimeTextView(), timeInMillis, 2);

            context.startService(intent);
        } else{
            Toast.makeText(context, "Only two timers supported now", Toast.LENGTH_SHORT).show();
        }
    }
}
