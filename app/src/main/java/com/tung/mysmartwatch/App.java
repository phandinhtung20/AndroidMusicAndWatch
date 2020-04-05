package com.tung.mysmartwatch;

import android.app.Application;
import android.content.IntentFilter;

import com.tung.mysmartwatch.utils.broadcast.MusicBCReceiver;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class App extends Application {
    private MusicBCReceiver broadcast;

    public static App mInstance;

    public static synchronized App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        startBroadcast();
    }

    @Override
    public void onTerminate() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcast);
        super.onTerminate();
    }

    private void startBroadcast() {
        broadcast = new MusicBCReceiver();
        IntentFilter filter = new IntentFilter("music.event");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcast, filter);
    }
}
