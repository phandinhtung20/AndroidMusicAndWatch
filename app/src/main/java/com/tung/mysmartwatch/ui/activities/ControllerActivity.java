package com.tung.mysmartwatch.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tung.mysmartwatch.App;
import com.tung.mysmartwatch.R;
import com.tung.mysmartwatch.adapters.MusicItemAdapter;
import com.tung.mysmartwatch.models.MusicItem;
import com.tung.mysmartwatch.utils.broadcast.BroadcastUtils;
import com.tung.mysmartwatch.utils.otto.BusProvider;
import com.tung.mysmartwatch.utils.otto.events.MusicEvent;
import com.tung.mysmartwatch.utils.permissions.PermissionManager;
import com.tung.mysmartwatch.utils.services.MusicService;
import com.tung.mysmartwatch.utils.services.MusicServiceBinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControllerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private TextView tvMusicName;
    private ProgressBar pbDuration;
    private List<MusicItem> listMusic;
    private ArrayAdapter musicAdapter;

    private MusicServiceBinder serviceBinder;

    private ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            serviceBinder = (MusicServiceBinder) iBinder;
            serviceBinder.setContext(ControllerActivity.this);
            createAudioProgressbarUpdater();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tvMusicName = findViewById(R.id.tv_music_name);
        pbDuration = findViewById(R.id.pb_audio_duration);
        ListView lvMusic = findViewById(R.id.lv_music);
        listMusic = new ArrayList<>();
        musicAdapter = new MusicItemAdapter(this, R.layout.layout_music_item, listMusic);
        lvMusic.setAdapter(musicAdapter);
        lvMusic.setOnItemClickListener(this);
        checkNotch();
        getListMusic();

        bindMusicService();
    }

    private void bindMusicService() {
        if (serviceBinder == null) {
            Intent intent = new Intent(this, MusicService.class);
            bindService(intent, musicServiceConnection, BIND_AUTO_CREATE);
        }
    }

    private void unbindMusicService() {
        if (serviceBinder != null) {
            unbindService(musicServiceConnection);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigatorBar();
    }

    @Override
    protected void onPause() {
        super.onPause();
        serviceBinder.releaseBinder();
        unbindMusicService();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void hideNavigatorBar() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            View decorView = getWindow().getDecorView();
            int uiOptions = 0;
            uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void getListMusic() {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection;
        boolean newVersion = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
        if (newVersion) {
            projection = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.DURATION
            };
        } else {
            projection = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.DISPLAY_NAME
            };
        }

        Cursor cursor = this.managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null);

        while(cursor.moveToNext()) {
            listMusic.add(new MusicItem(cursor.getString(4), cursor.getString(1),
                    cursor.getString(3), newVersion ? cursor.getInt(5) : 0));
        }

        Collections.sort(listMusic);
        musicAdapter.notifyDataSetChanged();
    }

    private void playMusic(int i) {
        tvMusicName.setText(listMusic.get(i).getName());
        pbDuration.setMax(listMusic.get(i).getDuration());
//        serviceBinder.setAudioProgressUpdateHandler(audioProgressUpdateHandler);
//        serviceBinder.startAudioFile(listMusic.get(i).getUri());

        Intent intentBC = new Intent("music.event");
        intentBC.putExtra("name", listMusic.get(i).getUri());
        intentBC.putExtra("play", true);
        BroadcastUtils.sendImplicitBroadcast(this, intentBC);

        BusProvider.getInstance().getBus().post(new MusicEvent("tung utng", false,1, 1));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationMusic(i);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        playMusic(i);
    }

    private void checkNotch() {
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        LinearLayout ll = findViewById(R.id.bg);
        final LinearLayout status_bar = findViewById(R.id.ll_status_bar);
        ll.setBackground(wallpaperDrawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            ll.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                        if (windowInsets != null) {
                            DisplayCutout cutout = windowInsets.getDisplayCutout();
                            if (cutout != null) {
                                status_bar.setVisibility(View.VISIBLE);
                                status_bar.setLayoutParams(new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT, cutout.getSafeInsetTop()));
                            }
                        }
                    }
                    return windowInsets;
                }
            });
        }
    }

    private Handler audioProgressUpdateHandler = null;
    @SuppressLint("HandlerLeak")
    private void createAudioProgressbarUpdater() {
        if(audioProgressUpdateHandler == null) {
            audioProgressUpdateHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg == null) return;
                    if (msg.what == serviceBinder.UPDATE_AUDIO_PROGRESS_BAR) {
                        pbDuration.setProgress(serviceBinder.getCurrentPosition());
                    }
                }
            };
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void notificationMusic(int i) {
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
                .setContentTitle(listMusic.get(i).getName())
                .setContentText(listMusic.get(i).getSinger())
                .setNumber(1)
                .setLargeIcon(bitmap);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(10, builder.build());
    }

    private void removeNotification() {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.cancel(10);
    }
}
