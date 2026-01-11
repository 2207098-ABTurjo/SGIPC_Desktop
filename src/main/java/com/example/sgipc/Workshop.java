package com.example.sgipc;

public class Workshop {
    private String title;
    private String time;
    private int duration;

    public Workshop() {}

    public Workshop(String title, String time, int duration) {
        this.title = title;
        this.time = time;
        this.duration = duration;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
}