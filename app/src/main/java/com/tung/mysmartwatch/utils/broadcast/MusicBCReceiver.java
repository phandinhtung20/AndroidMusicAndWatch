package com.tung.mysmartwatch.utils.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MusicBCReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Receiver: " + intent.getStringExtra("name"));
    }
}
