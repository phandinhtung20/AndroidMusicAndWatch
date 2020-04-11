package com.tung.mysmartwatch.utils.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

public class PermissionManager {
    public static final int REQUEST_CODE_READ = 0x01;
    public static final int REQUEST_CODE_FOREGROUND = 0x01;
    private static final String permissionRead = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String permissionForeground = Manifest.permission.FOREGROUND_SERVICE;

    public static boolean checkPermissionRead(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheckRead = ContextCompat.checkSelfPermission(context, permissionRead);
            return permissionCheckRead == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void requestPermissionRead(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new String[]{permissionRead}, REQUEST_CODE_READ);
        }
    }

    public static boolean checkPermissionForeground(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheckRead = ContextCompat.checkSelfPermission(context, permissionForeground);
            return permissionCheckRead == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void requestPermissionForeground(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new String[]{permissionForeground}, REQUEST_CODE_FOREGROUND);
        }
    }
}
