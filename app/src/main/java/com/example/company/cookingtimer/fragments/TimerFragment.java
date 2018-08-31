package com.example.company.cookingtimer.fragments;


import android.app.Dialog;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.example.company.cookingtimer.ServiceBridge;
import com.example.company.cookingtimer.TimerClickInterface;
import com.example.company.cookingtimer.adapters.TimerRecyclerAdapter;
import com.example.company.cookingtimer.services.ServiceManager;
import com.example.company.cookingtimer.utils.TimerUtils;
import com.example.company.cookingtimer.room.AppDatabase;
import com.example.company.cookingtimer.ApplicationTimer;
import com.example.company.cookingtimer.R;
import com.example.company.cookingtimer.models.Timer;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFragment extends Fragment {

    View emptyView;
    static CustomDialogFragment dialogFragment;

    TimerRecyclerAdapter recyclerAdapter;
    TimerUtils timerUtils;

    RecyclerView recyclerView;
    ServiceBridge serviceBridge;
    List<Timer> dbTimerList;

    private static final String TAG = "TimerFragment";

    public TimerFragment() {
        // Required empty public constructor

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_view_layout,
                container, false);

        initializeViews(rootView);
        setupAddTimerFab(rootView);
        initializeDataBase();
        createRecyclerAdapter();
        setupRecyclerView();

        return rootView;
    }

    private void initializeViews(View rootView) {
        emptyView = rootView.findViewById(R.id.empty_timer_view);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        timerUtils = TimerUtils.getInstance();
        serviceBridge = ServiceBridge.getInstance();
    }

    private void initializeDataBase(){
        final AppDatabase database = Room.databaseBuilder(getContext(), AppDatabase.class, "db-timers")
                .allowMainThreadQueries()
                .build();

        dbTimerList = database.getMyTimerDAO().getMyTimers();
    }

    private void setupAddTimerFab(View rv) {
        FloatingActionButton addTimerButton = rv.findViewById(R.id.button_add_timer);
        addTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTimerDialog();
            }
        });
    }

    private void createRecyclerAdapter(){
        recyclerAdapter = new TimerRecyclerAdapter(getContext(), dbTimerList, new TimerClickInterface() {
            @Override
            public void onItemClicked(View view, int position) {
                if (ServiceManager.isServiceAvailable()) {
                    Timer currentTimer = dbTimerList.get(position);

                    ApplicationTimer applicationTimer = new ApplicationTimer(getContext());
                    applicationTimer.startTimer(view, currentTimer);
                }
            }
        });
    }

    private void setupRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        if (recyclerAdapter.getItemCount() > 0) {
            emptyView.setVisibility(View.GONE);
        }
    }

    private static String[] addArrayString(int numberOfArrayCounts, int incremental) {
        String[] arrayString = new String[numberOfArrayCounts];
        int x = 0;
        for (int i = 0; i <= numberOfArrayCounts - 1; i++) {
            arrayString[i] = Integer.toString(x);
            x += incremental;
        }
        return arrayString;
    }


    public static class CustomDialogFragment extends DialogFragment {
        public CustomDialogFragment() {
            //Empty
        }

        public void hideKeyboard() {
            View thisView = getActivity().getCurrentFocus();
            if (thisView != null) {
                InputMethodManager inputManager = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(thisView.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

        private void setupWheelPickers(View view) {
            // Setup Hourly wheel picker
            String[] wheelPickerHourlyValue = addArrayString(24, 1);

            NumberPicker wheelPickerHourly = view.findViewById(R.id.wheel_picker_hourly);
            wheelPickerHourly.setMinValue(0);
            wheelPickerHourly.setMaxValue(wheelPickerHourlyValue.length - 1);
            wheelPickerHourly.setDisplayedValues(wheelPickerHourlyValue);

            // Setup Minutes wheel picker
            String[] wheelPickerMinValue = addArrayString(60, 1);
            NumberPicker wheelPickerMin = view.findViewById(R.id.wheel_picker_min);
            wheelPickerMin.setMinValue(0);
            wheelPickerMin.setMaxValue(wheelPickerMinValue.length - 1);
            wheelPickerMin.setDisplayedValues(wheelPickerMinValue);

            // Setup Sec wheel picker
            String[] wheelPickerSecValue = addArrayString(12, 5);
            NumberPicker wheelPickerSec = view.findViewById(R.id.wheel_picker_sec);
            wheelPickerSec.setMinValue(0);
            wheelPickerSec.setMaxValue(wheelPickerSecValue.length - 1);
            wheelPickerSec.setDisplayedValues(wheelPickerSecValue);
        }

        private void addTimerName(View view) {
            EditText timerName = view.findViewById(R.id.name_of_timer);
            timerName.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (i == 66) {
                        hideKeyboard();
                    }
                    return true;
                }
            });
        }

        private void addTimerIcon(View view) {
            ImageView addTimerIcon = view.findViewById(R.id.button_add_timer);
            final EditText timerName = view.findViewById(R.id.name_of_timer);
            final NumberPicker wheelPickerSec = view.findViewById(R.id.wheel_picker_sec);
            final NumberPicker wheelPickerMin = view.findViewById(R.id.wheel_picker_min);
            final NumberPicker wheelPickerHour = view.findViewById(R.id.wheel_picker_hourly);
            addTimerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveToDatabase(timerName, wheelPickerSec, wheelPickerMin, wheelPickerHour);
                    dialogFragment.dismissAllowingStateLoss();
                }
            });
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            final View dialogView = inflater.inflate(R.layout.full_screen_dialog,
                    container, false);

            setupWheelPickers(dialogView);
            addTimerName(dialogView);
            addTimerIcon(dialogView);

            return dialogView;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            return dialog;
        }

        private void saveToDatabase(EditText timerName, NumberPicker wheelPickerSec,
                                    NumberPicker wheelPickerMin, NumberPicker wheelPickerHour){
            int seconds = wheelPickerSec.getValue();
            int minutes = wheelPickerMin.getValue() * 1000 * 60;
            int hours = wheelPickerHour.getValue() * 1000 * 60 * 60;
            if (seconds != 0){
                seconds += 1000 * 5;
            }
            int timeInMillis = seconds + minutes + hours;
            String name = timerName.getText().toString();
            Timer timer = new Timer();
            timer.setTimerName(name);
            timer.setTimeInMillis(timeInMillis);

            AppDatabase database = Room.databaseBuilder(getContext(), AppDatabase.class, "db-timers")
                    .allowMainThreadQueries()
                    .build();
            database.getMyTimerDAO().insert(timer);

            // TODO update ListView
        }
    }

    private void showAddTimerDialog() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        dialogFragment = new CustomDialogFragment();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
        transaction.add(android.R.id.content, dialogFragment).addToBackStack(null).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
