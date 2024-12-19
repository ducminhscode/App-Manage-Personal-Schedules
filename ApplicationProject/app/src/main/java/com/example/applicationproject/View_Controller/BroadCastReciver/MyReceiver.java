package com.example.applicationproject.View_Controller.BroadCastReciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.applicationproject.View_Controller.Service.FouroundService;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("action");

        Intent serviceIntent = new Intent(context, FouroundService.class);
        serviceIntent.putExtra("answers", action);
        if (action != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            }
        }

    }
}
