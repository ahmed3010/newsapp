package com.shohayeb.newsapp;

/**
 * Created by Ahmad on 19/04/2018.
 */

public class News {
    private String title, section, date, author, webUrl;

    public News(String title, String section, String date, String webUrl) {
        this.title = title;
        this.section = section;
        this.date = date;
        this.webUrl = webUrl;
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

