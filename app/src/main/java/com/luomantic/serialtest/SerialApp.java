package com.luomantic.serialtest;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

public class SerialApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }

}
