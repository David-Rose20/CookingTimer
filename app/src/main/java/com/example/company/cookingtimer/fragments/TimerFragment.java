package com.example.company.cookingtimer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.company.cookingtimer.MainTimer;
import com.example.company.cookingtimer.R;
import com.example.company.cookingtimer.adapters.TimerAdapter;
import com.example.company.cookingtimer.models.Timer;
import com.example.company.cookingtimer.models.ViewContainer;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFragment extends Fragment {

    ListView timerListView;
    List<Timer> timerList;
    View emptyView;

    private static final String TAG = "TimerFragment";

    public TimerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_view_layout, container, false);

        initializeViews(rootView);

        timerListView.setEmptyView(emptyView);

        timerList = new ArrayList<>();
        timerList.add(new Timer("Timer title 1", 10000,
                R.drawable.food_placeholder,1));

        timerList.add(new Timer("Timer title 2", 20000,
                R.drawable.pasta_placeholder, 2));

        TimerAdapter timerAdapter = new TimerAdapter(getContext(), timerList);
        timerListView.setAdapter(timerAdapter);

        timerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: ");
                Timer currentTimer = timerList.get(position);
                MainTimer mainTimer = new MainTimer(getContext());
                ViewContainer viewContainer = createViewContainer(view, currentTimer.getTimerId());
                mainTimer.startTimer(currentTimer, viewContainer);
            }
        });

        return rootView;
    }

    private ViewContainer createViewContainer(View view, int viewId){
        DonutProgress donutProgress = view.findViewById(R.id.timer_donut_progress);
        TextView timeLeftText = view.findViewById(R.id.timer_time_left_text);

        return new ViewContainer(donutProgress, timeLeftText, viewId);
    }

    private void initializeViews(View rootView){
        timerListView = rootView.findViewById(R.id.list_view);
        emptyView = rootView.findViewById(R.id.empty_timer_view);
    }
}
