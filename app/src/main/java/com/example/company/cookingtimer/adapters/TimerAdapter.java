package com.example.company.cookingtimer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.company.cookingtimer.R;
import com.example.company.cookingtimer.activities.MainActivity;
import com.example.company.cookingtimer.fragments.TimerFragment;
import com.example.company.cookingtimer.models.Timer;
import com.example.company.cookingtimer.models.ViewContainer;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimerAdapter extends ArrayAdapter<Timer> {

    Context mContext;
    MainActivity mainActivity;
    List<Timer> mTimerList;

    private int mTimerId;

    private static final String TAG = "TimerAdapter";

    public TimerAdapter(@NonNull Context context, List<Timer> timerList) {
        super(context, 0, timerList);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.timer_list_items, parent, false);
        }
        Timer currentTimer = getItem(position);

        // Title
        TextView timerTitle = listItemView.findViewById(R.id.timer_title);
        timerTitle.setText(currentTimer.getTimerName());
        timerTitle.setSelected(true);

        // Image
        CircleImageView imageIcon = listItemView.findViewById(R.id.timer_circle_image);
        imageIcon.setImageDrawable(getContext().getDrawable(currentTimer.getImageResourceId()));

        mTimerId = currentTimer.getTimerId();

        return listItemView;
    }
}

