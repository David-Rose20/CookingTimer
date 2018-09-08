package com.example.company.cookingtimer.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.company.cookingtimer.interfaces.TimerClickInterface;
import com.example.company.cookingtimer.adapters.TimerRecyclerAdapter;
import com.example.company.cookingtimer.utils.TimerUtils;
import com.example.company.cookingtimer.room.AppDatabase;
import com.example.company.cookingtimer.ApplicationTimer;
import com.example.company.cookingtimer.R;
import com.example.company.cookingtimer.models.Timer;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFragment extends Fragment {

    private static final int GALLERY_CODE = 20;
    private static final int CAMERA_CODE = 200;

    private View emptyView;
    private TimerRecyclerAdapter recyclerAdapter;
    private TimerUtils timerUtils;

    private RecyclerView recyclerView;
    private ServiceBridge serviceBridge;
    private List<Timer> dbTimerList;

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

    private void initializeDataBase() {
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

        private String mUriPath;
        ImageView addPictureImageView;

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
                    dismissAllowingStateLoss();
                }
            });
            addPictureImageView = view.findViewById(R.id.add_picture_icon);
            addPictureImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openImageDialog();
                }
            });
        }

        private void openImageDialog(){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            alertBuilder.setTitle(R.string.choose_option);
            alertBuilder.setMessage(R.string.retrieve_image);
            alertBuilder.setNeutralButton(R.string.gallery, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    galleryIntent();
                }
            });
            alertBuilder.setPositiveButton(R.string.camera, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cameraIntent();
                }
            }).show();
        }

        private void galleryIntent() {
            Intent getPicIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            getPicIntent.setType("image/jpeg");
            startActivityForResult(getPicIntent, GALLERY_CODE);
        }

        private void cameraIntent() {
            Intent getThumbnailIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(getThumbnailIntent, 200);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (requestCode == GALLERY_CODE && resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                addPictureImageView.setImageURI(imageUri);
                mUriPath = imageUri.toString();
            }
            if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                addPictureImageView.setImageBitmap(thumbnail);

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());

                String fileName = timeStamp;

                File imageFile = new File(getContext().getFilesDir(), fileName);

                try {
                    FileOutputStream outputStream = new FileOutputStream(imageFile);
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    mUriPath = imageFile.getAbsolutePath();
                } catch (Exception e) {
                    Log.e(TAG, "onActivityResult: ", e);
                }
            }
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
                                    NumberPicker wheelPickerMin, NumberPicker wheelPickerHour) {
            int seconds = wheelPickerSec.getValue() * 5 * 1000;
            int minutes = wheelPickerMin.getValue() * 1000 * 60;
            int hours = wheelPickerHour.getValue() * 1000 * 60 * 60;
            int timeInMillis = seconds + minutes + hours;
            String name = timerName.getText().toString();
            Timer timer = new Timer();

            timer.setTimerName(name);
            timer.setTimeInMillis(timeInMillis);
            timer.setTimerImageUriString(mUriPath);
            AppDatabase database = Room.databaseBuilder(getContext(), AppDatabase.class, "db-timers")
                    .allowMainThreadQueries()
                    .build();
            database.getMyTimerDAO().insert(timer);

            // TODO update ListView
        }
    }

    private void showAddTimerDialog() {
        CustomDialogFragment dialogFragment = new CustomDialogFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

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
