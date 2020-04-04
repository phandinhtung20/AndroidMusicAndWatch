package com.tung.mysmartwatch.services;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;

import java.io.File;

public class MusicServiceBinder extends Binder {
    private MediaPlayer mediaPlayer;
    private String audioFile;
    private Context context;

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void startAudio() {
        initAudioPlayer();
        if(mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    // Initialise audio player.
    private void initAudioPlayer()
    {
        if (mediaPlayer == null) {
            Uri uri = Uri.fromFile(new File(audioFile));
            mediaPlayer = MediaPlayer.create(context, uri);
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        }
//                mediaPlayer.prepare();

                // This thread object will send update audio progress message to caller activity every 1 second.
//                Thread updateAudioProgressThread = new Thread()
//                {
//                    @Override
//                    public void run() {
//                        while(true)
//                        {
//                            // Create update audio progress message.
//                            Message updateAudioProgressMsg = new Message();
//                            updateAudioProgressMsg.what = UPDATE_AUDIO_PROGRESS_BAR;
//
//                            // Send the message to caller activity's update audio prgressbar Handler object.
//                            audioProgressUpdateHandler.sendMessage(updateAudioProgressMsg);
//
//                            // Sleep one second.
//                            try {
//                                Thread.sleep(1000);
//                            }catch(InterruptedException ex)
//                            {
//                                ex.printStackTrace();
//                            }
//                        }
//                    }
//                };
//                // Run above thread object.
//                updateAudioProgressThread.start();
//            }
    }
}
