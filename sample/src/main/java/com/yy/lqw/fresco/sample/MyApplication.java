package com.yy.lqw.fresco.sample;

import android.app.Application;

import com.yy.lqw.fresco.FrescoMe;

/**
 * Created by lunqingwen on 2016/11/24.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FrescoMe.initialize(this);
    }
}
