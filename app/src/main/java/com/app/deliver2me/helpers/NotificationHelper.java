package com.app.deliver2me.helpers;

import android.content.Context;
import android.media.RingtoneManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.app.deliver2me.R;
import com.app.deliver2me.activities.ViewAdActivity;

public class NotificationHelper {

    public static void displayNotification(Context context, String title, String body)
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, ViewAdActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.favicon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .setContentText(body);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,mBuilder.build());
    }
}
