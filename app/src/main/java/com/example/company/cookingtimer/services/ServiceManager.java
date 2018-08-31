package com.example.company.cookingtimer.services;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.company.cookingtimer.models.Timer;

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
    String serviceAction;


    public ServiceManager(Context context) {
        this.context = context;
    }

    public void startAvailableService(Timer timer) {

        TimerService timerService = new TimerService();
        TimerService2 timerService2 = new TimerService2();

        if (!timerService.isServiceRunning()) {
            Intent intent = new Intent(context, TimerService.class);

            String timerName = timer.getTimerName();
            int timeInMillis = timer.getTimeInMillis();

            intent.putExtra(TIMER_TITLE, timerName);
            intent.putExtra(TIMER_LENGTH, timeInMillis);

            context.startService(intent);
            setServiceAction("service-1");
            // TODO manage timers
        } else if (!timerService2.isServiceRunning()) {
            Intent intent = new Intent(context, TimerService2.class);

            String timerName = timer.getTimerName();
            int timeInMillis = timer.getTimeInMillis();

            intent.putExtra(TIMER_TITLE, timerName);
            intent.putExtra(TIMER_LENGTH, timeInMillis);

            Log.d(TAG, "startOpenService: ");

            context.startService(intent);
            setServiceAction("service-2");
        } else {
            Toast.makeText(context, "Only one timer supported now", Toast.LENGTH_SHORT).show();
        }
    }

    private void setServiceAction(String action){
        serviceAction = action;
    }

    public String getServiceAction(){
        return serviceAction;
    }


    public static boolean isServiceAvailable(){
        TimerService timerService = new TimerService();
        TimerService2 timerService2 = new TimerService2();
        if (timerService.isServiceRunning() && timerService2.isServiceRunning()){
            return false;
        } else {
            return true;
        }
    }
}
