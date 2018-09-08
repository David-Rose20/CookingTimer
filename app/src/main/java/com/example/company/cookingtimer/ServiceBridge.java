package com.example.company.cookingtimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.TextView;

import com.example.company.cookingtimer.adapters.TimerRecyclerAdapter;
import com.example.company.cookingtimer.models.Timer;
import com.example.company.cookingtimer.services.TimerService;
import com.example.company.cookingtimer.utils.TimerUtils;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;
import java.util.List;

/**
 * This class acts as a bridge to the Service(s) & TimerFragment's UI.
 */

public class ServiceBridge {

    private static ServiceBridge serviceBridgeInstance;
    private static List<ViewClass> viewClassList;

    TimerUtils timerUtils;
    static IntentFilter intentFilter;

    private BroadcastReceiver broadcastReceiver;
    public BridgeToService1 bridgeToService1;


    public static ServiceBridge getInstance(){
        if (serviceBridgeInstance == null){
            serviceBridgeInstance = new ServiceBridge();
            intentFilter = new IntentFilter();
            viewClassList = new ArrayList<>();

        }
        return serviceBridgeInstance;
    }

    /**
     *This model acts as a View container.
     * This is needed to keep track of the UI and
     * assign views on orientation changes, re-opening, etc.
     */
    public class ViewClass {

        DonutProgress progress;
        TextView timeTextView;
        int timerLength;
        String action;

        public ViewClass(DonutProgress progress, TextView timeTextView, int timerLength, String action){
            this.progress = progress;
            this.timeTextView = timeTextView;
            this.timerLength = timerLength;
            this.action = action;
        }

        public void setProgress(DonutProgress progress){
            this.progress = progress;
        }

        public void setMaxProgressValue(int maxProgressValue){
            this.progress.setMax(maxProgressValue);
        }

        public void setTimeTextView(TextView timeTextView){
            this.timeTextView = timeTextView;
        }

        public void setTimerLength(int timerLength){
            this.timerLength = timerLength;
        }

        public void setAction(String action){
            this.action = action;
        }

        public DonutProgress getProgress() {
            return progress;
        }

        public TextView getTimeTextView() {
            return timeTextView;
        }

        public String getAction(){
            return action;
        }
    }

    /**
     * This updates the UI for all timers
     */
    public class BridgeToService1 {


        public void bridge(final DonutProgress progress, TextView timeTextView,
                           long timerLengthInMilliSeconds, String intentFilter) {

            progress.setMax((int) timerLengthInMilliSeconds);
            viewClassList.add(new ViewClass(progress, timeTextView, (int) timerLengthInMilliSeconds, intentFilter));
        }

        private void applyValuesToViews(String action, int timeLeftInMillis) {
            for (int i = 0; i < viewClassList.size(); i++) {
                String listAction = viewClassList.get(i).getAction();
                if (listAction.equals(action)) {
                    DonutProgress donutProgress = viewClassList.get(i).getProgress();
                    TextView textView = viewClassList.get(i).getTimeTextView();
                    updateViewWithHandler(donutProgress, textView, timeLeftInMillis);
                }
            }
        }
        }

        private void updateViewWithHandler(DonutProgress progress, TextView timeTextView, int timeRemaining) {
            progress.setProgress(timeRemaining);
            timeTextView.setText(formatTime(timeRemaining));
        }

        private String formatTime(int durationInMillis) {
            return TimerUtils.getFormattedTime(durationInMillis);
        }


    public void bindServiceAndUI(Context context, DonutProgress progress,
                               TextView timeTextView, long timerLengthInMilliSeconds, String newIntentFilter) {
        intentFilter.addAction(newIntentFilter);

            bridgeToService1 = new BridgeToService1();
            bridgeToService1.bridge(progress, timeTextView,
                    timerLengthInMilliSeconds, newIntentFilter);

        timerUtils = TimerUtils.getInstance();

        localBroadcastReceiver();
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, intentFilter);
    }


    private void localBroadcastReceiver() {

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = "";
                if (intent.getAction() != null) {
                    action = intent.getAction();
                }
                long timeLeftInMillis = intent.getLongExtra(TimerService.TIME_REMAINING_KEY, 0);
                bridgeToService1.applyValuesToViews(action, (int)timeLeftInMillis);
            }
        };
    }

    /**
     * This is called from the Recycler Adapter.
     * When scrolling views are recycled so by using the holder we can get the
     * list_item view and  the timer is also needed.
     * @param holder
     * @param timer
     */
    public void viewHolder(TimerRecyclerAdapter.ViewHolder holder, Timer timer){

        View itemView = holder.itemView;
        //
        String title = timer.getTimerName();
        for (int i = 0; i < viewClassList.size(); i++) {
            ViewClass currentViewItem = viewClassList.get(i);
            // getAction is the title of the timer
            String viewClassAction = currentViewItem.getAction();
            // if the current actions equals the itemView's title it's a match
            if (viewClassAction.equals(title)){
                // Since the views are destroyed after being recycled
                // we have to set new views to the currentViewItem from the itemView.
                DonutProgress newProgress = itemView.findViewById(R.id.timer_donut_progress);
                currentViewItem.setProgress(newProgress);

                TextView newTimeText = itemView.findViewById(R.id.timer_time_left_text);
                currentViewItem.setTimeTextView(newTimeText);

                currentViewItem.setTimerLength(timer.getTimeInMillis());

                currentViewItem.setMaxProgressValue(timer.getTimeInMillis());

                currentViewItem.setAction(timer.getTimerName());
            }
        }
    }
}

