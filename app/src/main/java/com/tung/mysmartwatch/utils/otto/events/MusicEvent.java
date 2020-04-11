package com.tung.mysmartwatch.utils.otto.events;

public class MusicEvent {
    private String name;
    private boolean play;
    private int duration;
    private int current;

    public MusicEvent(String name, boolean play, int duration, int current) {
        this.name = name;
        this.play = play;
        this.duration = duration;
        this.current = current;
    }

    public String getName() {
        return name;
    }

    public boolean isPlay() {
        return play;
    }

    public int getDuration() {
        return duration;
    }

    public int getCurrent() {
        return current;
    }
}
