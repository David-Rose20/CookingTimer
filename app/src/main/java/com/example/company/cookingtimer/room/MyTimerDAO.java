package com.example.company.cookingtimer.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.company.cookingtimer.models.Timer;

import java.util.List;

@Dao
public interface MyTimerDAO {

    @Insert
    void insert(Timer... timers);

    @Update
    void update(Timer... timers);

    @Delete
    void delete(Timer timers);

    @Query("SELECT * FROM timer")
    List<Timer> getMyTimers();

    @Query("SELECT * FROM timer WHERE mTimerName =:name")
    Timer getTimerName(String name);

}
