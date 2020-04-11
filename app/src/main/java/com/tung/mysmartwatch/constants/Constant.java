package com.tung.mysmartwatch.constants;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tung.mysmartwatch.R;

public class Constant {
    public interface ACTION {
        public static String MAIN_ACTION = "com.tung.mysmartwatch.action.main";
        public static String INIT_ACTION = "com.tung.mysmartwatch.action.init";
        public static String PREV_ACTION = "com.tung.mysmartwatch.action.prev";
        public static String PLAY_ACTION = "com.tung.mysmartwatch.action.play";
        public static String NEXT_ACTION = "com.tung.mysmartwatch.action.next";
        public static String STARTFOREGROUND_ACTION = "com.tung.mysmartwatch.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.tung.mysmartwatch.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_album_art, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }
}
