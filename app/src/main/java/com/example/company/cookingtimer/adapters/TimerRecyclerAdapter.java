package com.example.company.cookingtimer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.company.cookingtimer.R;
import com.example.company.cookingtimer.ServiceBridge;
import com.example.company.cookingtimer.interfaces.TimerClickInterface;
import com.example.company.cookingtimer.models.Timer;
import com.example.company.cookingtimer.utils.TimerUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimerRecyclerAdapter extends RecyclerView.Adapter<TimerRecyclerAdapter.ViewHolder> {

    Context mContext;
    List<Timer> mTimerList;
    TimerUtils timerUtils;
    TimerClickInterface mClickInterface;
    ServiceBridge serviceBridge;

    public TimerRecyclerAdapter(Context context, List<Timer> timerList, TimerClickInterface timerClickInterface){
        mTimerList = timerList;
        mContext = context;
        timerUtils = TimerUtils.getInstance();
        mClickInterface = timerClickInterface;
        serviceBridge = ServiceBridge.getInstance();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView timerTitle;
        TextView timeLeft;
        CircleImageView imageIcon;
        ImageButton playButton;

        public ViewHolder(View itemView) {
            super(itemView);

            timerTitle = itemView.findViewById(R.id.timer_title);
            timeLeft = itemView.findViewById(R.id.timer_time_left_text);
            imageIcon = itemView.findViewById(R.id.timer_circle_image);
            playButton = itemView.findViewById(R.id.timer_play_image_button);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickInterface.onItemClicked(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItems = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timer_list_items, parent, false);
        return new ViewHolder(listItems);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Timer currentTimer = mTimerList.get(position);

        holder.timerTitle.setText(currentTimer.getTimerName());

        String formattedTime = formatTime(currentTimer.getTimeInMillis());
        holder.timeLeft.setText(formattedTime);

        Glide.with(mContext)
                .load(currentTimer.getTimerImageUriString())
                .apply(new RequestOptions().override(120))
                .into(holder.imageIcon);

        Log.d("adapter", "onBindViewHolder: URI = " + currentTimer.getTimerImageUriString());

        serviceBridge.viewHolder(holder, currentTimer);
    }

    @Override
    public int getItemCount() {
        return mTimerList.size();
    }

    private String formatTime(int durationInMillis) {
        return timerUtils.getFormattedTime(durationInMillis);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
