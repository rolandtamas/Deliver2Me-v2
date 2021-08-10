package com.app.deliver2me.notificationpack;

public class NotificationModel {
    private String title;
    private String body;

    public NotificationModel(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public NotificationModel() {
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
}
