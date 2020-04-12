package com.tung.mysmartwatch.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
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

import com.tung.mysmartwatch.R;
import com.tung.mysmartwatch.adapters.MusicItemAdapter;
import com.tung.mysmartwatch.models.MusicItem;
import com.tung.mysmartwatch.utils.notifcation.NotificationUtil;
import com.tung.mysmartwatch.utils.services.MusicService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.tung.mysmartwatch.utils.play_music.MusicUtility.getNextTime;

public class ControllerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private boolean bound;
    private TextView tvMusicName;
    private ProgressBar pbDuration;
    private List<MusicItem> listMusic;
    private ArrayAdapter musicAdapter;

    private MusicService musicService;
    private MusicService.MusicBinder serviceBinder;

    private Handler audioProgressHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pbDuration.setProgress(musicService.getCurrentPosition());
                }
            });
            audioProgressHandler.postDelayed(this, getNextTime());
        }
    };

    private ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            serviceBinder = (MusicService.MusicBinder) iBinder;
            musicService = serviceBinder.getService();
            bound = true;
            System.out.println("ServiceConnection connected");
            checkMusicStarted();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bound = false;
            System.out.println("ServiceConnection disconnected");
        }
    };

    private void checkMusicStarted() {
        if (musicService.isPlaying()) {
            tvMusicName.setText(musicService.getMusicItem().getName());
            pbDuration.setMax(musicService.getMusicItem().getDuration());
            startCount();
        }
    }

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
        System.out.println("Activity onCreate");
    }

    private void bindMusicService() {
        if (serviceBinder == null) {
            System.out.println("Activity bind");
            Intent intent = new Intent(this, MusicService.class);
            bindService(intent, musicServiceConnection, BIND_AUTO_CREATE);
        } else {
            System.out.println("Activity bind null");
        }
    }

    private void unbindMusicService() {
        if (bound) {
            System.out.println("Activity unbind");
            unbindService(musicServiceConnection);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigatorBar();
    }

    @Override
    protected void onStart() {
        System.out.println("Activity onStart");
        super.onStart();
//        bindMusicService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("Activity onStop");
//        unbindMusicService();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("Activity onBackPressed");
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
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("play", true);
        intent.putExtra("name", listMusic.get(i).getUri());
        startService(intent);
//        tvMusicName.setText(listMusic.get(i).getName());
//        pbDuration.setMax(listMusic.get(i).getDuration());
//        startCount();
//        musicService.startAudioFile(listMusic.get(i));
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

    private void startCount() {
        audioProgressHandler.removeCallbacks(runnable);
        audioProgressHandler.post(runnable);
    }
}
