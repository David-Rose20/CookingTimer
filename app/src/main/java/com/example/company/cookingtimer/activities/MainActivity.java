package com.example.company.cookingtimer.activities;

import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.company.cookingtimer.R;
import com.example.company.cookingtimer.adapters.TimerAdapter;
import com.example.company.cookingtimer.adapters.ViewPagerAdapter;
import com.example.company.cookingtimer.fragments.TimerFragment;
import com.example.company.cookingtimer.models.Timer;
import com.example.company.cookingtimer.services.TimerService2;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.text.SimpleDateFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0f);

        initializeViews();
        setupAdapters();
    }

    private void initializeViews() {
        viewPager = findViewById(R.id.view_pager);
    }

    private void setupAdapters() {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}