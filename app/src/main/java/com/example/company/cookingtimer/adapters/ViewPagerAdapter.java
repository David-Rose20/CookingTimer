package com.example.company.cookingtimer.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.company.cookingtimer.R;
import com.example.company.cookingtimer.fragments.TimerFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter{

    private Context mContext;

    public ViewPagerAdapter(Context context, FragmentManager fragmentManager){
        super(fragmentManager);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new TimerFragment();
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return mContext.getString(R.string.timers);
        } else {
            return "";
        }
    }
}
