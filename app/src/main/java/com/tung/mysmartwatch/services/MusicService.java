package com.tung.mysmartwatch.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {
//    private MediaPlayer mediaPlayer;
//    private String linkMusic;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }

    private MusicServiceBinder serviceBinder = new MusicServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        String linkMusic = intent.getStringExtra("link");
//        if (linkMusic == null || linkMusic.length() == 0) {
//            return START_NOT_STICKY;
//        }
//
//        if (mediaPlayer != null) {
//            if (this.linkMusic.equals(linkMusic)) {
//                if (mediaPlayer.isPlaying()) {
//                    return START_NOT_STICKY;
//                } else {
//                    mediaPlayer.reset();
//                    mediaPlayer.start();
//
//                    return START_STICKY;
//                }
//            }
//        }
//        this.linkMusic = linkMusic;
//
//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//            mediaPlayer.reset();
//        }
//        Uri uri = Uri.fromFile(new File(linkMusic));
//        mediaPlayer = MediaPlayer.create(this, uri);
//
//        if (mediaPlayer != null) {
//            mediaPlayer.setLooping(false);
//            mediaPlayer.start();
//        }
//
//        return START_STICKY;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mediaPlayer.stop();
//        mediaPlayer.release();
//    }
}
