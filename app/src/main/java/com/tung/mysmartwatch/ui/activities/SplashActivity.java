package com.tung.mysmartwatch.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tung.mysmartwatch.R;
import com.tung.mysmartwatch.utils.permissions.PermissionManager;

import static com.tung.mysmartwatch.utils.permissions.PermissionManager.REQUEST_CODE_READ;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            Intent intent = new Intent(SplashActivity.this, RoutingActivity.class);
//            Intent intent = new Intent(SplashActivity.this, ControllerActivity.class);
            Intent intent = new Intent(SplashActivity.this, ConfigureActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        handler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigatorBar();

        checkPermission();
    }

    private void checkPermission() {
        if (!PermissionManager.checkPermissionRead(this)) {
            PermissionManager.requestPermissionRead(this);
            return;
        }
        if (!PermissionManager.checkPermissionForeground(this)) {
            PermissionManager.requestPermissionForeground(this);
            return;
        }
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        LinearLayout ll = findViewById(R.id.bg);
        ll.setBackground(wallpaperDrawable);
        handler.postDelayed(runnable, 3000);
    }


    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_READ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handler.postDelayed(runnable, 1000);
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }
}
