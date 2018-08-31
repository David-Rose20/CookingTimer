package com.example.company.cookingtimer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.company.cookingtimer.R;
import com.example.company.cookingtimer.utils.TimerUtils;
import com.example.company.cookingtimer.models.Timer;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimerAdapter extends ArrayAdapter<Timer> {

    Context mContext;
    List<Timer> mTimerList;
    ViewGroup parentView;
    TimerUtils timerUtils;

    public TimerAdapter(@NonNull Context context, List<Timer> timerList) {
        super(context, 0, timerList);
        mContext = context;
        mTimerList = timerList;
        timerUtils = TimerUtils.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        parentView = parent;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.timer_list_items, parent, false);
        }
        final Timer currentTimer = getItem(position);


        // Title
        TextView timerTitle = listItemView.findViewById(R.id.timer_title);
        timerTitle.setText(currentTimer.getTimerName());
        timerTitle.setSelected(true);

        // Time
        TextView timeLeft = listItemView.findViewById(R.id.timer_time_left_text);
        String formattedTime = formatTime(currentTimer.getTimeInMillis());
        timeLeft.setText(formattedTime);

        // Image
        CircleImageView imageIcon = listItemView.findViewById(R.id.timer_circle_image);
        imageIcon.setImageDrawable(getContext().getDrawable(R.drawable.pasta_placeholder));

        ImageButton playButton = listItemView.findViewById(R.id.timer_play_image_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return listItemView;
    }

    private String formatTime(int durationInMillis) {
        return timerUtils.getFormattedTime(durationInMillis);
    }
}

