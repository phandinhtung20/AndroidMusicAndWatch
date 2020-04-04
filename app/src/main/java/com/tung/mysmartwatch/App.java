package com.tung.mysmartwatch;

import android.app.Application;

public class App extends Application {
    public static App mInstance;

    public static synchronized App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
