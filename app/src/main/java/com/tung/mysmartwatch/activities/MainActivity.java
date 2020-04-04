package com.tung.mysmartwatch.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.tung.mysmartwatch.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView tvDate;

    private SimpleDateFormat dateFormat;

    private Handler handler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int nextTime = updateTime();
            handler.postDelayed(this, nextTime);
        }
    };

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler = new Handler();
        tvDate = findViewById(R.id.tv_date);
        dateFormat = new SimpleDateFormat("hh : mm : ss");
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigatorBar();
        handler.postDelayed(runnable, 100);
    }

    private int updateTime() {
        Date now = new Date();
        tvDate.setText(dateFormat.format(now));
        return (int) (1000 - (now.getTime() % 1000));
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
}
