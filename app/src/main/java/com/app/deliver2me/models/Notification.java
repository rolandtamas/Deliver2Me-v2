package com.app.deliver2me.models;

public class Notification {

    private String title;
    private String body;
    private String author;
    private String takenBy;
    private String authorPhoneNo;
    private String address;

    public Notification(String title, String body, String author, String takenBy, String authorPhoneNo, String address) {
        this.title = title;
        this.body = body;
        this.author = author;
        this.takenBy = takenBy;
        this.authorPhoneNo = authorPhoneNo;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAuthorPhoneNo() {
        return authorPhoneNo;
    }

    public void setAuthorPhoneNo(String authorPhoneNo) {
        this.authorPhoneNo = authorPhoneNo;
    }

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

    public String getTakenBy() {
        return takenBy;
    }

    public void setTakenBy(String takenBy) {
        this.takenBy = takenBy;
    }

    public Notification(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public Notification(String title, String body, String author, String takenBy) {
        this.title = title;
        this.body = body;
        this.author = author;
        this.takenBy = takenBy;
    }

    public Notification(String title, String body, String author, String takenBy, String authorPhoneNo) {
        this.title = title;
        this.body = body;
        this.author = author;
        this.takenBy = takenBy;
        this.authorPhoneNo = authorPhoneNo;
    }
}
