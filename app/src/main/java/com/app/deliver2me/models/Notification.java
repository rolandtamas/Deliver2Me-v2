package com.app.deliver2me.models;

public class Notification {

    private String title;
    private String body;
    private String author;

    public Notification(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Notification() {
    }

    public Notification(String title, String body, String author) {
        this.title = title;
        this.body = body;
        this.author = author;
    }

    public Notification(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
