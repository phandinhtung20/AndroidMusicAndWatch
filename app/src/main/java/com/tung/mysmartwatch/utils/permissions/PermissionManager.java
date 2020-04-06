package com.tung.mysmartwatch.utils.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

public class PermissionManager {
    public static final int REQUEST_CODE_READ = 0x01;
    private static final String permissionRead = Manifest.permission.READ_EXTERNAL_STORAGE;

    public static boolean checkPermissionRead(Context context) {
        int permissionCheckRead = ContextCompat.checkSelfPermission(context, permissionRead);
        return permissionCheckRead == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestPermission(Activity activity) {
        activity.requestPermissions(new String[]{permissionRead}, REQUEST_CODE_READ);
    }
}
