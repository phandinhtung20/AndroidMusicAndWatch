package com.tung.mysmartwatch.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.tung.mysmartwatch.R;
import com.tung.mysmartwatch.utils.broadcast.BroadcastUtils;

public class ConfigureActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findViewById(R.id.btn_control).setOnClickListener(this);
        findViewById(R.id.btn_watch).setOnClickListener(this);

        checkNotch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigatorBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        Intent intentBC = null;
        switch (view.getId()) {
            case R.id.btn_watch:
                intentBC = new Intent("music.event");
                intentBC.putExtra("name", "Watch press");
                intent = new Intent(this, MainActivity.class);
                break;
            case R.id.btn_control:
                intentBC = new Intent("music.event");
                intentBC.putExtra("name", "Control press");
                intent = new Intent(this, ControllerActivity.class);
                break;
        }
        if (intentBC != null) {
            BroadcastUtils.sendImplicitBroadcast(this, intentBC);
        }
        if (intent != null) {
            startActivity(intent);
        }
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

    private void checkNotch() {
        final LinearLayout status_bar = findViewById(R.id.ll_status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            status_bar.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
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
