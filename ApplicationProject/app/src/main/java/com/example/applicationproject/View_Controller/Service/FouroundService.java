package com.example.applicationproject.View_Controller.Service;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC;
import static com.example.applicationproject.Application.MyApplication.CHANEL_ID;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.IBinder;
import android.widget.ImageButton;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.applicationproject.R;
import com.example.applicationproject.View_Controller.BroadCastReciver.MyReceiver;
import com.example.applicationproject.View_Controller.FragmentLogin.MeFragment;

import java.security.Provider;
import java.util.List;
import java.util.Map;

public class FouroundService extends Service {

    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String action = intent.getStringExtra("action");
        if (action != null){
            if (action.equals("stop_service")){
                stopSelf();
            }
        }

        sendNotification(title, content);

        return START_NOT_STICKY;
    }

    @SuppressLint("ForegroundServiceType")
    private void sendNotification(String title, String content) {
        Intent intent = new Intent(this, MeFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        @SuppressLint("RemoteViewLayout") RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notifycation_layout);
        remoteViews.setOnClickPendingIntent(R.id.imageButton_notify, getPendingIntent());
        Notification notification = new NotificationCompat.Builder(this, CHANEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setCustomContentView(remoteViews)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("action", "stop_service");
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);
    }

    public void onDestroy(){
        super.onDestroy();
    }
}
