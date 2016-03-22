package com.geniusmart.loveut;

import android.app.Application;

public class LoveUTApplication extends Application{

    private static LoveUTApplication sInstance;

    public static LoveUTApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
