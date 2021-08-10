package com.app.deliver2me.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.app.deliver2me.helpers.NotificationHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TakeOrderService extends FirebaseMessagingService {
    private static final String TAG = TakeOrderService.class.getSimpleName();
    public TakeOrderService()
    {

    }

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getNotification()!=null)
        {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            NotificationHelper.displayNotification(getApplicationContext(),title,body);
        }
    }
}
