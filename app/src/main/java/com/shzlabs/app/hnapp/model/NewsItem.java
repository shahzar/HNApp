package com.shzlabs.app.hnapp.model;

/**
 * Created by shaz on 24/4/16.
 */
public class NewsItem {
    String title;
    String url;
    String type;
    String by;
    long descendants;
    long id;
    long score;
    long time;

    public NewsItem(){}

    public NewsItem(String title, String url, String type, String by, long id, long score, long time){
        this.title = title;
        this.url = url;
        this.type = type;
        this.by = by;
        this.id = id;
        this.score = score;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public String getBy() {
        return by;
    }

    public long getScore() {
        return score;
    }

    public long getTime() {
        return time;
    }
}
