package com.shohayeb.newsapp;

import java.io.Serializable;

/**
 * Created by Ahmad on 19/04/2018.
 */

public class News implements Serializable {
    public static final long serialVersionUID = 21042018L;
    private String title, section, date, author, webUrl;

    public News(String title, String section, String date, String webUrl, String author) {
        this.title = title;
        this.section = section;
        this.date = date;
        this.webUrl = webUrl;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getWebUrl() {
        return webUrl;
    }
}

