package com.tung.mysmartwatch.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
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
import android.widget.Toast;

import com.tung.mysmartwatch.R;
import com.tung.mysmartwatch.adapters.MusicItemAdapter;
import com.tung.mysmartwatch.models.MusicItem;
import com.tung.mysmartwatch.services.MusicService;
import com.tung.mysmartwatch.services.MusicServiceBinder;

import java.util.ArrayList;
import java.util.List;

public class ControllerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView lvMusic;
    private List<MusicItem> listMusic;
    private ArrayAdapter musicAdapter;

    private MusicServiceBinder serviceBinder;

    private ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            serviceBinder = (MusicServiceBinder) iBinder;
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

        lvMusic = findViewById(R.id.lv_music);
        listMusic = new ArrayList<>();
        musicAdapter = new MusicItemAdapter(this, R.layout.layout_music_item, listMusic);
        lvMusic.setAdapter(musicAdapter);
        lvMusic.setOnItemClickListener(this);
        checkNotch();
        checkPermissionRead();

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
    protected void onDestroy() {
        super.onDestroy();
        unbindMusicService();
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
        boolean newVersion = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;
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

        musicAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getListMusic();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 1001) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getListMusic();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkPermissionRead() {
        int permissionCheckRead =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheckRead != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 1000);
        } else {
            checkPermissionWrite();
        }
    }

    private void checkPermissionWrite() {
        int permissionCheckWrite =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheckWrite != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1001);
        } else {
            getListMusic();
        }
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }

    private void playMusic(int i) {
        serviceBinder.setAudioFile(listMusic.get(i).getUri());
//        serviceBinder.setStreamAudio(true);
        serviceBinder.setContext(this);
//        serviceBinder.setAudioProgressUpdateHandler(audioProgressUpdateHandler);
        serviceBinder.startAudio();
        
//        Intent intent = new Intent(this, MusicService.class);
//        intent.putExtra("link", listMusic.get(i).getUri());
//        startService(intent);


//        Uri uri = Uri.fromFile(new File(listMusic.get(i).getUri()));
//        MediaPlayer mediaPlayer = MediaPlayer.create(this, uri);
//        if (mediaPlayer != null) {
//            mediaPlayer.setLooping(false);
//            mediaPlayer.start();
//        } else {
//            System.out.println("Media null ma oi");
//        }

//        System.out.println(Environment.getExternalStorageDirectory().getAbsolutePath());
//        System.out.println("Music file: " + listMusic.get(i).getUri());
////        String path = Environment.getExternalStorageDirectory(); //getExternalStorage() + "/"+ fileName+".mp3";
//        MediaPlayer player = new MediaPlayer();
//
//        try {
//            player.setDataSource(listMusic.get(i).getUri());
//            player.prepare();
//            player.start();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            System.out.println("Exception of type : " + e.toString());
//            e.printStackTrace();
//        }

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
}
