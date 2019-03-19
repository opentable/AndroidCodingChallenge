package com.opentable.moviereviews.lifecycle;


import android.app.Application;
import android.content.Context;

/**
 * @author ronan.oneill
 */
public class ApplicationWrapper extends Application {
    private static ApplicationWrapper mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized ApplicationWrapper getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }
}

