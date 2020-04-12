package com.tung.mysmartwatch.utils.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.squareup.otto.Subscribe;
import com.tung.mysmartwatch.utils.otto.events.MusicEvent;

public class MusicBCReceiver extends BroadcastReceiver {
    public MusicBCReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("name");
        System.out.println("Receiver: " + intent.getStringExtra("name"));
    }

    @Subscribe
    public void MusicEvent(MusicEvent event) {
        String name = event.getName();
        System.out.println("MusicEvent: " + name);
    }
}
