package com.greatcall.activitytransition;

import android.app.Application;

import com.greatcall.logging.LoggerSingleton;

/**
 * Created by skirk on 7/19/17.
 */

public class TransitionApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LoggerSingleton.createInstance(this);
    }

}
