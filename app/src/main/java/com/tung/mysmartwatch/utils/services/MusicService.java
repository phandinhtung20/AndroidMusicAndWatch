package com.tung.mysmartwatch.utils.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    private MusicServiceBinder serviceBinder = new MusicServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }
}
