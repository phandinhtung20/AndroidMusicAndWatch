package com.tung.mysmartwatch.utils.services;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.util.Date;

public class MusicServiceBinder extends Binder {
    public final int UPDATE_AUDIO_PROGRESS_BAR = 1;

    private MediaPlayer mediaPlayer;
    private String audioFile;
    private Context context;

    private void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public void setContext(Context context) {
        this.context = context;
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
        mediaPlayer = MediaPlayer.create(context, uri);

        if (mediaPlayer != null) {
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        }

        audioProgressUpdateHandler.removeCallbacks(runnable);
        audioProgressUpdateHandler.post(runnable);
//        mediaPlayer.setOnCompletionListener();
    }

    private Handler audioProgressUpdateHandler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message updateAudioProgressMsg = new Message();
            updateAudioProgressMsg.what = UPDATE_AUDIO_PROGRESS_BAR;

            audioProgressUpdateHandler.sendMessage(updateAudioProgressMsg);
            audioProgressUpdateHandler.postDelayed(this, getNextTime());
        }
    };
    public void setAudioProgressUpdateHandler(Handler audioProgressUpdateHandler) {
        this.audioProgressUpdateHandler = audioProgressUpdateHandler;
    }

    public void releaseBinder() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            audioProgressUpdateHandler.removeCallbacks(runnable);
        }
    }

    public int getCurrentPosition() {
        if (mediaPlayer == null) return 0;
        if (!mediaPlayer.isPlaying()) {
            audioProgressUpdateHandler.removeCallbacks(runnable);
        }
        return mediaPlayer.getCurrentPosition();
    }

    private int getNextTime() {
        Date now = new Date();
        return (1000 - (int) now.getTime()%1000);
    }
}
