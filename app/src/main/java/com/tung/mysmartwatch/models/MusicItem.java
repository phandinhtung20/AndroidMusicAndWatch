package com.tung.mysmartwatch.models;

public class MusicItem implements Comparable<MusicItem> {
    private String name;
    private String singer;
    private String uri;
    private int duration;

    public MusicItem(String name, String singer, String uri, int duration) {
        this.name = name;
        this.singer = singer;
        this.uri = uri;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getDuration() {
        return duration;
    }

    public String getDurationString() {
        int temp = duration / 1000;
        return String.format("%02d:%02d", temp / 60, (temp % 60));
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public int compareTo(MusicItem musicItem) {
        return this.getName().compareTo(musicItem.getName());
    }
}
