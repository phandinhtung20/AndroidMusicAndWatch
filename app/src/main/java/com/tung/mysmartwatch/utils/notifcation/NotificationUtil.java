package com.tung.mysmartwatch.utils.notifcation;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tung.mysmartwatch.App;
import com.tung.mysmartwatch.R;
import com.tung.mysmartwatch.models.MusicItem;
import com.tung.mysmartwatch.ui.activities.MainActivity;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationUtil {

    public static Notification notificationMusic(Context context, MusicItem item) {
        Intent replyIntent = new Intent(context, MainActivity.class);
        PendingIntent replyPendingIntent =
                PendingIntent.getActivity(context, 0, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        NotificationCompat.Style style = new androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(1)
                .setMediaSession(null);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.CHANNEL1_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_music_player)
                .addAction(R.drawable.apollo_holo_dark_prev, "Previous", replyPendingIntent)
                .addAction(R.drawable.apollo_holo_dark_pause, "Pause", replyPendingIntent)
                .addAction(R.drawable.apollo_holo_dark_next, "Next", replyPendingIntent)
                .setStyle(style)
                .setContentTitle(item.getName())
                .setContentText(item.getSinger())
                .setNumber(1)
                .setLargeIcon(bitmap);

        Notification notification = builder.build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(11, notification);
        return notification;
    }

    public static Notification notificationEx(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.CHANNEL1_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_music_player)
                .setContentTitle("Service")
                .setContentText("No content");

        Notification notification = builder.build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(10, notification);
        return notification;
    }
}
