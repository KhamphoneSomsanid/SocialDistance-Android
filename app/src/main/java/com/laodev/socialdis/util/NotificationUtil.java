package com.laodev.socialdis.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.laodev.socialdis.R;
import com.laodev.socialdis.activity.MainActivity;
import com.laodev.socialdis.model.HistoryModel;

public class NotificationUtil extends ContextWrapper {

    public static final String NOTIFICATION_CHANNEL_NAME_SERIVCE = "Messages Notifications";
    public static final String NOTIFICATION_CHANNEL_ID_SERIVCE = "Messages_Notifications_ID";

    private NotificationManager manager;

    public NotificationUtil(Context base) {
        super(base);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel messagesChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID_SERIVCE,
                    NOTIFICATION_CHANNEL_NAME_SERIVCE, NotificationManager.IMPORTANCE_HIGH);
            messagesChannel.setVibrationPattern(getVibrationPattern());
            getManager().createNotificationChannel(messagesChannel);
        }
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    private long[] getVibrationPattern() {
        return SharedPreferenceUtil.isVibrateEnabled() ? new long[]{200, 200} : new long[0];
    }

    public Notification createActiveNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String messageBody = "Starting Social Distance Service";
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Running Service!")
                        .setContentText(messageBody)
                        .setVibrate(getVibrationPattern())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(SharedPreferenceUtil.isVibrateEnabled());
            getManager().createNotificationChannel(channel);
        }
        getManager().notify(2 , notificationBuilder.build());
        return notificationBuilder.build();
    }

    public Notification createActiveNotification(HistoryModel historyModel) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String title = historyModel.status;
        String messageBody = "Covid Distance Notification";
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setVibrate(getVibrationPattern())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);

            channel.enableVibration(SharedPreferenceUtil.isVibrateEnabled());
            getManager().createNotificationChannel(channel);
        }
        getManager().notify(1 , notificationBuilder.build());
        return notificationBuilder.build();
    }

}
