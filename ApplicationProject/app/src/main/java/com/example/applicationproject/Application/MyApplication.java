package com.example.applicationproject.Application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.lifecycle.ViewModelProvider;

import com.example.applicationproject.View_Controller.ShareDataMission;

public class MyApplication extends Application {
    public static final String CHANEL_ID = "channel_service";

    @Override
    public void onCreate() {
        super.onCreate();
        createchannelNotification();
    }

    private void createchannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Notification Channel";
            String description = "Channel for foreground service";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANEL_ID, name, importance);
            channel.setDescription(description);

            // Đăng ký channel với hệ thống
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
