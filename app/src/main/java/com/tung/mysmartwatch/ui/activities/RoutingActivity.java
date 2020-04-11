package com.tung.mysmartwatch.ui.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.session.MediaSession;
import android.media.session.MediaSession.Token;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.view.View;

import com.tung.mysmartwatch.App;
import com.tung.mysmartwatch.R;

import java.util.Date;

public class RoutingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routing);

        findViewById(R.id.base).setOnClickListener(this);
        findViewById(R.id.long_message).setOnClickListener(this);
        findViewById(R.id.intent).setOnClickListener(this);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.reply).setOnClickListener(this);
        findViewById(R.id.event_public).setOnClickListener(this);
        findViewById(R.id.download).setOnClickListener(this);
        findViewById(R.id.message).setOnClickListener(this);
        findViewById(R.id.big_picture).setOnClickListener(this);
        findViewById(R.id.music).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base:
                notificationBase();
                break;
            case R.id.long_message:
                notificationLong();
                break;
            case R.id.intent:
                notificationIntent();
                break;
            case R.id.button:
                notificationButton();
                break;
            case R.id.reply:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                    notificationReply();
                }
                break;
            case R.id.event_public:
                notificationPublic();
                break;
            case R.id.download:
                notificationDownload();
                break;
            case R.id.message:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationMessage();
                }
                break;
            case R.id.big_picture:
                notificationPicture();
                break;
            case R.id.music:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notificationMusic();
                }
                break;
        }
    }

    private int count = 0;
    private void notificationDownload() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(RoutingActivity.this);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(RoutingActivity.this, App.CHANNEL1_ID);
                builder.setContentText("Download in progress")
                        .setSmallIcon(R.drawable.ic_download)
                        .setPriority(NotificationCompat.PRIORITY_LOW);
                if(count < 100) {
                    builder.setContentTitle("Picture Download");
                    int PROGRESS_MAX = 100;
                    int PROGRESS_CURRENT = count;
                    builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
                    count++;
                } else {
                    builder.setContentText("Download complete")
                            .setProgress(0,0,false);
                }
                notificationManager.notify(8, builder.build());
                handler.postDelayed(this, 200);
            }
        }, 200);
    }

    public void notificationBase() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, App.CHANNEL1_ID)
                .setSmallIcon(R.drawable.ic_email)
                .setContentTitle("Tao ne thong bao base nhe")
                .setContentText("Noi dung thong bao deo co cai gi dau!!!")
                .setShowWhen(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(0, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notificationMessage() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, App.CHANNEL1_ID)
                .setSmallIcon(R.drawable.ic_email)
                .setContentTitle("Tao là thông báo rất dài nhé")
                .setStyle(new NotificationCompat.MessagingStyle("Me")
                        .setConversationTitle("Team lunch")
                        .addMessage("Hi", new Date().getTime(), "Me") // Pass in null for user.
                        .addMessage("What's up?", new Date().getTime(), "Coworker")
                        .addMessage("Not much", new Date().getTime(), "Me")
                        .addMessage("How about lunch?", new Date().getTime(), "Coworker"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(7, builder.build());
    }

    public void notificationPublic() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("Show message");
                NotificationCompat.Builder builder = new NotificationCompat.Builder(RoutingActivity.this, App.CHANNEL1_ID)
                        .setSmallIcon(R.drawable.ic_email)
                        .setContentTitle("Tao ne thong bao public nhe")
                        .setContentText("Noi dung thong bao deo co cai gi dau!!!")
                        .setShowWhen(false)
                        .setAutoCancel(true)
                        .setTimeoutAfter(30000)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(RoutingActivity.this);
                managerCompat.notify(6, builder.build());
            }
        }, 2000);

    }

    public void notificationLong() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, App.CHANNEL1_ID)
                .setSmallIcon(R.drawable.ic_email)
                .setContentTitle("Tao là thông báo rất dài nhé")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getResources().getString(R.string.welcome_messages)))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1, builder.build());
    }

    public void notificationIntent() {
        Intent intent = new Intent(this, ConfigureActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, App.CHANNEL1_ID)
                .setSmallIcon(R.drawable.ic_email)
                .setContentTitle("Nào cùng mở New Intent nào")
                .setContentText("Click vào đây để mở nhé bạn!!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(2, builder.build());
    }

    public void notificationButton() {
        Intent intent = new Intent(this, ConfigureActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, App.CHANNEL1_ID)
                .setSmallIcon(R.drawable.ic_email)
                .setContentTitle("Thông báo có button nhé bạn")
                .setContentText("Click button đi nào bợi ơi!!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_email, "Mail", pendingIntent)
                .setAutoCancel(true);


        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(3, builder.build());
    }

    public static final String KEY_TEXT_REPLY = "key_text_reply";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public void notificationReply() {
        Intent intent = new Intent(this, ConfigureActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, App.CHANNEL1_ID)
                .setSmallIcon(R.drawable.ic_email)
                .setContentTitle("Thông báo có reply nhé bạn")
                .setContentText("Click button đi nào bạn ơi!!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY).setLabel("Enter your message...").build();
            Intent replyIntent = new Intent(this, MainActivity.class);
            intent.putExtra("name", "Send press roi do m oi");
            PendingIntent replyPendingIntent =
                    PendingIntent.getActivity(this, 0, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Action action = new NotificationCompat.Action
                    .Builder(R.drawable.ic_reply, "Reply", replyPendingIntent)
                    .addRemoteInput(remoteInput)
                    .build();

            builder.addAction(action);
        }


        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(4, builder.build());
    }

    public void notificationPicture() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, App.CHANNEL1_ID)
                .setSmallIcon(R.drawable.ic_email)
                .setContentTitle("Screen ne ban oi")
                .setContentText("Tap to view!!!")
                .setShowWhen(false)
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .bigLargeIcon(null))
//                .setStyle(new NotificationCompat.BigPictureStyle()
//                        .bigPicture(bitmap))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(9, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void notificationMusic() {
        Intent replyIntent = new Intent(this, MainActivity.class);
        PendingIntent replyPendingIntent =
                PendingIntent.getActivity(this, 0, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        NotificationCompat.Style style = new androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(1)
                .setMediaSession(null);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, App.CHANNEL1_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_music_player)
                .addAction(R.drawable.apollo_holo_dark_prev, "Previous", replyPendingIntent)
                .addAction(R.drawable.apollo_holo_dark_pause, "Pause", replyPendingIntent)
                .addAction(R.drawable.apollo_holo_dark_next, "Next", replyPendingIntent)
                .setStyle(style)
                .setContentTitle("Wonderful music")
                .setContentText("My Awesome Band")
                .setLargeIcon(bitmap);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(10, builder.build());
    }
}
