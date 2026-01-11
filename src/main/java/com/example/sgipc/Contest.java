package com.example.sgipc;

public class Contest {
    private String title;
    private String time;
    private int duration;
    private String link;

    public Contest() {}

    public Contest(String title, String time, int duration, String link) {
        this.title = title;
        this.time = time;
        this.duration = duration;
        this.link = link;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
}