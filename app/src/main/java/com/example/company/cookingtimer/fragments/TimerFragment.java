package com.example.company.cookingtimer.fragments;


import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.company.cookingtimer.activities.AddTimerActivity;
import com.example.company.cookingtimer.interfaces.TimerClickInterface;
import com.example.company.cookingtimer.adapters.TimerRecyclerAdapter;
import com.example.company.cookingtimer.room.AppDatabase;
import com.example.company.cookingtimer.ApplicationTimer;
import com.example.company.cookingtimer.R;
import com.example.company.cookingtimer.models.Timer;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFragment extends Fragment {

    private View emptyView;
    private TimerRecyclerAdapter recyclerAdapter;

    private RecyclerView recyclerView;
    public List<Timer> dbTimerList;

    private static final String TAG = "TimerFragment";

    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_view_layout,
                container, false);

        initializeViews(rootView);
        setupAddTimerFab(rootView);
        setDbTimerList();


        return rootView;
    }

    private void initializeViews(View rootView) {
        emptyView = rootView.findViewById(R.id.empty_timer_view);
        recyclerView = rootView.findViewById(R.id.recycler_view);
    }

    private void setDbTimerList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase database = Room.databaseBuilder(getContext(), AppDatabase.class, "db-timers")
                        .build();
                dbTimerList = database.getMyTimerDAO().getMyTimers();
                createRecyclerAdapter();
                setupRecyclerView();
            }
        }).start();
    }


    private void setupAddTimerFab(View rv) {
        FloatingActionButton addTimerButton = rv.findViewById(R.id.button_add_timer);
        addTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddTimerActivity.class));
            }
        });
    }

    private void createRecyclerAdapter() {
        recyclerAdapter = new TimerRecyclerAdapter(getContext(), dbTimerList, new TimerClickInterface() {
            @Override
            public void onItemClicked(View view, int position) {
                Timer currentTimer = dbTimerList.get(position);

                ApplicationTimer applicationTimer = new ApplicationTimer(getContext());
                applicationTimer.startTimer(view, currentTimer);
            }
        });
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        if (recyclerAdapter.getItemCount() > 0) {
            emptyView.setVisibility(View.GONE);
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0){
                    Log.d(TAG, "onScrolled: dy > 0");

                }
                if (dy < 0){
                    Log.d(TAG, "onScrolled: dy < 0");
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}
