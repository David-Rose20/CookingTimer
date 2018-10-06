package com.example.company.cookingtimer.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.company.cookingtimer.R;
import com.example.company.cookingtimer.models.Timer;
import com.example.company.cookingtimer.room.AppDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddTimerActivity extends AppCompatActivity {

    private static final String TAG = "AddTimerActivity";
    private static final int GALLERY_CODE = 20;
    private static final int CAMERA_CODE = 200;

    private String mUriPath;
    ImageView addPictureImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timer);

        setupWheelPickers();
        addTimerName();
        addTimerIcon();

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

    private void setupWheelPickers() {
        // Setup Hourly wheel picker
        String[] wheelPickerHourlyValue = addArrayString(24, 1);

        NumberPicker wheelPickerHourly = findViewById(R.id.wheel_picker_hourly);
        wheelPickerHourly.setMinValue(0);
        wheelPickerHourly.setMaxValue(wheelPickerHourlyValue.length - 1);
        wheelPickerHourly.setDisplayedValues(wheelPickerHourlyValue);

        // Setup Minutes wheel picker
        String[] wheelPickerMinValue = addArrayString(60, 1);
        NumberPicker wheelPickerMin = findViewById(R.id.wheel_picker_min);
        wheelPickerMin.setMinValue(0);
        wheelPickerMin.setMaxValue(wheelPickerMinValue.length - 1);
        wheelPickerMin.setDisplayedValues(wheelPickerMinValue);

        // Setup Sec wheel picker
        String[] wheelPickerSecValue = addArrayString(12, 5);
        NumberPicker wheelPickerSec = findViewById(R.id.wheel_picker_sec);
        wheelPickerSec.setMinValue(0);
        wheelPickerSec.setMaxValue(wheelPickerSecValue.length - 1);
        wheelPickerSec.setDisplayedValues(wheelPickerSecValue);
    }

    private void addTimerName() {
        EditText timerName = findViewById(R.id.name_of_timer);
        timerName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == 66) {
//                        hideKeyboard();
                }
                return true;
            }
        });
    }

    private void addTimerIcon() {
        ImageView addTimerIcon = findViewById(R.id.button_add_timer);
        final EditText timerName = findViewById(R.id.name_of_timer);
        final NumberPicker wheelPickerSec = findViewById(R.id.wheel_picker_sec);
        final NumberPicker wheelPickerMin = findViewById(R.id.wheel_picker_min);
        final NumberPicker wheelPickerHour = findViewById(R.id.wheel_picker_hourly);
        addTimerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDatabase(timerName, wheelPickerSec, wheelPickerMin, wheelPickerHour);
                startActivity(new Intent(AddTimerActivity.this, MainActivity.class));
            }
        });
        addPictureImageView = findViewById(R.id.add_picture_icon);
        addPictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImageDialog();
            }
        });
    }

    private void openImageDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
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
        startActivityForResult(getThumbnailIntent, CAMERA_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .apply(new RequestOptions().override(150))
                    .into(addPictureImageView);
            addPictureImageView.setImageURI(imageUri);
            mUriPath = imageUri.toString();
        }
        if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            Glide.with(this)
                    .load(thumbnail)
                    .apply(new RequestOptions().override(150))
                    .into(addPictureImageView);
            addPictureImageView.setImageBitmap(thumbnail);

            String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());


            File imageFile = new File(this.getFilesDir(), fileName);

            try {
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                mUriPath = imageFile.getAbsolutePath();
            } catch (Exception e) {
                Log.e(TAG, "onActivityResult: ", e);
            }
        }
    }

    private void saveToDatabase(EditText timerName, NumberPicker wheelPickerSec,
                                NumberPicker wheelPickerMin, NumberPicker wheelPickerHour) {
        int seconds = wheelPickerSec.getValue() * 5 * 1000;
        int minutes = wheelPickerMin.getValue() * 1000 * 60;
        int hours = wheelPickerHour.getValue() * 1000 * 60 * 60;
        int timeInMillis = seconds + minutes + hours;
        String name = timerName.getText().toString();

        final Timer timer = new Timer();
        timer.setTimerName(name);
        timer.setTimeInMillis(timeInMillis);
        timer.setTimerImageUriString(mUriPath);

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase database = Room.databaseBuilder(getBaseContext(), AppDatabase.class, "db-timers")
                        .build();
                database.getMyTimerDAO().insert(timer);
            }
        }).start();
    }
}
