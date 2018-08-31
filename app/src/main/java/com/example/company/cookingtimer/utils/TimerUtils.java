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
    public static String getFormattedTime(int durationInMillis) {
        String formattedTime;
        int seconds = durationInMillis / 1000 % 60;
        int minutes = durationInMillis / (1000 * 60) % 60;
        int hours = durationInMillis / (1000 * 60 * 60) % 24;
        formattedTime = String.format("%02dh:%02dm:%02ds", hours, minutes, seconds);

        return formattedTime;
    }
}
