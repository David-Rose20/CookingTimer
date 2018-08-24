package com.example.company.cookingtimer.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.example.company.cookingtimer.R;

public class CustomDialogFragment extends DialogFragment {

    public static CustomDialogFragment dialogFragment;

    private static String[] addArrayString(int numberOfArrayCounts, int incremental) {
        String[] arrayString = new String[numberOfArrayCounts];
        int x = 0;
        for (int i = 0; i <= numberOfArrayCounts - 1; i++) {
            arrayString[i] = Integer.toString(x);
            x += incremental;
        }
        return arrayString;
    }

    public CustomDialogFragment() {
        //Empty constructor
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
                TimerFragment.dialogFragment.dismiss();
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