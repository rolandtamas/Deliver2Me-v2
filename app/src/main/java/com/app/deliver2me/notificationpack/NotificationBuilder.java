package com.app.deliver2me.notificationpack;

import com.google.gson.annotations.SerializedName;

public class NotificationBuilder {

    @SerializedName("to")
    private String userToken;
    @SerializedName("notification")
    private NotificationModel notificationModel;

    public NotificationBuilder(){}

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public NotificationModel getNotificationModel() {
        return notificationModel;
    }

    public void setNotificationModel(NotificationModel notificationModel) {
        this.notificationModel = notificationModel;
    }
}
