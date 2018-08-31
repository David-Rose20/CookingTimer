package com.example.company.cookingtimer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.company.cookingtimer.adapters.TimerRecyclerAdapter;
import com.example.company.cookingtimer.models.Timer;
import com.example.company.cookingtimer.services.ServiceManager;
import com.example.company.cookingtimer.services.TimerService;
import com.example.company.cookingtimer.services.TimerService2;
import com.github.lzyzsd.circleprogress.DonutProgress;

public class ApplicationTimer {

    private Context mContext;
    ServiceBridge serviceBridge;

    public ApplicationTimer(Context context){
        mContext = context;
        serviceBridge = ServiceBridge.getInstance();
    }

    public void startTimer(View view, Timer timer) {
        ServiceManager serviceManager = new ServiceManager(mContext);
        serviceManager.startAvailableService(timer);
        String serviceAction = serviceManager.getServiceAction();
        bindNewServiceWithUI(view, timer, serviceAction);
    }

    private void bindNewServiceWithUI(View view, Timer timer, String serviceAction){

        DonutProgress donutProgress = view.findViewById(R.id.timer_donut_progress);
        TextView timeTextView = view.findViewById(R.id.timer_time_left_text);

        serviceBridge.bindServiceAndUI(mContext, donutProgress,
                timeTextView, timer.getTimeInMillis(), serviceAction);
    }

    public void testingHolder(TimerRecyclerAdapter.ViewHolder holder){
        TimerService timerService = new TimerService();
        String timerOneName = timerService.getTimerName();

        TimerService2 timerService2 = new TimerService2();
        String timerTwoName = timerService2.getTimerName();

        View view = holder.itemView;
        TextView textView = view.findViewById(R.id.timer_title);

        if (textView.getText().equals(timerOneName)){
            DonutProgress donutProgress = view.findViewById(R.id.timer_donut_progress);
            TextView timeTextView = view.findViewById(R.id.timer_time_left_text);

            serviceBridge.bindServiceAndUI(mContext, donutProgress,
                    timeTextView, timerService.getTimerLengthMillis(), "service-1");
        }
        if (textView.getText().equals(timerTwoName)){

            DonutProgress donutProgress = view.findViewById(R.id.timer_donut_progress);
            TextView timeTextView = view.findViewById(R.id.timer_time_left_text);

            serviceBridge.bindServiceAndUI(mContext, donutProgress,
                    timeTextView, timerService2.getTimerLengthMillis(), "service-2");
        }
    }

    public void startServiceBridge(RecyclerView recyclerView, int position, String serviceAction){

        View view = recyclerView.findViewHolderForAdapterPosition(position).itemView;

        DonutProgress donutProgress = view.findViewById(R.id.timer_donut_progress);
        TextView timeTextView = view.findViewById(R.id.timer_time_left_text);

        TimerService timerService = new TimerService();
        serviceBridge.bindServiceAndUI(mContext, donutProgress,
                timeTextView, timerService.getTimerLengthMillis(), serviceAction);
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
