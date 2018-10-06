package com.example.company.cookingtimer.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.company.cookingtimer.models.Timer;

@Database(entities = {Timer.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MyTimerDAO getMyTimerDAO();

}
