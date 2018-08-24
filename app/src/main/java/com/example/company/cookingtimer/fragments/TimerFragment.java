package com.example.company.cookingtimer.fragments;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
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
    static CustomDialogFragment dialogFragment;

    private static final String TAG = "TimerFragment";

    public TimerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_view_layout,
                container, false);


        initializeViews(rootView);
        setupAddTimerButton(rootView);

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
    private void setupAddTimerButton(View rv) {
        Button addTimerButton = rv.findViewById(R.id.button_add_timer);
        addTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTimerDialog();
            }
        });
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
            addTimerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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

    }

    private void showAddTimerDialog() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        dialogFragment = new CustomDialogFragment();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
        transaction.add(android.R.id.content, dialogFragment).addToBackStack(null).commit();
    }

}
