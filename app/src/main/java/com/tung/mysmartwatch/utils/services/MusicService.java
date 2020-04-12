package com.tung.mysmartwatch.utils.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.tung.mysmartwatch.models.MusicItem;
import com.tung.mysmartwatch.utils.notifcation.NotificationUtil;

import java.io.File;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private MusicItem musicItem;
    private MusicBinder serviceBinder = new MusicBinder();
    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer == null) {
                startAudio("/storage/emulated/0/Music/Tình nồng.mp3");
            }
            Log.d(">>>", "Log ne");
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Notification notification = NotificationUtil.notificationEx(this);
            startForeground(0, notification);
            handler.post(runnable);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(">>>>>>>", "Destroy cmnr");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean play = intent.getBooleanExtra("play", false);
        String name = intent.getStringExtra("name");
        if (play) {
            System.out.println("--" + name + "--");
//            startAudio(name);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Notification notification = NotificationUtil.notificationEx(this);
//                startForeground(0, notification);
//                handler.post(runnable);
//            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void startAudioFile(MusicItem musicItem) {
        if (musicItem == null) {
            return;
        }

        if (mediaPlayer != null) {
            if (this.musicItem.getUri().equals(musicItem.getUri())) {
                if (mediaPlayer.isPlaying()) {
                    return;
                } else {
                    mediaPlayer.reset();
                    mediaPlayer.start();
                    return;
                }
            }
        }
        this.musicItem = musicItem;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Notification notification = NotificationUtil.notificationMusic(this, musicItem);
            startForeground(0, notification);
        }
        startAudio();
    }

    private void startAudio(String link) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Uri uri = Uri.fromFile(new File(link));
        mediaPlayer = MediaPlayer.create(this, uri);

        if (mediaPlayer != null) {
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        }
    }

    private void startAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Uri uri = Uri.fromFile(new File(musicItem.getUri()));
        mediaPlayer = MediaPlayer.create(this, uri);

        if (mediaPlayer != null) {
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        }
    }

    public MusicItem getMusicItem() {
        return musicItem;
    }

    public int getCurrentPosition() {
        if (mediaPlayer == null) return 0;
        return mediaPlayer.getCurrentPosition();
    }

    public boolean isPlaying() {
        if (mediaPlayer == null) return false;
        return mediaPlayer.isPlaying();
    }
}
