package com.tung.mysmartwatch.utils.broadcast;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class BroadcastUtils {

    public static void sendImplicitBroadcast(Context context, Intent intent) {
//        PackageManager pm = context.getPackageManager();
//        List<ResolveInfo> matches = pm.queryBroadcastReceivers(intent, 0);
//
//        for (ResolveInfo info : matches) {
//            Intent explicit = new Intent(intent);
//            ComponentName cn = new ComponentName(info.activityInfo.applicationInfo.packageName,
//                    info.activityInfo.name);
//
//            explicit.setComponent(cn);
//        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
