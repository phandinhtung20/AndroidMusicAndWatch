package com.tung.mysmartwatch;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.IntentFilter;
import android.os.Build;
import android.widget.Toast;

import com.tung.mysmartwatch.utils.broadcast.MusicBCReceiver;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class App extends Application {
    private MusicBCReceiver broadcast;
    public static final String CHANNEL1_ID = "CN_1";

    public static App mInstance;

    public static synchronized App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        startBroadcast();
        createNotificationChannel();
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getResources().getString(R.string.channel_1_name);
            String description = getResources().getString(R.string.channel_1_description);
            int importantCode = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL1_ID, name, importantCode);
            channel.setDescription(description);
            channel.setShowBadge(false);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            } else {
                Toast.makeText(this, "manager nul roi ma oi", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
