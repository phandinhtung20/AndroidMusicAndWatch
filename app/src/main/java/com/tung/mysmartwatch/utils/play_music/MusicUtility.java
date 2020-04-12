package com.tung.mysmartwatch.utils.play_music;

import java.util.Date;

public class MusicUtility {
    public static int getNextTime() {
        Date now = new Date();
        return (1000 - (int) now.getTime()%1000);
    }
}
