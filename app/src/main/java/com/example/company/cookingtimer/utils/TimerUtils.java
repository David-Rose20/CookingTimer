package com.example.company.cookingtimer.utils;

public class TimerUtils {

    private static TimerUtils INSTANCE;

    public static TimerUtils getInstance(){
        if (INSTANCE == null){
            INSTANCE = new TimerUtils();
        }
        return INSTANCE;
    }

    /*Huge thanks to Brajendra Pandey
    https://stackoverflow.com/a/16520928/6353637*/
    public String getFormattedTime(int durationInMillis){
        int seconds = durationInMillis / 1000 % 60;
        int minutes = durationInMillis / (1000 * 60) % 60;
        String formattedTime = String.format("%02dm:%02ds", minutes, seconds);
        return formattedTime;
    }
}
