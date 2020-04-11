package com.tung.mysmartwatch.utils.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;

import com.squareup.otto.Subscribe;
import com.tung.mysmartwatch.App;
import com.tung.mysmartwatch.utils.otto.BusProvider;
import com.tung.mysmartwatch.utils.otto.events.MusicEvent;

import java.io.File;

public class MusicBCReceiver extends BroadcastReceiver {
    public MusicBCReceiver() {
        super();
        BusProvider.getInstance().getBus().register(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean play = intent.getBooleanExtra("play", false);
        String name = intent.getStringExtra("name");
        System.out.println("Receiver: " + intent.getStringExtra("name"));
        if (play) {
            startAudioFile(name);
        }
    }

    private MediaPlayer mediaPlayer;
    private String audioFile;

    private void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public void startAudioFile(String audioFile) {
        if (audioFile == null || audioFile.length() == 0) {
            return;
        }

        if (mediaPlayer != null) {
            if (this.audioFile.equals(audioFile)) {
                if (mediaPlayer.isPlaying()) {
                    return;
                } else {
                    mediaPlayer.reset();
                    mediaPlayer.start();
                    return;
                }
            }
        }
        setAudioFile(audioFile);
        startAudio();
    }

    private void startAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Uri uri = Uri.fromFile(new File(audioFile));
        mediaPlayer = MediaPlayer.create(App.getInstance().getApplicationContext(), uri);

        if (mediaPlayer != null) {
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        }
    }

    @Subscribe
    public void MusicEvent(MusicEvent event) {
        String name = event.getName();
        System.out.println("MusicEvent: " + name);
    }
}
